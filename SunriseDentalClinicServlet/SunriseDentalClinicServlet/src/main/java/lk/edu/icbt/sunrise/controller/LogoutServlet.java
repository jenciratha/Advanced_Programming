package lk.edu.icbt.sunrise.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/logout") public final class LogoutServlet extends HttpServlet {
    protected void doPost(HttpServletRequest q, HttpServletResponse s) throws IOException {
        q.getSession().invalidate();
        s.sendRedirect(q.getContextPath() + "/login?logout=1");
    }
}
