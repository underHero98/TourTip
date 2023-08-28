package com.example.projectT.api;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Component
public class KakaoAddrChangeApiUtil {
    public static ArrayList<Double> xyList = new ArrayList<>();
    private static String GEOCODE_URL="http://dapi.kakao.com/v2/local/search/address.json?query=";
    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("Res_keys");
    private static String GEOCODE_KEY= resourceBundle.getString("geocodeKey");
    public static ArrayList<Double> changeAddr(String addr){
        xyList.clear();
        URL obj;

        try{
            String address = URLEncoder.encode(addr, "UTF-8");

            obj = new URL(GEOCODE_URL+address);

            HttpURLConnection con = (HttpURLConnection)obj.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization","KakaoAK "+GEOCODE_KEY);
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
            JSONObject count = (JSONObject) jsonObject.get("meta");
            Long countNum = (Long) count.get("total_count");

            if(countNum == 0){
                xyList.add((double) 0);
                xyList.add((double) 0);
                System.out.println(addr+"의 정보가 조회 불가능 합니다.");
            }else{
                JSONArray documents = (JSONArray) jsonObject.get("documents");
                JSONObject addrType = (JSONObject) documents.get(0);
                JSONObject addrs = (JSONObject) addrType.get("address");
                String x = (String) addrs.get("x");
                String y = (String) addrs.get("y");
                double x1 = Double.parseDouble(x);
                double y1 = Double.parseDouble(y);
                xyList.add(y1);
                xyList.add(x1);
            }
            return xyList;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            xyList.add((double)0);
            xyList.add((double)0);
            return xyList;
        }

    }
}
