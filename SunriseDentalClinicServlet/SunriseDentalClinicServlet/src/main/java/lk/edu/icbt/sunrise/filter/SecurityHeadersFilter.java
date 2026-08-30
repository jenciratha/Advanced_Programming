package lk.edu.icbt.sunrise.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebFilter("/*") public final class SecurityHeadersFilter implements Filter {
    public void doFilter(ServletRequest r, ServletResponse s, FilterChain c) throws IOException, ServletException {
        HttpServletResponse h =(HttpServletResponse) s;
        h.setHeader("X-Content-Type-Options", "nosniff");
        h.setHeader("X-Frame-Options", "DENY");
        h.setHeader("Referrer-Policy", "same-origin");
        h.setHeader("Content-Security-Policy", "default-src 'self'; style-src 'self' 'unsafe-inline'");
        c.doFilter(r, s);
    }
}
