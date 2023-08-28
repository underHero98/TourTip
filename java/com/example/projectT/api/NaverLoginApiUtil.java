package com.example.projectT.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@Log4j2
@Component
public class NaverLoginApiUtil {
    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("Res_keys");
    private final static String CLIENTID = resourceBundle.getString("naverClientId"); // 애플리케이션 클라이언트 아이디
    private final static String CLIENT_SECRET = resourceBundle.getString("naverClientSecret"); //애플리케이션 클라이언트 시크릿
    private final static String REDIRECT_BASEURI = resourceBundle.getString("redirectBaseUri");

    Map<String, String> tokens;

    public Map<String, String> getTokens(HttpServletRequest request) throws UnsupportedEncodingException {
        String naverCode = request.getParameter("code");
        String naverState = request.getParameter("state");
        String redirectURI = URLEncoder.encode(REDIRECT_BASEURI+"/oauth/naver", "UTF-8");

        String apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&";
        apiURL += "client_id=" + CLIENTID;              //ClientID
        apiURL += "&client_secret=" + CLIENT_SECRET;    //ClientSecret
        apiURL += "&redirect_uri=" + redirectURI;       //콜백 URI
        apiURL += "&code=" + naverCode;                 //네이버 code
        apiURL += "&state=" + naverState;               //네이버 state
        log.info("apiURL=" + apiURL);

        HttpURLConnection con = null;
        String res = "";
        tokens = new HashMap<>();

        try {
            con = connect(apiURL);

            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                res = readBody(con.getInputStream());
            } else { // 에러 발생
                res = readBody(con.getErrorStream());
            }

            if (responseCode == 200) {
                Map<String, Object> parsedJson = new JSONParser(res).parseObject();
                log.info(parsedJson.toString());

                //토큰 발급
                String access_token = (String) parsedJson.get("access_token");
                String refresh_token = (String) parsedJson.get("refresh_token");

                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
        return tokens;
    }

    private static String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }

    private static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }
}
