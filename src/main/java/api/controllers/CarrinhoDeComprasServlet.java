package api.controllers;

import api.config.HibernateUtil;
import api.dao.ProdutoDao;
import api.dao.UsuarioDao;
import api.models.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/carrinho")
public class CarrinhoDeComprasServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession httpSession = req.getSession();
        UsuarioModel usuario = (UsuarioModel) httpSession.getAttribute("usuario");

        if (usuario == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String acao = req.getParameter("acao");
        String idStr = req.getParameter("id");

        if (idStr == null) {
            StringBuilder body = new StringBuilder();
            BufferedReader br = req.getReader();
            for (String line; (line = br.readLine()) != null; ) body.append(line);
            Matcher m = Pattern.compile("\"id\"\\s*:\\s*(\\d+)").matcher(body.toString());
            if (m.find()) { idStr = m.group(1); acao = "adicionar"; }
        }

        try (Session s = HibernateUtil.getSessionFactory().openSession()) {

            Transaction tx = s.beginTransaction();

            if ("adicionar".equals(acao) || "remover".equals(acao)) {

                int idProduto = Integer.parseInt(idStr);
                ProdutoModel produto = ProdutoDao.buscarPorId(idProduto).orElse(null);
                if (produto == null) { resp.sendError(404); return; }

                ItemCarrinhoModel item = s.createQuery(
                                "FROM ItemCarrinhoModel i WHERE i.usuario.id = :u AND i.produto.id = :p",
                                ItemCarrinhoModel.class)
                        .setParameter("u", usuario.getId())
                        .setParameter("p", idProduto)
                        .uniqueResult();

                if ("adicionar".equals(acao)) {
                    if (item == null) {
                        item = new ItemCarrinhoModel();
                        item.setUsuario(usuario);
                        item.setProduto(produto);
                        item.setQuantidade(1);
                        s.persist(item);
                    } else {
                        item.setQuantidade(item.getQuantidade() + 1);
                    }
                } else {
                    if (item != null) {
                        if (item.getQuantidade() > 1) item.setQuantidade(item.getQuantidade() - 1);
                        else s.remove(item);
                    }
                }

                tx.commit();

                if ("application/json".equalsIgnoreCase(req.getContentType())) {
                    resp.setContentType("application/json");
                    resp.getWriter().write("{\"success\":true}");
                    return;
                }
                resp.sendRedirect("carrinho.jsp");
                return;
            }

            if ("finalizar".equals(acao)) {

                List<ItemCarrinhoModel> itens = s.createQuery(
                                "FROM ItemCarrinhoModel i WHERE i.usuario.id = :u", ItemCarrinhoModel.class)
                        .setParameter("u", usuario.getId())
                        .list();

                if (itens.isEmpty()) {
                    req.setAttribute("erro", "Carrinho vazio.");
                    req.getRequestDispatcher("carrinho.jsp").forward(req, resp);
                    req.removeAttribute("erro");
                    return;
                }

                PedidoModel pedido = new PedidoModel();
                pedido.setUsuario(usuario);
                pedido.setDataPedido(LocalDateTime.now());

                for (ItemCarrinhoModel carrinhoItem : itens) {
                    ItemPedidoModel ip = new ItemPedidoModel();
                    ip.setPedido(pedido);
                    ip.setProduto(carrinhoItem.getProduto());
                    ip.setQuantidade(carrinhoItem.getQuantidade());
                    pedido.getItens().add(ip);
                    s.remove(carrinhoItem);
                }

                s.persist(pedido);
                tx.commit();

                resp.sendRedirect("pedidoConfirmado.jsp");
                return;
            }

            tx.rollback();
            resp.sendRedirect("carrinho.jsp");
        } catch (Exception e) {
            req.setAttribute("erro", "Erro: " + e.getMessage());
            req.getRequestDispatcher("carrinho.jsp").forward(req, resp);
            req.removeAttribute("erro");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession hs = req.getSession();
        UsuarioModel u = (UsuarioModel) hs.getAttribute("usuario");

        if (u != null) {
            UsuarioDao.buscarPorId(u.getId()).ifPresent(usuario -> {
                Hibernate.initialize(usuario.getItensCarrinho()); // <-- resolvendo LazyInitializationException
                req.setAttribute("usuario", usuario);
            });
        }
        req.getRequestDispatcher("carrinho.jsp").forward(req, resp);
    }
}