package api.controllers;

import java.io.IOException;

import api.dao.UsuarioDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import api.models.UsuarioModel;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/cadastroConta")
public class CadastroContaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nome = req.getParameter("nome");
        int idade  = req.getParameter("idade") == null ? 0 : Integer.parseInt(req.getParameter("idade"));
        String email = req.getParameter("email");
        String telefone = req.getParameter("telefone");
        String senha = req.getParameter("senha");
        String confirmSenha = req.getParameter("confirmacaoSenha");

        if (email == null || senha == null || confirmSenha == null || email.isBlank() || senha.isBlank()) {
            req.setAttribute("erro", "Campos obrigatórios estão vazios");
            req.getRequestDispatcher("cadastroConta.jsp").forward(req, resp);
            req.removeAttribute("erro");
            return;
        }
        if (!senha.equals(confirmSenha)) {
            req.setAttribute("erro", "As senhas não coincidem");
            req.getRequestDispatcher("cadastroConta.jsp").forward(req, resp);
            req.removeAttribute("erro");
            return;
        }

        if (UsuarioDao.buscarPorEmail(email).isPresent()) {
            req.setAttribute("erro", "E-mail já cadastrado!");
            req.getRequestDispatcher("cadastroConta.jsp").forward(req, resp);
            req.removeAttribute("erro");
            return;
        }

        String hash = BCrypt.hashpw(senha, BCrypt.gensalt());
        UsuarioModel novo = new UsuarioModel(nome, idade, email, telefone, hash);
        UsuarioDao.salvar(novo);

        req.getSession().setAttribute("usuario", novo);
        resp.sendRedirect("telaInicial.jsp");
    }
}