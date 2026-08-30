package lk.edu.icbt.sunrise.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lk.edu.icbt.sunrise.config.ApplicationFactory;

import java.io.IOException;

@WebServlet("/appointments") public final class AppointmentListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest q, HttpServletResponse s) throws ServletException, IOException {
        q.setAttribute("appointments", ApplicationFactory.get().appointments.search(q.getParameter("q"), q.getParameter("status"), q.getParameter("date")));
        q.getRequestDispatcher("/WEB-INF/views/appointments.jsp").forward(q, s);
    }
}
