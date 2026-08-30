package lk.edu.icbt.sunrise.controller.api;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lk.edu.icbt.sunrise.config.ApplicationFactory;

import java.io.IOException;

@WebServlet("/api/v1/appointments/*") public final class AppointmentApiServlet extends HttpServlet {
    private final ObjectMapper json = new ObjectMapper().registerModule(new JavaTimeModule());
    protected void doGet(HttpServletRequest q, HttpServletResponse s) throws IOException {
        s.setContentType("application/json");
        String path = q.getPathInfo();
        if (path == null || path.length() < 2) {
            s.setStatus(400);
            json.writeValue(s.getWriter(), java.util.Map.of("error", "Appointment number required"));
            return;
        }
        var a = ApplicationFactory.get().appointments.findByNumber(path.substring(1));
        if (a.isEmpty()) {
            s.setStatus(404);
            json.writeValue(s.getWriter(), java.util.Map.of("error", "Not found"));
            return;
        }
        json.writeValue(s.getWriter(), a.get());
    }
}
