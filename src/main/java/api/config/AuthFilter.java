package api.config;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/telaInicialAdmin.jsp"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        String tipo = (session != null) ? (String) session.getAttribute("tipo") : null;

        if (tipo == null) {
            // Não está logado, redireciona para login
            response.sendRedirect("login.jsp");
            return;
        }

        if (!tipo.equals("admin")) {
            // Logado, mas não é admin, acesso negado
            response.sendRedirect("acessoNegado.jsp");
            return;
        }

        // É admin, deixa passar
        chain.doFilter(req, res);
    }
}
