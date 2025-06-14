package pl.lodz.security.weather.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.lodz.weather.controller.WeatherController;
import pl.lodz.weather.dto.CurrentWeather;
import pl.lodz.weather.dto.WeatherResponse;
import pl.lodz.weather.service.WeatherService;


import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = WeatherController.class)
@AutoConfigureMockMvc
@WithMockUser(username = "admin@edu.p.lodz.pl", authorities = "ADMIN")
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WeatherService weatherService;

    @Test
    void shouldReturnTemperatureFromWeatherService() throws Exception {
        // given
        WeatherResponse mockResponse = new WeatherResponse(
                51.0, 19.0, 123.0, "Europe/Warsaw",
                new CurrentWeather("2024-06-11T12:00", 23.5, 10.0, 5.0, 1)
        );
        when(weatherService.getCurrentWeather()).thenReturn(List.of(mockResponse));
        // when, then
        mockMvc.perform(get("/api/weather/temperature"))
                .andExpect(status().isOk())
                .andExpect(content().string("23.5"));
    }
    @Test
    void shouldReturnErrorWhenNoWeatherData() throws Exception {
        when(weatherService.getCurrentWeather()).thenReturn(List.of());

        mockMvc.perform(get("/api/weather/temperature"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("No weather data available"));
    }
}