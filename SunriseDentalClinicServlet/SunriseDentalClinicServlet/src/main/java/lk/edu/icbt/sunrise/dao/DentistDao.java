package lk.edu.icbt.sunrise.dao;

import lk.edu.icbt.sunrise.model.Dentist;

import java.util.List;

public interface DentistDao {
    List<Dentist> findActive();
}
