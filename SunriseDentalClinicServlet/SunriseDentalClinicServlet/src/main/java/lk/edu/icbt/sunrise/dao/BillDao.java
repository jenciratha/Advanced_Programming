package lk.edu.icbt.sunrise.dao;

import lk.edu.icbt.sunrise.model.Bill;
import java.sql.Connection;

import java.util.*;

public interface BillDao {
    Bill create(Connection c, Bill bill);
    Optional<Bill> findByAppointment(String no);
}
