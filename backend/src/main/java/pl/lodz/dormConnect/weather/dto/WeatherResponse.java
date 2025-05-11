package pl.lodz.dormConnect.weather.dto;


public record WeatherResponse(
        double latitude,
        double longitude,
        double elevation,
        String timezone,
        CurrentWeather current_weather
) {}
