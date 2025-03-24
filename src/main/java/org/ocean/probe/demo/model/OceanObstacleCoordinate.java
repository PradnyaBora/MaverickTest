package org.ocean.probe.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ocean_obstacle_coordinates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OceanObstacleCoordinate {

    @Id
    Integer id;
    Integer x;
    Integer y;

}
