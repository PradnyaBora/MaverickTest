package org.ocean.probe.demo.repository;

import org.ocean.probe.demo.model.ProbeVisitedHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProbeVisitedHistoryRepo extends JpaRepository<ProbeVisitedHistory, Integer> {
    List<ProbeVisitedHistory> findAll();
}
