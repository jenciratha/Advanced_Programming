package lk.edu.icbt.sunrise.dao;

import lk.edu.icbt.sunrise.model.Treatment;

import java.util.*;

public interface TreatmentDao {
    List<Treatment> findActive();
    Optional<Treatment> findById(long id);
}
