package com.example.projectT.api.dbreg;

import lombok.SneakyThrows;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class WeatherRestApiUtil {
//    implementation group: 'com.squareup.okhttp', name: 'okhttp', version: '2.7.5' <-- build.gradle에 추가
    private static final Map<String, String> weatherList = new HashMap<>();
    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("Res_keys");
    private final static String WEATHER_API = resourceBundle.getString("weatherKey");
    @SneakyThrows
    public static Map<String, String> weather(double x, double y, LocalDate z){

        weatherList.clear();
        List<Object> temp = new ArrayList<>();
        String weatherInfo;
        String weatherIcon;
        String iconUrl;

        try {
            String latitude = Double.toString(x);
            String longitude = Double.toString(y);

            String url = "https://api.weatherbit.io/v2.0/forecast/daily?lat=" + latitude + "&lon=" + longitude + "&key=" + WEATHER_API;

            URLConnection connection = new URL(url).openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            reader.close();

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(stringBuilder.toString());
            JSONArray data = (JSONArray) json.get("data");

            String dateStr = z.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            for (Object datum : data) {
                JSONObject forecast = (JSONObject) datum;
                String date = (String) forecast.get("valid_date");
                if (date.equals(dateStr)) {
                    temp.add(forecast.get("max_temp"));
                    temp.add(forecast.get("min_temp"));
                    JSONObject weatherChk = (JSONObject) forecast.get("weather");
                    weatherInfo = (String) weatherChk.get("description");
                    weatherIcon = (String) weatherChk.get("icon");
                    iconUrl = "https://www.weatherbit.io/static/img/icons/" + weatherIcon + ".png";
                    weatherList.put("min", String.valueOf(temp.get(1)));
                    weatherList.put("max", String.valueOf(temp.get(0)));
                    weatherList.put("weather", weatherInfo);
                    weatherList.put("icon", iconUrl);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return weatherList;
    }
}
