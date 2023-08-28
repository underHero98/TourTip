package com.example.projectT.controller;

import com.example.projectT.dto.*;
import com.example.projectT.repository.HotelRepository;
import com.example.projectT.repository.ReviewRepository;
import com.example.projectT.service.FoodService;
import com.example.projectT.service.HotelService;
import com.example.projectT.service.ReviewService;
import com.example.projectT.service.ViewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/page/hotel")
public class HotelController {

    private final HotelRepository hotelRepository;
    private final HotelService hotelService;
    private final FoodService foodService;
    private final ViewService viewService;
    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;

    // 테스트 용
    @GetMapping("/hotelDetail")
    public String hotelDetail(){
        return "page/hotel/hotelDetail";
    }

    // 테스트 용
    @GetMapping("/hotelView")
    public String hotelView(){
        return "page/hotel/hotelView";
    }



    // 호텔 검색. 지역,체크인,체크아웃,수용인원 설정해서 검색.
    @GetMapping("/checkHotel")
    @ResponseBody
    public Map<String, Object> searchHotelList(@RequestParam String region,
                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkIn,
                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOut,
                                          @RequestParam int capacity) {

        Map<String, Object> hotelInfoMap = new HashMap<>();
        List<HotelDto> check = hotelService.check(region, checkIn, checkOut, capacity);
        List<String> hotelImgList = new ArrayList<>();

        check.forEach(h->hotelImgList.add(hotelService.findImg(h).get(0)));
        hotelInfoMap.put("hotelList", check);
        hotelInfoMap.put("hotelImgList", hotelImgList);

        log.info("hotelList = {}", check);
        log.info("hotelImgList = {}", hotelImgList);

        return hotelInfoMap;
    }

    // 테스트 용
    @RequestMapping("/getHotel")
    public String getHotel(Model model,
            @RequestParam Long hotelId) {

        model.addAttribute("hotelId",hotelId);
        return "hotelMap";
    }


    // 테스트 용
    @GetMapping("/hotels")
    @ResponseBody
    public HotelDto hotels(@RequestParam Long hotelId){

        HotelDto hotel = hotelService.entityToDto(hotelRepository.findById(hotelId).get());

        return hotel;
    }

    @GetMapping("hotelDetailInfo")
    @ResponseBody
    public Map<String, Object> hotelDetailInfo(@RequestParam Long hotelId, @RequestParam LocalDate checkInDate) {

        Map<String, Object> hotelDetailInfo = new HashMap<>();
        List<FoodDto> foodDtoList = new ArrayList<>();
        List<ViewDto> viewDtoList = new ArrayList<>();
        List<String> imgUrlListH = new ArrayList<>();
        List<String> imgUrlListV = new ArrayList<>();
        List<String> imgUrlListF = new ArrayList<>();
        List<ReviewDto> reviewDtoList = new ArrayList<>();
        List<String> reviewNickNameList = new ArrayList<>();
        List<Map<String, String>> weatherInfoList = new ArrayList<>();

        var hotelDto = hotelService.entityToDto(hotelRepository.findById(hotelId).get());
        double posX = hotelDto.getPosX();
        double posY = hotelDto.getPosY();
//            음식점, 관광지, 리뷰 리스트 받기
        foodService.findByXY(posX, posY).forEach(food->{foodDtoList.add(foodService.entityToDto(food));});
        viewService.findByXY(posX, posY).forEach(view->{viewDtoList.add(viewService.entityToDto(view));});
        reviewRepository.findTop6ByHotelId(hotelId).forEach(
                review->{
                    var reviewD = reviewService.entityToDto(review);
                    reviewDtoList.add(reviewD);
                    reviewNickNameList.add(reviewService.findNickName(reviewD));
                });
//            날씨 정보 3일
//            today 날씨는 호출되지 않으므로 tomorrow 날씨로 출력됨
//            날씨정보 출력 방식 -> {최저온도=4.3, 최고온도=21.1, 날씨=Clear Sky}
        for(int wl = 0; wl < 3; wl ++) {
            var info = Map.copyOf(hotelService.weatherInfo(hotelDto, checkInDate.plusDays(wl)));
            weatherInfoList.add(info);
        }
//            이미지 3개 첨부
//            3개가 없는 경우 고려
//            img src 부분에 첨부
        for(int hs = 0; hs < hotelService.findImg(hotelDto).size(); hs++){
            imgUrlListH.add(hotelService.findImg(hotelDto).get(hs));
        }
//           이미지 각각 1개 첨부
        for(int vs = 0; vs < Math.min(8, viewDtoList.size()); vs ++){
            imgUrlListV.add(viewService.findImg(viewDtoList.get(vs)).get(0));
        }
        for(int fs = 0; fs < Math.min(8, foodDtoList.size()); fs ++){
            imgUrlListF.add(foodService.findImg(foodDtoList.get(fs)).get(0));
        }

        hotelDetailInfo.put("hotel", hotelDto);
        hotelDetailInfo.put("foodList", foodDtoList);
        hotelDetailInfo.put("viewList", viewDtoList);
        hotelDetailInfo.put("reviewList", reviewDtoList);
        hotelDetailInfo.put("weatherInfoList", weatherInfoList);
        hotelDetailInfo.put("imgUrlListH", imgUrlListH);
        hotelDetailInfo.put("imgUrlListF", imgUrlListF);
        hotelDetailInfo.put("imgUrlListV", imgUrlListV);
        hotelDetailInfo.put("reviewNickNameList", reviewNickNameList);

        return hotelDetailInfo;
    }

    // 리뷰 작성
    @PostMapping("/postReview")
    public String postReview(ReviewDto reviewDto,
                             RedirectAttributes reAttr,
                             @RequestParam LocalDate checkInDate,
                             @SessionAttribute(name = "userInfo", required = false) UserDto userDto) {

        // 로그인 미적용, 수정 필요
        if (userDto == null) {
            reviewDto.setUserId(1L);
        } else {
            reviewDto.setUserId(userDto.getId());
        }

        reviewService.registerReview(reviewDto);

        reAttr.addAttribute("hotelId", reviewDto.getHotelId());
        reAttr.addAttribute("checkInDate", checkInDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return "redirect:/page/hotel/hotelDetail";
    }
}
