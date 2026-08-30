package lk.edu.icbt.sunrise.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lk.edu.icbt.sunrise.config.ApplicationFactory;

import java.io.IOException;

@WebServlet("/appointments/view") public final class AppointmentViewServlet extends HttpServlet {
    protected void doGet(HttpServletRequest q, HttpServletResponse s) throws ServletException, IOException {
        var f = ApplicationFactory.get();
        var a = f.appointments.findByNumber(q.getParameter("no"));
        if (a.isEmpty()) {
            s.sendError(404);
            return;
        }
        q.setAttribute("appointment", a.get());
        q.setAttribute("bill", f.bills.findByAppointment(a.get().appointmentNumber()).orElse(null));
        q.getRequestDispatcher("/WEB-INF/views/appointment-view.jsp").forward(q, s);
    }
}
