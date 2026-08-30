package lk.edu.icbt.sunrise.listener;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebListener;
import lk.edu.icbt.sunrise.config.*;
import lk.edu.icbt.sunrise.model.*;

import org.mindrot.jbcrypt.BCrypt;

@WebListener public final class ApplicationListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent e) {
        ApplicationFactory f = ApplicationFactory.get();
        if (f.users.count() == 0) {
            f.users.create(new User(0, "admin", BCrypt.hashpw("Admin@123", BCrypt.gensalt(12)), "System Administrator", Role.ADMIN, true, 0));
            f.users.create(new User(0, "reception", BCrypt.hashpw("Reception@123", BCrypt.gensalt(12)), "Reception Staff", Role.RECEPTIONIST, true, 0));
        }
        e.getServletContext().setAttribute("appName", "Sunrise Dental Clinic");
    }

    public void contextDestroyed(ServletContextEvent e) {
        DatabaseConfig.close();
    }
}
