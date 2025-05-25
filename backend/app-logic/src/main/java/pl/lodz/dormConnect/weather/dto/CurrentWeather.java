package pl.lodz.dormConnect.weather.dto;

public record CurrentWeather(
        String time,
        double temperature,
        double windspeed,
        double winddirection,
        int is_day
) {}
