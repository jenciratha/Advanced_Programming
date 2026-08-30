package lk.edu.icbt.sunrise.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lk.edu.icbt.sunrise.config.ApplicationFactory;
import java.io.IOException;

import java.math.BigDecimal;

@WebServlet("/billing/issue") public final class BillingServlet extends HttpServlet {
    protected void doPost(HttpServletRequest q, HttpServletResponse s) throws ServletException, IOException {
        try {
            ApplicationFactory.get().billingService.issue(q.getParameter("no"), new BigDecimal(q.getParameter("discount") == null || q.getParameter("discount").isBlank() ? "0" : q.getParameter("discount")));
            s.sendRedirect(q.getContextPath() + "/appointments/view?no=" + q.getParameter("no"));
        } catch (Exception e) {
            q.getSession().setAttribute("flash", e.getMessage());
            s.sendRedirect(q.getContextPath() + "/appointments/view?no=" + q.getParameter("no"));
        }
    }
}
