package lk.edu.icbt.sunrise.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lk.edu.icbt.sunrise.config.ApplicationFactory;
import lk.edu.icbt.sunrise.exception.*;
import java.io.IOException;

import java.time.*;

@WebServlet("/appointments/new") public final class AppointmentCreateServlet extends HttpServlet {
    protected void doGet(HttpServletRequest q, HttpServletResponse s) throws ServletException, IOException {
        var f = ApplicationFactory.get();
        q.setAttribute("dentists", f.dentists.findActive());
        q.setAttribute("treatments", f.treatments.findActive());
        q.getRequestDispatcher("/WEB-INF/views/appointment-form.jsp").forward(q, s);
    }

    protected void doPost(HttpServletRequest q, HttpServletResponse s) throws ServletException, IOException {
        try {
            var a = ApplicationFactory.get().appointmentService.register(q.getParameter("patientName"), q.getParameter("address"), q.getParameter("contact"), q.getParameter("email"), Long.parseLong(q.getParameter("dentistId")), Long.parseLong(q.getParameter("treatmentId")), LocalDate.parse(q.getParameter("date")), LocalTime.parse(q.getParameter("time")), q.getParameter("notes"));
            q.getSession().setAttribute("flash", "Appointment " + a.appointmentNumber() + " created successfully");
            s.sendRedirect(q.getContextPath() + "/appointments/view?no=" + a.appointmentNumber());
        } catch (ValidationException e) {
            q.setAttribute("errors", e.getErrors());
            doGet(q, s);
        } catch (Exception e) {
            q.setAttribute("error", e.getMessage());
            doGet(q, s);
        }
    }
}
