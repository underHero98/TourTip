package com.example.projectT.api.dbreg;

import com.example.projectT.dto.HotelDto;
import com.example.projectT.repository.HotelRepository;
import com.example.projectT.service.HotelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class HotelRegApiUtil {
    @Autowired
    private HotelService hotelService;
    @Autowired
    private HotelRepository hotelRepository;
    public void initDaeguHotelData(){
        URI uri = UriComponentsBuilder
                .fromUriString("https://thegoodnight.daegu.go.kr/ajax/api/thegoodnight.html")
                .queryParam("mode", "json")
                .queryParam("area", "중구")
                .queryParam("item_count", 100)
                .encode().build().toUri();

        var returnData = new RestTemplate().exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                }
        ).getBody();
        ArrayList<Object> dataList = (ArrayList<Object>) returnData.get("data");

        dataList.forEach(e -> {
            Map<String, String> dataMap = (Map<String, String>) e;
            Map<String, Object> entityMap = new HashMap<>();
            String key;
            String value;

            for(Map.Entry<String, String> entry : dataMap.entrySet()) {
                switch(entry.getKey()) {
                    case "address": key = "addr"; break;
                    case "shop": key = "name"; break;
                    case "tel": key = "tel"; break;
                    case "offer": key = "service"; break;
                    case "facilities": key = "facilities"; break;
                    case "tag": key = "tag"; break;
                    default: continue;
                }
                value = entry.getValue();

                if (value.length() >= 255) {
                    entityMap.put(key, value.substring(0, 254));
                }else {
                    entityMap.put(key, value);
                }
            }

            HotelDto hotelDto = new ObjectMapper().convertValue(entityMap, HotelDto.class);
            hotelService.register(hotelDto);
        });
    }
}
