package com.mysignals.api.repository;

import com.mysignals.api.entity.Sensor;
import com.mysignals.api.utils.SensorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
    @Query("SELECT s FROM Sensor s WHERE s.type = :type")
    List<Sensor> findByType(SensorType type);
}
