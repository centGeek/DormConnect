//package pl.lodz.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import pl.lodz.model.Manager;
//import pl.lodz.model.Student;
//import pl.lodz.security.fixtures.ManagerFixture;
//import pl.lodz.security.fixtures.StudentFixture;
//import pl.lodz.service.ManagerRegisterService;
//import pl.lodz.service.StudentRegisterService;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(RegisterController.class)
//class RegisterControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private ManagerRegisterService managerRegisterService;
//
//    @MockitoBean
//    private StudentRegisterService studentRegisterService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    void shouldRegisterManagerSuccessfully() throws Exception {
//        Manager manager = ManagerFixture.anyManager();
//
//        mockMvc.perform(post("/register/manager")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(manager)))
//                .andExpect(status().isCreated());
//
//        Mockito.verify(managerRegisterService).registerManager(manager);
//    }
//
//    @Test
//    void shouldRegisterStudentSuccessfully() throws Exception {
//        Student student = StudentFixture.anyStudent();
//
//        mockMvc.perform(post("/register/student")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(student)))
//                .andExpect(status().isCreated());
//
//        Mockito.verify(studentRegisterService).registerStudent(student);
//    }
//
//    @Test
//    void shouldReturnBadRequestForInvalidManager() throws Exception {
//        Manager invalidManager = ManagerFixture.anyManager();
//
//        mockMvc.perform(post("/register/manager")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidManager)))
//                .andExpect(status().isBadRequest());
//
//        Mockito.verifyNoInteractions(managerRegisterService);
//    }
//
//    @Test
//    void shouldReturnBadRequestForInvalidStudent() throws Exception {
//        Student invalidStudent = StudentFixture.anyStudent();
//
//        mockMvc.perform(post("/register/student")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidStudent)))
//                .andExpect(status().isBadRequest());
//
//        Mockito.verifyNoInteractions(studentRegisterService);
//    }
//}
