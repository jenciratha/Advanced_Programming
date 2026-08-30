package lk.edu.icbt.sunrise.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lk.edu.icbt.sunrise.config.ApplicationFactory;

import java.io.IOException;

@WebServlet("/login") public final class LoginServlet extends HttpServlet {
    protected void doGet(HttpServletRequest q, HttpServletResponse s) throws ServletException, IOException {
        q.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(q, s);
    }

    protected void doPost(HttpServletRequest q, HttpServletResponse s) throws ServletException, IOException {
        ApplicationFactory.get().auth.authenticate(q.getParameter("username"), q.getParameter("password")).ifPresentOrElse(u -> {
            try {
                q.getSession().invalidate(); HttpSession n = q.getSession(true); n.setAttribute("user", u); n.setMaxInactiveInterval(1800); s.sendRedirect(q.getContextPath() + "/dashboard");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, () -> {
            try {
                q.setAttribute("error", "Invalid credentials or locked account"); doGet(q, s);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
