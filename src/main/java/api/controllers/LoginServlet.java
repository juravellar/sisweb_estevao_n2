package api.controllers;

import api.dao.UsuarioDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import api.models.UsuarioModel;
import org.mindrot.jbcrypt.BCrypt;  // import da lib bcrypt
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String senha = req.getParameter("senha");
        HttpSession session = req.getSession();

        if (email == null || senha == null || email.isBlank() || senha.isBlank()) {
            session.setAttribute("erro", "Campos obrigatórios não foram preenchidos");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
            session.removeAttribute("erro");
            return;
        }

        if (!UsuarioDao.temUsuariosCadastrados()) {
            session.setAttribute("erro", "Nenhum usuário cadastrado. Faça o cadastro primeiro e tente novamente.");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
            session.removeAttribute("erro");
            return;
        }

        if (email.equals("admin@admin") && senha.equals("admin")) {
            resp.sendRedirect("telaInicialAdmin.jsp");
            session.setAttribute("tipo", "admin");
            session.setAttribute("usuario", email);  // ou outro atributo de usuário
            return;
        }

        var optUsuario = UsuarioDao.buscarPorEmail(email);
        if (optUsuario.isEmpty()) {
            session.setAttribute("erro", "Usuário não encontrado.");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
            session.removeAttribute("erro");
            return;
        }

        UsuarioModel usuario = optUsuario.get();
        if (!BCrypt.checkpw(senha, usuario.getSenha())) {
            session.setAttribute("erro", "Senha incorreta.");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
            session.removeAttribute("erro");
            return;
        }

        // Usuário comum
        session.setAttribute("tipo", "usuario");
        session.setAttribute("usuario", usuario);
        resp.sendRedirect("telaInicial.jsp");
    }

}
