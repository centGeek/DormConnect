package pl.lodz.dormConnect.weather.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.lodz.dormConnect.DormConnectApplicationTests;
import pl.lodz.dormConnect.weather.dto.CurrentWeather;
import pl.lodz.dormConnect.weather.dto.WeatherResponse;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@SpringBootTest
@Import(DormConnectApplicationTests.class)
class WeatherServiceTest {

    @Test
    void testWeatherApiReturnsRelevantResult() throws Exception {
        WeatherService service = new WeatherService();

        Field field = WeatherService.class.getDeclaredField("restTemplate");
        field.setAccessible(true);
        RestTemplate mockRestTemplate = mock(RestTemplate.class);
        field.set(service, mockRestTemplate);

        List<WeatherResponse> mockList = List.of(new WeatherResponse(3.0, 5.0, 3.2, "Europe",
                new CurrentWeather("now", 5.0, 3.0, 2.0, 1)));

        when(mockRestTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(ResponseEntity.ok(mockList));

        List<WeatherResponse> result = service.getCurrentWeather();

        assertEquals(1, result.size());
    }

}
