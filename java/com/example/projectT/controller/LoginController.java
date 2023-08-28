package com.example.projectT.controller;

import com.example.projectT.api.KakaoLoginApiUtil;
import com.example.projectT.api.KakaoProfileApiUtil;
import com.example.projectT.api.NaverLoginApiUtil;
import com.example.projectT.api.NaverProfileApiUtil;
import com.example.projectT.domain.entity.User;
import com.example.projectT.dto.UserDto;
import com.example.projectT.repository.UserRepository;
import com.example.projectT.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Map;
import java.util.ResourceBundle;

@Slf4j
@RequiredArgsConstructor
@Controller
public class LoginController {

    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("Res_keys");
    private final static String NAVER_CLIENT_ID = resourceBundle.getString("naverClientId"); // naver ClientId
    private final static String KAKAO_CLIENT_ID = resourceBundle.getString("kakaoClientId"); // kakao ClientId
    private final static String REDIRECT_BASEURI = resourceBundle.getString("redirectBaseUri");

    //네이버 api
    private final NaverLoginApiUtil naverLoginApiUtil;
    private final NaverProfileApiUtil naverProfileApiUtil;

    //카카오 api
    private final KakaoLoginApiUtil kakaoLoginApiUtil;
    private final KakaoProfileApiUtil kakaoProfileApiUtil;

    private final UserService userService;
    private final UserRepository userRepository;


    // 메인 목록
    @RequestMapping({"/", "main.html"})
    public String main() {
        return "/main";

    }

    // 메인 목록
    @GetMapping("/logout")
    public String logout(HttpSession session) { // redirect
        log.info("logout");
        session.invalidate();
        return "redirect:/login.html";
    }

    /* Login */
    @RequestMapping("/login.html")
    public String login(Model model, HttpSession session) throws UnsupportedEncodingException {

        log.info("login page");
        // [ 네이버 로그인
        // 네이버 콜백 URI 생성
        String naverCallBackUri = URLEncoder.encode(REDIRECT_BASEURI + "/oauth/naver", "utf-8");
        // state 랜덤생성
        SecureRandom naverRandom = new SecureRandom();
        String naverState = new BigInteger(130, naverRandom).toString();
        // 네이버 api url 생성
        String naverApiURL = "https://nid.naver.com/oauth2.0/authorize?response_type=code";
        naverApiURL += String.format("&client_id=%s&redirect_uri=%s&state=%s", NAVER_CLIENT_ID, naverCallBackUri, naverState);

        // 클라이언트 전달
        session.setAttribute("naverState", naverState);
        model.addAttribute("naverLoginUrl", naverApiURL);
        // ] 네이버 로그인

        // [ 카카오 로그인
        // 카카오 콜백 URI 생성
        String kakaoCallBackUri = URLEncoder.encode(REDIRECT_BASEURI + "/oauth/kakao", "utf-8");
        // state 랜덤생성
        SecureRandom kakaoRandom = new SecureRandom();
        String kakaoState = new BigInteger(130, kakaoRandom).toString();
        // 카카오 api url 생성
        String kakaoApiURL = "https://kauth.kakao.com/oauth/authorize?response_type=code";
        kakaoApiURL += String.format("&client_id=%s&redirect_uri=%s&state=%s", KAKAO_CLIENT_ID, kakaoCallBackUri, kakaoState);

        // 클라이언트 전달
        session.setAttribute("kakaoState", naverState);
        model.addAttribute("kakaoLoginUrl", kakaoApiURL);
        // 카카오 로그인

        return "page/login/login";
    }

    @RequestMapping("/oauth/naver")
    public String naverOauth(HttpSession session, HttpServletRequest request)
            throws UnsupportedEncodingException, ParseException {

        //네이버 토큰 발급
        Map<String, String> res = naverLoginApiUtil.getTokens(request);
        String access_token = res.get("access_token");
        String refresh_token = res.get("refresh_token");

        session.setAttribute("naverCurrentAT", access_token);
        session.setAttribute("naverCurrentRT", refresh_token);

        /* access token을 사용해 사용자 프로필 조회 api 호출 */
        Map<String, String> profile = naverProfileApiUtil.getProfile(access_token); // Map으로 사용자 데이터 받기
        log.info("profile : " + profile);

        //회원가입 조회
        String pw = profile.get("id");
        String email = profile.get("email");

        return apiUserLogin(email, pw, session);
    }

    @RequestMapping("/oauth/kakao")
    public String kakaoOauth(HttpSession session, HttpServletRequest request)
            throws UnsupportedEncodingException, ParseException {

        //카카오 토큰 발급
        Map<String, String> res = kakaoLoginApiUtil.getTokens(request);
        String access_token = res.get("access_token");
        String refresh_token = res.get("refresh_token");

        session.setAttribute("kakaoCurrentAT", access_token);
        session.setAttribute("kakaoCurrentRT", refresh_token);

        /* access token을 사용해 사용자 프로필 조회 api 호출 */
        Map<String, String> profile = kakaoProfileApiUtil.getProfile(access_token); // Map으로 사용자 데이터 받기
        log.info("profile : " + profile);

        //회원가입 조회
        String identifier = profile.get("id");
        String email = profile.get("email");

        return apiUserLogin(email, identifier, session);
    }

    //API 로그인시 닉네임 설정
    @PostMapping("setUserNickNameLogin")
    public String setUserNickName(
            @RequestParam String nickName,
            @SessionAttribute(name = "userInfo", required = false) UserDto userDto) {

        userDto.setNickName(nickName);
        userService.update(userDto);

        return "redirect:/";
    }

    @GetMapping("/ezlogin")
    public String ezlogin() {
        return "/page/login/ezlogin";
    }

    /*
     * 회원가입 시작.
     * */

    @GetMapping("/register")
    public String register() {
        return "/page/login/register";
    }

    @ResponseBody
    @GetMapping("/userInfoCheck")
    public boolean userInfoCheck(@RequestParam String name, @RequestParam String value) {
        return userService.isExistsUserInfo(name, value);
    }

    @PostMapping("/save")
    public String save(UserDto userDto, HttpSession session) {

        session.setAttribute("userInfo", userService.register(userDto));

        return "redirect:/";
    }

    @PostMapping("/login/loginTest")
    public String loginTest(UserDto userDto, HttpSession session) {
        var user = userRepository.findByEmailAndPw(userDto.getEmail(), userDto.getPw());
        log.info("[LoginController] loginTest() >> user = {}", user);
        if (user.isEmpty()) {
            return "page/login/login";
        } else {
            user.ifPresent(e -> {

                log.info("login success");
            });

            session.setAttribute("userInfo", userService.entityToDto(user.get()));
            return "redirect:/";
        }
    }

    //카카오, 네이버 API로 로그인 하는 경우
    public String apiUserLogin(String email, String pw, HttpSession session) {

        boolean isExistUser = userRepository.findByEmailAndPw(email, pw).isPresent();

        //신규 회원 등록
        if (isExistUser == false) {

            var user = User.builder()
                    .email(email)
                    .pw(pw)
                    .build();
            userRepository.save(user);
        }

        var userEntity = userRepository.findByEmailAndPw(email, pw).get();
        session.setAttribute("userInfo", userService.entityToDto(userEntity));

        //기존 회원 닉네임 유무 판별
        if (userEntity.getNickName() == null) {
            return "redirect:/ezlogin";
        } else {
            return "redirect:/main.html";
        }
    }
}