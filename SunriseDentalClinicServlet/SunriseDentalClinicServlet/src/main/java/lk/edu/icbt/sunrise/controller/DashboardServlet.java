package lk.edu.icbt.sunrise.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lk.edu.icbt.sunrise.config.ApplicationFactory;
import java.io.IOException;

import java.time.LocalDate;

@WebServlet({
    "/", "/dashboard"
}) public final class DashboardServlet extends HttpServlet {
    protected void doGet(HttpServletRequest q, HttpServletResponse s) throws ServletException, IOException {
        var f = ApplicationFactory.get();
        q.setAttribute("metrics", f.appointments.dashboard());
        q.setAttribute("appointments", f.appointments.findByDate(LocalDate.now()));
        q.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(q, s);
    }
}
