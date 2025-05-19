package api.controllers;

import api.dao.ProdutoDao;
import api.models.ProdutoModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/telaInicialAdmin")
public class TelaInicialAdminServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(TelaInicialAdminServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<ProdutoModel> produtos = ProdutoDao.listarTodos();
            req.setAttribute("produtos", produtos);

            String editarIdStr = req.getParameter("editarId");
            if (editarIdStr != null && !editarIdStr.isBlank()) {
                try {
                    long editarId = Long.parseLong(editarIdStr);
                    ProdutoDao.buscarPorId(editarId).ifPresent(produto -> req.setAttribute("produtoEditar", produto));
                } catch (NumberFormatException e) {
                    req.setAttribute("erro", "ID inválido para edição.");
                }
            }
            LOGGER.info("Carregados " + produtos.size() + " produtos para exibição");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar produtos", e);
            req.setAttribute("erro", "Erro ao carregar produtos: " + e.getMessage());
        }

        req.getRequestDispatcher("telaInicialAdmin.jsp").forward(req, resp);
    }
}
