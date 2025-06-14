package pl.lodz.dormitoryservice.floors.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.lodz.dormitoryservice.entity.FloorEntity;
import pl.lodz.dormitoryservice.fixtures.FloorFixture;
import pl.lodz.dormitoryservice.floors.service.FloorsService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = FloorsController.class)
@AutoConfigureMockMvc
@WithMockUser(username = "admin@edu.p.lodz.pl", authorities = "ADMIN")
class FloorsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FloorsService floorsService;

    @Test
    void thatGetFloorsWorksCorrectly() throws Exception {
        // given
        List<Integer> floors = List.of(1, 2);

        when(floorsService.getFloors()).thenReturn(floors);
        // when, then
        mockMvc.perform(get("/api/floors/get"))
                .andExpect(status().isOk());
    }

//    @Test
//    void thatAddFloorWorksCorrectly() throws Exception {
//        //given
//        FloorEntity expectedFloor = FloorFixture.anyFloorEntity();
//        when(floorsService.addFloor()).thenReturn(expectedFloor);
//
//        // when, then
//        mockMvc.perform(post("/api/floors/add"))
//                .andExpect(status().isCreated());
//
//        verify(floorsService, times(1)).addFloor();
//    }


}