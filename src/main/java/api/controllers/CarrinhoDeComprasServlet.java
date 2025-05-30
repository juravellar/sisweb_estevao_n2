package api.controllers;

import api.models.ItemPedidoModel;
import api.models.PedidoModel;
import api.models.ProdutoModel;
import api.models.UsuarioModel;
import api.util.CarrinhoSessionUtil;
import api.util.HibernateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/carrinho")
public class CarrinhoDeComprasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession sess   = req.getSession();
        UsuarioModel user  = (UsuarioModel) sess.getAttribute("usuario");

        if (user == null) {                       // não logado
            resp.sendRedirect("login.jsp");
            return;
        }

        String acao   = req.getParameter("acao"); // adicionar | remover | finalizar
        String idParm = req.getParameter("id");

        if (acao != null) {
            if ("finalizar".equals(acao)) {       // GET /carrinho?acao=finalizar
                finalizarPedido(sess, user, req, resp);
                return;                           // evita segundo redirect
            }

            if (idParm != null) {
                try {
                    int idProd = Integer.parseInt(idParm);
                    switch (acao) {
                        case "adicionar" -> CarrinhoSessionUtil.add(sess, idProd);
                        case "remover"   -> CarrinhoSessionUtil.remove(sess, idProd);
                    }
                } catch (NumberFormatException ignored) { }
            }
            resp.sendRedirect("carrinho");        // PRG pattern
            return;
        }

        Map<Integer, Integer> carrinho = CarrinhoSessionUtil.getCarrinho(sess);
        Map<Integer, ProdutoModel> detalhes = new HashMap<>();

        if (!carrinho.isEmpty()) {
            try (Session hs = HibernateUtil.getSessionFactory().openSession()) {
                hs.createQuery("FROM ProdutoModel WHERE id IN (:ids)", ProdutoModel.class)
                        .setParameterList("ids", carrinho.keySet())
                        .list()
                        .forEach(p -> detalhes.put(p.getId(), p));
            }
        }

        req.setAttribute("carrinho", carrinho);
        req.setAttribute("detalhes", detalhes);
        req.getRequestDispatcher("carrinho.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession sess   = req.getSession();
        UsuarioModel user  = (UsuarioModel) sess.getAttribute("usuario");

        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String acao = req.getParameter("acao");   // adicionar | remover | finalizar
        int idProd  = 0;
        try {
            idProd = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException ignored) { }

        switch (acao) {
            case "adicionar" -> {
                CarrinhoSessionUtil.add(sess, idProd);
                resp.sendRedirect("carrinho");
            }
            case "remover" -> {
                CarrinhoSessionUtil.remove(sess, idProd);
                resp.sendRedirect("carrinho");
            }
            case "finalizar" -> {
                finalizarPedido(sess, user, req, resp); // redireciona internamente
                return;                                 // NÃO executar outro redirect
            }
            default -> resp.sendRedirect("carrinho");   // fallback
        }
    }

    private void finalizarPedido(HttpSession sess, UsuarioModel usr,
                                 HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        Map<Integer, Integer> cart = CarrinhoSessionUtil.getCarrinho(sess);

        if (cart.isEmpty()) {
            req.setAttribute("erro", "Carrinho vazio.");
            req.getRequestDispatcher("carrinho.jsp").forward(req, resp);
            return;
        }

        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = s.beginTransaction();

            PedidoModel pedido = new PedidoModel();
            pedido.setUsuario(usr);
            pedido.setDataPedido(LocalDateTime.now());

            for (var entry : cart.entrySet()) {
                ProdutoModel p = s.get(ProdutoModel.class, entry.getKey());

                ItemPedidoModel ip = new ItemPedidoModel();
                ip.setPedido(pedido);
                ip.setProduto(p);
                ip.setQuantidade(entry.getValue());

                pedido.getItens().add(ip);
            }

            s.persist(pedido);
            tx.commit();
        }

        CarrinhoSessionUtil.clear(sess);
        resp.sendRedirect("pedidoConfirmado.jsp"); // único redirect na finalização
    }
}
