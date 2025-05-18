package api.controllers;

import api.dao.ProdutoDao;
import api.models.ProdutoModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/telaInicialAdmin")
public class TelaInicialAdminServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(TelaInicialAdminServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Verificar se o usuário está logado como administrador
        try {
            List<ProdutoModel> produtos = ProdutoDao.listarTodos();
            req.setAttribute("produtos", produtos);
            LOGGER.info("Carregados " + produtos.size() + " produtos para exibição");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar produtos", e);
            req.setAttribute("erro", "Erro ao carregar produtos: " + e.getMessage());
        }

        req.getRequestDispatcher("telaInicialAdmin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nome = req.getParameter("nome-produto");
        String precoStr = req.getParameter("preco-produto");
        String descricao = req.getParameter("descricao-produto");

        if (nome == null || precoStr == null || nome.isBlank() || precoStr.isBlank()) {
            req.setAttribute("erro", "Preencha todos os campos.");
            req.setAttribute("produtos", ProdutoDao.listarTodos()); // carrega lista para a JSP
            req.getRequestDispatcher("telaInicialAdmin.jsp").forward(req, resp);
            req.removeAttribute("erro");
            return;
        }

        if (ProdutoDao.buscarPorNome(nome).isPresent() && ProdutoDao.buscarPorPreco(Double.parseDouble(precoStr)).isPresent() && ProdutoDao.buscarPorDescricao(descricao).isPresent()) {
            req.setAttribute("erro", "Produto já cadastrado.");
            req.setAttribute("produtos", ProdutoDao.listarTodos());
            req.getRequestDispatcher("telaInicialAdmin.jsp").forward(req, resp);
            req.removeAttribute("erro");
            return;
        }

        try {
            double preco = Double.parseDouble(precoStr);
            ProdutoModel p = new ProdutoModel();
            p.setNome(nome);
            p.setPreco(preco);
            p.setDescricao(descricao);
            ProdutoDao.salvar(p);
            resp.sendRedirect("telaInicialAdmin");  // redireciona para o servlet que lista os produtos

        } catch (NumberFormatException e) {
            req.setAttribute("erro", "Preço inválido.");
            req.setAttribute("produtos", ProdutoDao.listarTodos());
            req.getRequestDispatcher("telaInicialAdmin.jsp").forward(req, resp);
            req.removeAttribute("erro");
        }
    }
}