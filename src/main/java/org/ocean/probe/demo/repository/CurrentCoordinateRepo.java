package org.ocean.probe.demo.repository;

import org.ocean.probe.demo.model.CurrentCoordinate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrentCoordinateRepo extends JpaRepository<CurrentCoordinate, Integer> {
    Optional<CurrentCoordinate> findById(Integer Id);
}
