package com.example.projectT.api.dbreg;

import com.example.projectT.api.NaverSearchApiUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HotelImgRestApiUtil {
    List<String> imgSrcs = new ArrayList<>();
    public List<String> searchImg(String hotelName){
        imgSrcs.clear();
        String img = null;
        try {
            NaverSearchApiUtil searchUtil = new NaverSearchApiUtil();
            String response = searchUtil.search(hotelName);
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(response.toString());

            Long cnt = null;
            if(jsonObject.get("total") != null) cnt = (Long) jsonObject.get("total");

            if(cnt == null || cnt == 0){
                imgSrcs.add(null);
            } else {
                JSONArray items = (JSONArray) jsonObject.get("items");
                if(items.size() == 0){
                    imgSrcs.add(null);
                } else if (items.size()>0 && items.size() < 3){
                    for(int i = 0; i < items.size(); i ++){
                        JSONObject link = (JSONObject) items.get(i);
                        img = (String) link.get("link");
                        imgSrcs.add(img);
                    }
                } else {
                    for(int i = 0; i < 3; i ++){
                        JSONObject link = (JSONObject) items.get(i);
                        img = (String) link.get("link");
                        imgSrcs.add(img);
                    }
                }
                return imgSrcs;
            }
        } catch(Exception e){
            e.printStackTrace();
            imgSrcs.add(null);
            return imgSrcs;
        }
        return imgSrcs;
    }
}
