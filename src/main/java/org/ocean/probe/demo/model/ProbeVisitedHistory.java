package org.ocean.probe.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "probe_visited_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProbeVisitedHistory {
    @Id
    Integer id;
    @Column(name="x_coordinate")
    Integer x;
    @Column(name="y_coordinate")
    Integer y;
}
