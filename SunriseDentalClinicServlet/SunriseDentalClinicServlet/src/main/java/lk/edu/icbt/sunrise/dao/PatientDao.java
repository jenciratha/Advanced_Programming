package lk.edu.icbt.sunrise.dao;

import lk.edu.icbt.sunrise.model.Patient;
import java.sql.Connection;

import java.util.*;

public interface PatientDao {
    Patient create(Connection c, Patient p);
    Optional<Patient> findById(long id);
    List<Patient> search(String query);
}
