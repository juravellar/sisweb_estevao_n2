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

@WebServlet("/produto/editar")
public class ProdutoEditarServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idStr     = req.getParameter("id");
        String nome      = req.getParameter("nome-produto");
        String precoStr  = req.getParameter("preco-produto");
        String descricao = req.getParameter("descricao-produto");

        if (idStr == null || nome == null || precoStr == null
                || nome.isBlank() || precoStr.isBlank()) {
            erro(req, resp, "Preencha todos os campos.");
            return;
        }

        try {
            int    id    = Integer.parseInt(idStr);
            double preco = Double.parseDouble(precoStr);

            Optional<ProdutoModel> opt = ProdutoDao.buscarPorId(id);   // só traz ativos por causa do @Where
            if (opt.isEmpty()) {
                erro(req, resp, "Produto não encontrado ou inativo.");
                return;
            }

            ProdutoModel p = opt.get();
            p.setNome(nome);
            p.setPreco(preco);
            p.setDescricao(descricao);

            ProdutoDao.editar(Optional.of(p));                 // faz session.merge()
            resp.sendRedirect(req.getContextPath() + "/telaInicialAdmin");

        } catch (NumberFormatException e) {
            erro(req, resp, "ID ou preço inválido.");
        }
    }

    private void erro(HttpServletRequest req, HttpServletResponse resp, String msg)
            throws ServletException, IOException {
        req.setAttribute("erro", msg);
        req.setAttribute("produtos", ProdutoDao.listarTodos());
        req.getRequestDispatcher("telaInicialAdmin.jsp").forward(req, resp);
    }
}