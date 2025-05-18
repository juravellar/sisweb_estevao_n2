package api.controllers;

import api.dao.UsuarioDao;
import api.models.UsuarioModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/telaInicial")
public class TelaInicialServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UsuarioModel usuarioSessao = (UsuarioModel) session.getAttribute("usuario");

        if (usuarioSessao != null) {
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

        request.getRequestDispatcher("/telaInicial.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);   // mesma lógica
    }
}
