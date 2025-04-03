package pl.lodz.dormConnect.dorm.controllers;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "user",roles = "USER",password = "password")
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addRoomTest() throws Exception {
    String testJsonRequest = """
            {
              "number": "A999",
              "capacity": 2,
              "floor": 1,
              "active": true
            }
            """;
    mockMvc.perform(post("/dorm/room")
            .contentType(MediaType.APPLICATION_JSON)
            .content(testJsonRequest)
                    .with(csrf()))
            .andExpect(status().isCreated());

    mockMvc.perform(delete("/dorm/room")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Long.toString(4))
            .with(csrf())).andExpect(status().isOk());
    }

    @Test
    @Transactional
    void getRoomsTest() throws Exception {
        mockMvc.perform(
                get("/dorm/room")
        ).andExpect(status().is2xxSuccessful());
    }
}