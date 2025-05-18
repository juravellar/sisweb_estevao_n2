package api.controllers;

import api.dao.ProdutoDao;
import api.dao.UsuarioDao;
import api.models.ProdutoModel;
import api.models.UsuarioModel;
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

@WebServlet("/telaInicial")
public class TelaInicialServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(TelaInicialServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UsuarioModel usuarioSessao = (UsuarioModel) session.getAttribute("usuario");
        try {
            System.out.println("produtos");
            if (usuarioSessao != null) {
                List<ProdutoModel> produtos = ProdutoDao.listarTodos();
                session.setAttribute("produtos", produtos);

                LOGGER.info("Carregados " + produtos.size() + " produtos para exibição");

                UsuarioDao.buscarPorId(usuarioSessao.getId()).ifPresentOrElse(
                        usuario -> {
                            request.setAttribute("usuario", usuario);
                            session.setAttribute("usuario", usuario);   // mantém atualizado
                        },
                        () -> {
                            request.setAttribute("usuario", null);
                            session.removeAttribute("usuario");
                        }
                );
            } else {
                request.setAttribute("usuario", null);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar produtos", e);
            session.setAttribute("erro", "Erro ao carregar produtos: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);   // mesma lógica
    }
}
