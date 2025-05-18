package api.controllers;

import api.dao.ProdutoDao;
import api.models.ProdutoModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = {
        "/produtos",          // listagem / busca  (GET)
        "/produto",           // cadastro          (POST)
        "/produto/editar",    // edição            (POST)
        "/produto/deletar"    // exclusão          (POST)
})
public class ProdutoServlet extends HttpServlet {

    /* ============================ GET ============================ */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // só “/produtos” usa GET
        listarProdutos(req, resp);
    }

    /* =========================== POST ============================ */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();

        switch (path) {
            case "/produto"         -> cadastrarProduto(req, resp);
            case "/produto/editar"  -> editarProduto(req, resp);
            case "/produto/deletar" -> deletarProduto(req, resp);
            default                 -> resp.sendError(405);
        }
    }

    /* ====================== LISTAGEM / BUSCA ===================== */
    private void listarProdutos(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String busca      = req.getParameter("busca");
        String precoMinSt = req.getParameter("precoMin");
        String precoMaxSt = req.getParameter("precoMax");

        Double precoMin = parseDoubleOrNull(precoMinSt);
        Double precoMax = parseDoubleOrNull(precoMaxSt);

        if ((precoMinSt != null && precoMin == null) ||
                (precoMaxSt != null && precoMax == null)) {
            req.setAttribute("erro", "Valores de preço inválidos.");
        }

        List<ProdutoModel> produtos =
                ProdutoDao.buscarPorFiltros(busca, precoMin, precoMax);

        req.setAttribute("produtos", produtos);
        req.setAttribute("busca", busca);
        req.setAttribute("precoMin", precoMinSt);
        req.setAttribute("precoMax", precoMaxSt);

        req.getRequestDispatcher("produtos.jsp").forward(req, resp);
    }

    /* =========================== CADASTRO ======================== */
    private void cadastrarProduto(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String nome       = req.getParameter("nome-produto");
        String precoStr   = req.getParameter("preco-produto");
        String descricao  = req.getParameter("descricao-produto");

        if (nome == null || precoStr == null || nome.isBlank() || precoStr.isBlank()) {
            erro(req, resp, "Preencha todos os campos.");
            return;
        }

        try {
            double preco = Double.parseDouble(precoStr);

            boolean jaExiste =
                    ProdutoDao.buscarPorNome(nome).isPresent() &&
                            ProdutoDao.buscarPorPreco(preco).isPresent() &&
                            ProdutoDao.buscarPorDescricao(descricao).isPresent();

            if (jaExiste) {
                erro(req, resp, "Produto já cadastrado.");
                return;
            }

            ProdutoModel p = new ProdutoModel();
            p.setNome(nome);
            p.setPreco(preco);
            p.setDescricao(descricao);

            ProdutoDao.salvar(p);
            resp.sendRedirect(req.getContextPath() + "/telaInicialAdmin");

        } catch (NumberFormatException e) {
            erro(req, resp, "Preço inválido.");
        }
    }

    /* ============================ EDIÇÃO ========================= */
    private void editarProduto(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idStr     = req.getParameter("id");
        String nome      = req.getParameter("nome-produto");
        String precoStr  = req.getParameter("preco-produto");
        String descricao = req.getParameter("descricao-produto");

        if (idStr == null || nome == null || precoStr == null ||
                nome.isBlank() || precoStr.isBlank()) {
            erro(req, resp, "Preencha todos os campos.");
            return;
        }

        try {
            int    id    = Integer.parseInt(idStr);
            double preco = Double.parseDouble(precoStr);

            Optional<ProdutoModel> opt = ProdutoDao.buscarPorId(id);
            if (opt.isEmpty()) {
                erro(req, resp, "Produto não encontrado ou inativo.");
                return;
            }

            ProdutoModel p = opt.get();
            p.setNome(nome);
            p.setPreco(preco);
            p.setDescricao(descricao);

            ProdutoDao.editar(Optional.of(p));
            resp.sendRedirect(req.getContextPath() + "/telaInicialAdmin");

        } catch (NumberFormatException e) {
            erro(req, resp, "ID ou preço inválido.");
        }
    }

    /* =========================== EXCLUSÃO ======================== */
    private void deletarProduto(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isBlank()) {
            erro(req, resp, "ID não informado.");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);

            Optional<ProdutoModel> opt = ProdutoDao.buscarPorId(id);
            if (opt.isEmpty()) {
                erro(req, resp, "Produto não encontrado ou já inativo.");
                return;
            }

            ProdutoDao.deletar(opt.get());
            resp.sendRedirect(req.getContextPath() + "/telaInicialAdmin");

        } catch (NumberFormatException e) {
            erro(req, resp, "ID inválido.");
        }
    }

    /* ============================= UTIL ========================== */
    private void erro(HttpServletRequest req, HttpServletResponse resp, String msg)
            throws ServletException, IOException {
        req.setAttribute("erro", msg);
        req.setAttribute("produtos", ProdutoDao.listarTodos());
        req.getRequestDispatcher("telaInicialAdmin.jsp").forward(req, resp);
    }

    private Double parseDoubleOrNull(String str) {
        if (str == null || str.isBlank()) return null;
        try { return Double.parseDouble(str); } catch (NumberFormatException e) { return null; }
    }
}
