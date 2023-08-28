package com.example.projectT.api.dbreg;

import com.example.projectT.dto.FoodDto;
import com.example.projectT.repository.FoodRepository;
import com.example.projectT.service.FoodService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class FoodRegApiUtil {
    @Autowired
    private FoodService foodService;
    @Autowired
    private FoodRepository foodRepository;
    private static String DAEGU = "https://www.daegufood.go.kr/kor/api/tasty.html";

    private final RestTemplate restTemplate = new RestTemplate();
    public void initDaeguFoodData(){

        URI uri = UriComponentsBuilder
                .fromUriString(DAEGU)
                .queryParam("mode", "json")
                .queryParam("addr", "중구")
                .encode().build().toUri();

        restTemplate.getInterceptors().add((request, body, execution) -> {
            ClientHttpResponse response = execution.execute(request,body);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return response;
        });

        final HttpHeaders headers = new HttpHeaders();
        final HttpEntity<?> entity = new HttpEntity<>(headers);
        var returnData = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                entity,
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
                    case "GNG_CS": key = "addr"; break;
                    case "FD_CS": key = "category"; break;
                    case "BZ_NM": key = "name"; break;
                    case "TLNO": key = "tel"; break;
                    case "PKPL": key = "pk"; break;
                    case "BKN_YN": key = "reserv"; break;
                    case "MNU": key = "menu"; break;
                    case "MBZ_HR": key = "open"; break;
                    case "SMPL_DESC": key = "note"; break;
                    default: continue;
                }

                value = entry.getValue();

                if (key == "reserv") {
                    entityMap.put(key, value.equals("가능"));
                } else {
                    if (value.length() >= 255)
                        entityMap.put(key, value.substring(0, 254));
                    else
                        entityMap.put(key, value);
                }
            }

            FoodDto foodDto = new ObjectMapper().convertValue(entityMap, FoodDto.class);
            foodService.register(foodDto);
        });
    }
}
