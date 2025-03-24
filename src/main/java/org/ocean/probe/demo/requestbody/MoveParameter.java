package org.ocean.probe.demo.requestbody;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ocean.probe.demo.enums.Direction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MoveParameter {
    @NotNull(message = "Direction cannot be null")
    private Direction direction;

    @NotNull(message = "Step cannot be null")
    @Min(value = 1, message = "Step must be at least 1")
    private Integer step;
}
