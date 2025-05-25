package pl.lodz.dormConnect.dorm.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.lodz.dormConnect.config.PersistenceContainersTestConfiguration;
import pl.lodz.dormConnect.dorm.DormTestRooms;
import pl.lodz.dormConnect.dorm.services.RoomService;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(PersistenceContainersTestConfiguration.class)
@WithMockUser(username = "admin@edu.p.lodz.pl", authorities = "ADMIN")
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RoomService roomService;

    @Transactional
    @Rollback
    @Test
    void addAndRemoveRoomTest() throws Exception {
        String testJsonRequest = """
                {
                  "number": "TEST_NUMBER_999",
                  "capacity": 2,
                  "floor": 1,
                  "active": true,
                  "roomAssigns": [],
                  "groupedRooms": null
                }
                """;
        MvcResult result = mockMvc.perform(post("/api/dorm/room/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testJsonRequest)
                        .with(csrf()))
                .andExpect(status().isCreated()).andReturn();
        //It should be number of created Room
        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        long idOfCreatedRoom = jsonNode.get("id").asLong();

        MvcResult resultOfCreatedRoom = mockMvc.perform(get("/api/dorm/room/" + idOfCreatedRoom).accept(MediaType.APPLICATION_JSON).with(csrf())).andExpect(status().isOk()).andReturn();


        mockMvc.perform(delete("/api/dorm/room/" + idOfCreatedRoom)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())).andExpect(status().isOk());
    }

    @Test
    @Rollback
    @Transactional
    void getRoomsTest() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/api/dorm/room").with(csrf())
        ).andExpect(status().is2xxSuccessful()).andReturn();
        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(responseBody);
    }

    @Test
    @Rollback
    @Transactional
    void assignRoomTest() throws Exception {
        roomService.addRoom(DormTestRooms.testRoom1());
        MvcResult result = mockMvc.perform(
                get("/api/dorm/room")
        ).andExpect(status().is2xxSuccessful()).andReturn();
        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        long idOfCreatedRoom = jsonNode.get(0).get("id").asLong();

        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.putIfAbsent("roomEntity", jsonNode.get(0));

        objectNode.put("idOfStudent", 15L);
        objectNode.put("fromDate", LocalDate.of(2025, 1, 1).toString());
        objectNode.put("toDate", LocalDate.of(2025, 1, 15).toString());


        String uri = "/api/dorm/room/" + idOfCreatedRoom + "/assign" ;
        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectNode.toString())
                        .with(csrf()))
                .andExpect(status().isOk());

        //One student cannot have more than one reservation at the time
        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectNode.toString())
                        .with(csrf()))
                .andExpect(status().is5xxServerError());

        //Another student in the same period
        objectNode.put("idOfStudent", 16L);
        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectNode.toString())
                        .with(csrf()))
                .andExpect(status().isOk());

        //This should not succeed as there is no more space!
        objectNode.put("idOfStudent", 17L);
        mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectNode.toString())
                .with(csrf())).andExpect(status().is5xxServerError());

        //And now, we should be able to "rent" as the room is going to be empty at the time
        objectNode.put("idOfStudent", 17L);
        objectNode.put("fromDate", LocalDate.of(2025, 1, 16).toString());
        objectNode.put("toDate", LocalDate.of(2025, 1, 20).toString());
        mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectNode.toString())
                .with(csrf())).andExpect(status().isOk());
        //Lastly test the case when we do not specify endDate
        objectNode.put("fromDate", LocalDate.of(2025, 1, 25).toString());
        objectNode.putNull("toDate");
        mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectNode.toString())
                .with(csrf())).andExpect(status().isOk());
        //
        objectNode.put("fromDate", LocalDate.of(2025, 1, 30).toString());
        objectNode.put("toDate", LocalDate.of(2025, 2, 20).toString());
        mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectNode.toString())
                .with(csrf())).andExpect(status().is5xxServerError());


        objectNode.put("idOfStudent", 16L);
        mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectNode.toString())
                .with(csrf())).andExpect(status().isOk());
    }
}