package lk.edu.icbt.sunrise.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebFilter(urlPatterns = {
    "/dashboard", "/appointments/*", "/billing/*", "/reports/*", "/admin/*"
}) public final class AuthFilter implements Filter {
    public void doFilter(ServletRequest r, ServletResponse s, FilterChain c) throws IOException, ServletException {
        HttpServletRequest q =(HttpServletRequest) r;
        if (q.getSession(false) == null || q.getSession(false).getAttribute("user") == null) {
           ((HttpServletResponse) s).sendRedirect(q.getContextPath() + "/login");
            return;
        }
        c.doFilter(r, s);
    }
}
