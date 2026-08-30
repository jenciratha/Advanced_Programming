package lk.edu.icbt.sunrise.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/help") public final class HelpServlet extends HttpServlet {
    protected void doGet(HttpServletRequest q, HttpServletResponse s) throws ServletException, IOException {
        q.getRequestDispatcher("/WEB-INF/views/help.jsp").forward(q, s);
    }
}
