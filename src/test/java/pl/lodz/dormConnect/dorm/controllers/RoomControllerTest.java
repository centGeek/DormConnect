package pl.lodz.dormConnect.dorm.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin",roles = "ADMIN")
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addRoomTest() throws Exception {
    String testJsonRequest = """
            {
              "number": "A101",
              "capacity": 2,
              "floor": 1,
              "active": true
            }
            """;
    mockMvc.perform(post("/dorm/room")
            .contentType(MediaType.APPLICATION_JSON)
            .content(testJsonRequest))
            .andExpect(status().isCreated());

    }

    @Test
    void getRoomsTest() throws Exception {
        mockMvc.perform(
                get("/dorm/room/")
        ).andExpect(status().is2xxSuccessful());
    }
}