package lk.edu.icbt.sunrise.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

import java.util.UUID;

@WebFilter("/*") public final class CsrfFilter implements Filter {
    public void doFilter(ServletRequest r, ServletResponse s, FilterChain c) throws IOException, ServletException {
        HttpServletRequest q =(HttpServletRequest) r;
        HttpSession session = q.getSession();
        String token =(String) session.getAttribute("csrfToken");
        if (token == null) {
            token = UUID.randomUUID().toString();
            session.setAttribute("csrfToken", token);
        }
        if ("POST".equalsIgnoreCase(q.getMethod()) && !q.getRequestURI().endsWith("/login") && !token.equals(q.getParameter("csrfToken"))) {
           ((HttpServletResponse) s).sendError(403, "Invalid CSRF token");
            return;
        }
        c.doFilter(r, s);
    }
}
