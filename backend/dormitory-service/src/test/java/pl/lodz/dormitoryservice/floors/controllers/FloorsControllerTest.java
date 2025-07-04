package pl.lodz.dormitoryservice.floors.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.lodz.dormitoryservice.floors.service.FloorsService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = FloorsController.class)
@AutoConfigureMockMvc
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

}