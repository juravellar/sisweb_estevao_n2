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

@WebServlet("/produtos")
public class ProdutoBuscaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String busca = req.getParameter("busca");
        String precoMinStr = req.getParameter("precoMin");
        String precoMaxStr = req.getParameter("precoMax");

        Double precoMin = null;
        Double precoMax = null;

        try {
            if (precoMinStr != null && !precoMinStr.isBlank()) {
                precoMin = Double.parseDouble(precoMinStr);
            }
            if (precoMaxStr != null && !precoMaxStr.isBlank()) {
                precoMax = Double.parseDouble(precoMaxStr);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("erro", "Valores de preço inválidos.");
        }

        List<ProdutoModel> produtos = ProdutoDao.buscarPorFiltros(busca, precoMin, precoMax);

        req.setAttribute("produtos", produtos);
        req.setAttribute("busca", busca);
        req.setAttribute("precoMin", precoMinStr);
        req.setAttribute("precoMax", precoMaxStr);

        req.getRequestDispatcher("produtos.jsp").forward(req, resp);
    }
}
