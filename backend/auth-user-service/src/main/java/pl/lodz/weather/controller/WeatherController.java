package pl.lodz.weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.weather.dto.WeatherResponse;
import pl.lodz.weather.service.WeatherService;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/temperature")
    public ResponseEntity<String> getTemperature() {
        List<WeatherResponse> weatherResponses = weatherService.getCurrentWeather();

        if (weatherResponses != null && !weatherResponses.isEmpty()) {
            WeatherResponse currentLocation = weatherResponses.getFirst();
            double temperature = currentLocation.current_weather().temperature();
            return ResponseEntity.ok(String.valueOf(temperature));
        } else {
            return ResponseEntity
                    .status(500)
                    .body("No weather data available");
        }
    }
}
