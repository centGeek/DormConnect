package pl.lodz.dormproblemservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.lodz.dormproblemservice.ProblemStatus;
import pl.lodz.dormproblemservice.dto.CreateDormProblemDTO;
import pl.lodz.dormproblemservice.dto.GetDormProblemDTO;
import pl.lodz.dormproblemservice.service.DormProblemService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.lodz.dormproblemservice.fixtures.DormProblemFixtures.anyDormProblemDTO;

@WebMvcTest(DormProblemController.class)
class DormProblemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DormProblemService dormProblemService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String AUTH_HEADER = "Bearer testtoken";

    private GetDormProblemDTO exampleGetDto;
    private CreateDormProblemDTO exampleCreateDto;

    @BeforeEach
    void setUp() {
        exampleGetDto = anyDormProblemDTO();
    }


    @Test
    void testGetDormProblems() throws Exception {
        when(dormProblemService.getAllDormProblems(anyString())).thenReturn(List.of(exampleGetDto));

        mockMvc.perform(get("/api/dorm-problem/get")
                        .header("Authorization", AUTH_HEADER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(exampleGetDto.id()));
    }

    @Test
    void testGetDormProblemById() throws Exception {
        when(dormProblemService.getDormProblemById(1L)).thenReturn(exampleGetDto);

        mockMvc.perform(get("/api/dorm-problem/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(exampleGetDto.id()))
                .andExpect(jsonPath("$.description").value(exampleGetDto.description()));
    }
    @Test
    void testDeleteDormProblem() throws Exception {
        when(dormProblemService.getDormProblemById(1L)).thenReturn(exampleGetDto);
        doNothing().when(dormProblemService).deleteDormProblem(1L);

        mockMvc.perform(delete("/api/dorm-problem/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(exampleGetDto.id()))
                .andExpect(jsonPath("$.description").value(exampleGetDto.description()));
    }

    @Test
    void testGetProblemStatuses() throws Exception {
        List<ProblemStatus> statuses = List.of(ProblemStatus.IN_PROGRESS, ProblemStatus.REJECTED);
        when(dormProblemService.getAllProblemStatuses()).thenReturn(statuses);

        mockMvc.perform(get("/api/dorm-problem/problem-statuses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").value("IN_PROGRESS"))
                .andExpect(jsonPath("$[1]").value("REJECTED"));
    }

    @Test
    void testExtractTokeInvalidHeader() throws Exception {
        mockMvc.perform(post("/api/dorm-problem/create")
                        .header("Authorization", "InvalidToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exampleCreateDto)))
                .andExpect(status().isBadRequest());
    }
}
