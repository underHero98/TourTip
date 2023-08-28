package com.example.projectT.api.dbreg;

import com.example.projectT.dto.ViewDto;
import com.example.projectT.repository.ViewRepository;
import com.example.projectT.service.ViewService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

@Component
public class ViewRegApiUtil {
    @Autowired
    private ViewRepository viewRepository;
    @Autowired
    private ViewService viewService;

    private final RestTemplate restTemplate = new RestTemplate();

    private static String DAEGU = "https://apis.data.go.kr/B551011/KorService1/locationBasedList1";
    private static String SERVICE_KEY = "fMJBAShY3O5ixFOhLMVI4dKzaSvRl3I0mwlZzUhRAzWUF8%2BkU3tFweJqT1wZFIuHbEwVwdLPsiDvZQhAdqnSoA%3D%3D";

    public void initDaegeViewData(){

        ViewDto viewDto = ViewDto.builder().build();
        URL obj;

        try{
            obj = new URL(DAEGU+"?MobileOS=WIN&MobileApp=a&_type=json&mapX=128.55&mapY=35.86&radius=6000&serviceKey="+SERVICE_KEY+"&contentTypeId=12&numOfRows=100");

            HttpURLConnection con = (HttpURLConnection)obj.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("content-type", "application/json");
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setDefaultUseCaches(false);

            Charset charset = Charset.forName("UTF-8");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse(response.toString());
            JSONObject objectBody = (JSONObject) jsonObject.get("response");
            JSONObject itemHeader = (JSONObject) objectBody.get("body");
            JSONObject itemBody = (JSONObject) itemHeader.get("items");
            JSONArray items = (JSONArray) itemBody.get("item");

            for(int i = 0; i<items.size(); i++){
                JSONObject getItem = (JSONObject) items.get(i);
                String addr = (String) getItem.get("addr1");
                String name = (String) getItem.get("title");
                String x = (String) getItem.get("mapy");
                String y = (String) getItem.get("mapx");
                double x1 = Double.valueOf(x);
                double y1 = Double.valueOf(y);
                viewDto.setAddr(addr);
                viewDto.setName(name);
                viewDto.setPosX(x1);
                viewDto.setPosY(y1);
                viewService.register(viewDto);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

    }
}
