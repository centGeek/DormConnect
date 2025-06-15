package pl.lodz.security.weather.controller.rest;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pl.lodz.security.config.PostgresContainerConfig;
import pl.lodz.weather.dto.CurrentWeather;
import pl.lodz.weather.dto.WeatherResponse;
import pl.lodz.weather.service.WeatherService;


import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WithMockUser(username = "admin@edu.p.lodz.pl", authorities = "ADMIN")
@Import(PostgresContainerConfig.class)
class WeatherControllerRestAssuredTest {

    @LocalServerPort
    private int port;

    @MockitoBean
    private WeatherService weatherService;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @Test
    void shouldReturnTemperature() {
        // given
        WeatherResponse response = new WeatherResponse(
                51.0, 19.0, 123.0, "Europe/Warsaw",
                new CurrentWeather("2024-06-11T12:00", 23.5, 10.0, 5.0, 1)
        );

        when(weatherService.getCurrentWeather()).thenReturn(List.of(response));

        given()
                .accept(MediaType.TEXT_PLAIN_VALUE)
                .when()
                .get("/api/weather/temperature")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("23.5"));
    }

    @Test
    void shouldReturnErrorWhenNoData() {
        //given
        when(weatherService.getCurrentWeather()).thenReturn(List.of());

        given()
                .accept(MediaType.TEXT_PLAIN_VALUE)
                .when()
                .get("/api/weather/temperature")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(equalTo("No weather data available"));
    }
}
