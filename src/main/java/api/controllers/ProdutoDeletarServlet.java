package api.controllers;

import java.io.IOException;
import java.util.Optional;

import api.dao.ProdutoDao;
import api.models.ProdutoModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/produto/deletar")
public class ProdutoDeletarServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isBlank()) {
            erro(req, resp, "ID não informado.");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);

            Optional<ProdutoModel> opt = ProdutoDao.buscarPorId(id);   // só ativos
            if (opt.isEmpty()) {
                erro(req, resp, "Produto não encontrado ou já inativo.");
                return;
            }

            ProdutoDao.deletar(opt.get());     // Hibernate executa UPDATE ativo=false
            resp.sendRedirect(req.getContextPath() + "/telaInicialAdmin");

        } catch (NumberFormatException e) {
            erro(req, resp, "ID inválido.");
        }
    }

    private void erro(HttpServletRequest req, HttpServletResponse resp, String msg)
            throws ServletException, IOException {
        req.setAttribute("erro", msg);
        req.setAttribute("produtos", ProdutoDao.listarTodos());
        req.getRequestDispatcher("telaInicialAdmin.jsp").forward(req, resp);
    }
}