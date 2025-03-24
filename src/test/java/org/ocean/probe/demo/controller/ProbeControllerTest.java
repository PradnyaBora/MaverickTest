package org.ocean.probe.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.ocean.probe.demo.enums.Direction;
import org.ocean.probe.demo.requestbody.MoveParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class ProbeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    @DisplayName("Should verify current position")
    void verifyCurrentPosition() throws Exception {
        mockMvc.perform(get("/probe/current-position"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.x").value(0))
                .andExpect(jsonPath("$.y").value(0));
    }

    @Test
    @DisplayName("Move Forward for 4 steps from (0,0) to (0,4)")
    @Order(2)
    void verifyMoveProbeForwardBy4Steps() throws Exception {
        performMoveRequest("FORWARD", 4, 0, 4);
    }

    @Test
    @DisplayName("Move Right for 4 steps from (0,4) to (3,4) due to obstacle")
    @Order(3)
    void verifyMoveProbeRightBy4StepsTowardsObstacle() throws Exception {
        performMoveRequest("RIGHT", 4, 3, 4);
    }

    @Test
    @DisplayName("Move Forward by 8 steps from (3,4) to (3,9) due to border")
    @Order(4)
    void verifyMoveProbeForwardTowardsBorder() throws Exception {
        performMoveRequest("FORWARD", 8, 3, 9);
    }

    @Test
    @DisplayName("Move Left by 5 steps from (3,9) to (1,9)")
    @Order(5)
    void verifyMoveProbeLeftTowardsBorder() throws Exception {
        performMoveRequest("LEFT", 5, 1, 9);
    }

    @Test
    @DisplayName("Move Backward by 4 steps from (1,9) to (1,5)")
    @Order(6)
    void verifyMoveProbeBackwardBy4Steps() throws Exception {
        performMoveRequest("BACKWARD", 4, 1, 5);
    }

    @Order(7)
    @Test
    @DisplayName("Verify probe history")
    void verifyProbeHistory() throws Exception {
        mockMvc.perform(get("/probe/history"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(5))  // Check array size

                // First position (0,0)
                .andExpect(jsonPath("$[0].x").value(0))
                .andExpect(jsonPath("$[0].y").value(0))

                // Second position (3,4)  <-- Corrected value
                .andExpect(jsonPath("$[1].x").value(0))
                .andExpect(jsonPath("$[1].y").value(4))

                // Third position (3,9)
                .andExpect(jsonPath("$[2].x").value(3))
                .andExpect(jsonPath("$[2].y").value(4))

                // Fourth position (1,9)
                .andExpect(jsonPath("$[3].x").value(3))
                .andExpect(jsonPath("$[3].y").value(9))

                // Fifth position (1,5)
                .andExpect(jsonPath("$[4].x").value(1))
                .andExpect(jsonPath("$[4].y").value(9));
    }


    /**
     * Helper method to perform a move request and validate the expected response.
     */
    private void performMoveRequest(String direction, int step, int expectedX, int expectedY) throws Exception {
        MoveParameter moveParameter = new MoveParameter(Direction.valueOf(direction), step);
        String requestBody = objectMapper.writeValueAsString(moveParameter);

        mockMvc.perform(put("/probe/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.x").value(expectedX))
                .andExpect(jsonPath("$.y").value(expectedY));
    }
}
