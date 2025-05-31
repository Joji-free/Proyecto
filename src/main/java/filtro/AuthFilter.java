package filtro;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String uri = request.getRequestURI();
        HttpSession session = request.getSession(false);

        boolean esLogin = uri.endsWith("login") || uri.endsWith("index.jsp")
                || uri.contains("css/") || uri.contains("js/") || uri.contains("images/");
        boolean yaLogueado = (session != null && session.getAttribute("usuario") != null);

        if (yaLogueado || esLogin) {
            chain.doFilter(req, res);
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

    @Override
    public void destroy() { }
}
