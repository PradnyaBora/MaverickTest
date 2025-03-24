package org.ocean.probe.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "current_coordinate")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentCoordinate {
    @Id
    @Column(name="Id")
    Integer id;
    @Column(name="x_coordinate")
    Integer x;
    @Column(name="y_coordinate")
    Integer y;
}
