package pl.lodz.dormConnect.weather.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.lodz.dormConnect.weather.dto.WeatherResponse;

import java.util.List;

@Service
public class WeatherService {

    private  RestTemplate restTemplate = new RestTemplate();

    public List<WeatherResponse> getCurrentWeather() {
        String url = String.format(
                "https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&current_weather=true",
                51.0, 19.0
        );
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<WeatherResponse>>() {}
        ).getBody();
    }
}
