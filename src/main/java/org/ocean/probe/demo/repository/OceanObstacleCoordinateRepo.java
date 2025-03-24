package org.ocean.probe.demo.repository;

import org.ocean.probe.demo.model.OceanObstacleCoordinate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OceanObstacleCoordinateRepo extends JpaRepository<OceanObstacleCoordinate, Integer> {
    Optional<OceanObstacleCoordinate> findById(Integer id);

    Optional<OceanObstacleCoordinate> findFirstByXGreaterThanAndYEqualsOrderByXAsc(Integer x, Integer y);

    Optional<OceanObstacleCoordinate> findFirstByXLessThanAndYEqualsOrderByXDesc(Integer x, Integer y);

    Optional<OceanObstacleCoordinate> findFirstByXEqualsAndYGreaterThanOrderByYAsc(Integer x, Integer y);

    Optional<OceanObstacleCoordinate> findFirstByXEqualsAndYLessThanOrderByYDesc(Integer x, Integer y);
}
