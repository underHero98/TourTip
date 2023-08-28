package com.example.projectT.api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ResourceBundle;

public class NaverSearchApiUtil {
    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("Res_keys");
    private final static String CLIENT_ID = resourceBundle.getString("naverClientIdS"); // 애플리케이션 클라이언트 아이디
    private final static String CLIENT_SECRET = resourceBundle.getString("naverClientSecretS"); //애플리케이션 클라이언트 시크릿
    private final String searchUrl = "https://openapi.naver.com/v1/search/image?query=";
    public String search(String hotelName) {

        HttpURLConnection con = null;
        String res = "";
        String text = null;
        try {
            text = URLEncoder.encode(hotelName,"UTF-8");
            URL url = new URL(searchUrl+text);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", CLIENT_ID);
            con.setRequestProperty("X-Naver-Client-Secret", CLIENT_SECRET);

            int responseCode = con.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                res = readyBody(con.getInputStream());
            } else {
                res = readyBody(con.getErrorStream());
            }

        } catch (Exception e) {
            System.out.println("ERROR : "+e);
        } finally {
            con.disconnect();
        }
        return res;
    }
    public String readyBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);
        try (BufferedReader bf = new BufferedReader(streamReader)){
            StringBuilder responseBody = new StringBuilder();
            String line;

            while ((line = bf.readLine()) != null){
                responseBody.append(line);
            }
            return responseBody.toString();
        } catch (Exception e){
            throw new RuntimeException("실패",e);
        }

    }
}
