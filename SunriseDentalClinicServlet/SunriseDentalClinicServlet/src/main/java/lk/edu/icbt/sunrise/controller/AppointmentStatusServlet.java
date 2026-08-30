package lk.edu.icbt.sunrise.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lk.edu.icbt.sunrise.config.ApplicationFactory;
import lk.edu.icbt.sunrise.model.AppointmentStatus;

import java.io.IOException;

@WebServlet("/appointments/status") public final class AppointmentStatusServlet extends HttpServlet {
    protected void doPost(HttpServletRequest q, HttpServletResponse s) throws IOException {
        ApplicationFactory.get().appointments.updateStatus(q.getParameter("no"), AppointmentStatus.valueOf(q.getParameter("status")));
        s.sendRedirect(q.getContextPath() + "/appointments/view?no=" + q.getParameter("no"));
    }
}
