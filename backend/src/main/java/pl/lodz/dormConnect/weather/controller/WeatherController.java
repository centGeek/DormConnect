package pl.lodz.dormConnect.weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.dormConnect.weather.dto.WeatherResponse;
import pl.lodz.dormConnect.weather.service.WeatherService;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/temperature")
    public double getTemperature() {
        List<WeatherResponse> weatherResponses = weatherService.getCurrentWeather();

        if (weatherResponses != null && !weatherResponses.isEmpty()) {
            WeatherResponse currentLocation = weatherResponses.get(0);
            return currentLocation.current_weather().temperature();
        } else {
            throw new RuntimeException("No weather data available");
        }
    }
}
