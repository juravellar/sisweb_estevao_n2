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
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("adminLogado") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

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
        // Se necessário, redirecionar para o doGet para recarregar a página
        doGet(req, resp);
    }
}