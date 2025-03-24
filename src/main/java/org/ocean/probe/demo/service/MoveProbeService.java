package org.ocean.probe.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ocean.probe.demo.dto.Coordinates;
import org.ocean.probe.demo.exception.ProbeException;
import org.ocean.probe.demo.requestbody.MoveParameter;
import org.ocean.probe.demo.model.CurrentCoordinate;
import org.ocean.probe.demo.model.OceanObstacleCoordinate;
import org.ocean.probe.demo.repository.CurrentCoordinateRepo;
import org.ocean.probe.demo.repository.OceanObstacleCoordinateRepo;
import org.ocean.probe.demo.repository.ProbeVisitedHistoryRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MoveProbeService {

    private final CurrentCoordinateRepo currentCoordinateRepo;
    private final OceanObstacleCoordinateRepo oceanObstacleCoordinateRepo;
    private final ProbeVisitedHistoryRepo probeVisitedHistoryRepo;

    private static final int MAX_PROBE_POSITION = 9;
    private static final int MIN_PROBE_POSITION = 1;
    private static final int CURRENT_COORDINATE_ID = 1;

    public Coordinates moveProbe(MoveParameter moveParam) {
        CurrentCoordinate currentCoord = currentCoordinateRepo.findById(CURRENT_COORDINATE_ID)
                .orElseThrow(() -> new ProbeException("Current probe position not found"));

        Coordinates newCoordinates = switch (moveParam.getDirection()) {
            case FORWARD -> moveVertically(currentCoord, moveParam.getStep(), true);
            case BACKWARD -> moveVertically(currentCoord, moveParam.getStep(), false);
            case RIGHT -> moveHorizontally(currentCoord, moveParam.getStep(), true);
            case LEFT -> moveHorizontally(currentCoord, moveParam.getStep(), false);
        };

        // Save the updated coordinates
        currentCoordinateRepo.save(
                CurrentCoordinate.builder()
                        .id(CURRENT_COORDINATE_ID)
                        .x(newCoordinates.getX())
                        .y(newCoordinates.getY())
                        .build());

        log.info("Probe moved to new coordinates: {}", newCoordinates);
        return newCoordinates;
    }

    private Coordinates moveVertically(CurrentCoordinate currentCoord, int step, boolean isForward) {
        Optional<OceanObstacleCoordinate> obstacle = isForward
                ? oceanObstacleCoordinateRepo.findFirstByXEqualsAndYGreaterThanOrderByYAsc(currentCoord.getX(), currentCoord.getY())
                : oceanObstacleCoordinateRepo.findFirstByXEqualsAndYLessThanOrderByYDesc(currentCoord.getX(), currentCoord.getY());

        int newY = obstacle.map(o -> (isForward && currentCoord.getY() + step >= o.getY()) ? o.getY() - 1
                        : (!isForward && currentCoord.getY() - step <= o.getY()) ? o.getY() + 1
                        : (isForward ? Math.min(currentCoord.getY() + step, MAX_PROBE_POSITION) : Math.max(currentCoord.getY() - step, MIN_PROBE_POSITION)))
                .orElseGet(() -> isForward ? Math.min(currentCoord.getY() + step, MAX_PROBE_POSITION) : Math.max(currentCoord.getY() - step, MIN_PROBE_POSITION));

        return Coordinates.builder()
                .x(currentCoord.getX())
                .y(newY)
                .build();
    }

    private Coordinates moveHorizontally(CurrentCoordinate currentCoord, int step, boolean isRight) {
        Optional<OceanObstacleCoordinate> obstacle = isRight
                ? oceanObstacleCoordinateRepo.findFirstByXGreaterThanAndYEqualsOrderByXAsc(currentCoord.getX(), currentCoord.getY())
                : oceanObstacleCoordinateRepo.findFirstByXLessThanAndYEqualsOrderByXDesc(currentCoord.getX(), currentCoord.getY());

        int newX = obstacle.map(o -> (isRight && currentCoord.getX() + step >= o.getX()) ? o.getX() - 1
                        : (!isRight && currentCoord.getX() - step <= o.getX()) ? o.getX() + 1
                        : (isRight ? Math.min(currentCoord.getX() + step, MAX_PROBE_POSITION) : Math.max(currentCoord.getX() - step, MIN_PROBE_POSITION)))
                .orElseGet(() -> isRight ? Math.min(currentCoord.getX() + step, MAX_PROBE_POSITION) : Math.max(currentCoord.getX() - step, MIN_PROBE_POSITION));

        return Coordinates.builder().x(newX).y(currentCoord.getY()).build();
    }
}