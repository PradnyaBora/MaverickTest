package org.ocean.probe.demo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ocean.probe.demo.dto.Coordinates;
import org.ocean.probe.demo.requestbody.MoveParameter;
import org.ocean.probe.demo.service.MoveProbeService;
import org.ocean.probe.demo.model.CurrentCoordinate;
import org.ocean.probe.demo.model.ProbeVisitedHistory;
import org.ocean.probe.demo.repository.CurrentCoordinateRepo;
import org.ocean.probe.demo.repository.ProbeVisitedHistoryRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/probe")
public class ProbeController {

    private final CurrentCoordinateRepo currentCoordinateRepo;
    private final MoveProbeService moveProbeService;
    private final ProbeVisitedHistoryRepo probeVisitedHistoryRepo;

    @GetMapping("/current-position")
    public ResponseEntity<Coordinates> getCurrentPosition() {
        CurrentCoordinate currentCoordinate = currentCoordinateRepo.findById(1)
                .orElseThrow(() -> new RuntimeException("Probe position not found"));

        Coordinates coordinates = Coordinates.builder()
                .x(currentCoordinate.getX())
                .y(currentCoordinate.getY())
                .build();

        return ResponseEntity.ok(coordinates);
    }

    @PutMapping("/move")
    public ResponseEntity<Coordinates> moveProbe(@Valid @RequestBody MoveParameter moveParameter) {
        return ResponseEntity.ok(moveProbeService.moveProbe(moveParameter));
    }

    @GetMapping("/history")
    public ResponseEntity<List<ProbeVisitedHistory>> getProbeHistory() {
        return ResponseEntity.ok(probeVisitedHistoryRepo.findAll());
    }
}
