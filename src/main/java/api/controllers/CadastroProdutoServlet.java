package api.controllers;

import java.io.IOException;

import api.dao.ProdutoDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import api.models.ProdutoModel;

@WebServlet("/cadastroProduto")
public class CadastroProdutoServlet extends HttpServlet {

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