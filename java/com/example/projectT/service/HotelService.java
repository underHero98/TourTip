package com.example.projectT.service;

import com.example.projectT.api.KakaoAddrChangeApiUtil;
import com.example.projectT.api.dbreg.HotelImgRestApiUtil;
import com.example.projectT.api.dbreg.WeatherRestApiUtil;
import com.example.projectT.domain.entity.Hotel;
import com.example.projectT.dto.HotelDto;
import com.example.projectT.repository.HotelRepository;
import com.example.projectT.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class HotelService {
    private final HotelRepository hotelRepository;
    private final KakaoAddrChangeApiUtil kakaoAddrChangeApiUtil;
    private final HotelImgRestApiUtil hotelImgRestApiUtil;
    private final ReviewRepository reviewRepository;
    private final WeatherRestApiUtil weatherRestApiUtil;

    public void register(HotelDto hotelDto) {

        if (hotelRepository.findByName(hotelDto.getName()).isEmpty()) {
            ArrayList<Double> xy = kakaoAddrChangeApiUtil.changeAddr(hotelDto.getAddr());
            hotelDto.setPosX(xy.get(0));
            hotelDto.setPosY(xy.get(1));
            entityToDto(hotelRepository.save(dtoToEntity(hotelDto)));
        } else {
            System.out.println("ERROR");
        }
        if(!hotelRepository.findByPosXBetweenAndPosYBetween(0,0.01,0,0.01).isEmpty()){
            var foodDelete = hotelRepository.findByPosXBetweenAndPosYBetween(0,0.01,0,0.01);
            foodDelete.forEach(e->{
                hotelRepository.delete(e);
            });
        }
    }

    public Hotel dtoToEntity(HotelDto hotelDto) {
        var entity = Hotel.builder()
                .addr(hotelDto.getAddr())
                .theme(hotelDto.getTheme())
                .name(hotelDto.getName())
                .tel(hotelDto.getTel())
                .pk(hotelDto.getPk())
                .reserv(hotelDto.isReserv())
                .service(hotelDto.getService())
                .tag(hotelDto.getTag())
                .facilities(hotelDto.getFacilities())
                .posX(hotelDto.getPosX())
                .posY(hotelDto.getPosY())
                .checkIn(hotelDto.getCheckIn())
                .checkOut(hotelDto.getCheckOut())
                .starRating(hotelDto.getStarRating())
                .price(hotelDto.getPrice())
                .capacity(hotelDto.getCapacity())
                .region(hotelDto.getRegion())
                .build();
        return entity;
    }

    public HotelDto entityToDto(Hotel hotel) {
        var dto = HotelDto.builder()
                .id(hotel.getId())
                .addr(hotel.getAddr())
                .theme(hotel.getTheme())
                .name(hotel.getName())
                .tel(hotel.getTel())
                .pk(hotel.getPk())
                .reserv(hotel.isReserv())
                .service(hotel.getService())
                .tag(hotel.getTag())
                .facilities(hotel.getFacilities())
                .posX(hotel.getPosX())
                .posY(hotel.getPosY())
                .checkIn(hotel.getCheckIn())
                .checkOut(hotel.getCheckOut())
                .starRating(hotel.getStarRating())
                .price(hotel.getPrice())
                .capacity(hotel.getCapacity())
                .region(hotel.getRegion())
                .build();
        return dto;
    }

    public List<Hotel> findByXY(double x, double y) {
        List<Hotel> hotelList = hotelRepository
                .findByPosXBetweenAndPosYBetween(x-0.1, x+0.1, y-0.1, y+0.1);
        return hotelList;
    }

    //         검색서비스
    public List<HotelDto> check(String region, LocalDate checkIn, LocalDate checkOut, int capacity) {
        List<HotelDto> hotelDtoList = new ArrayList<>();
        hotelRepository.findHotel(region, checkIn, checkOut, capacity).forEach(e -> {
            hotelDtoList.add(entityToDto(e));
        });
        return hotelDtoList;
    }

    //weather Info
    public Map<String, String> weatherInfo(HotelDto hotelDto, LocalDate chkIn){
        return weatherRestApiUtil.weather(hotelDto.getPosX(),hotelDto.getPosY(),chkIn);
    }
    //hotel Img
    public List<String> findImg(HotelDto hotelDto){
        return hotelImgRestApiUtil.searchImg(hotelDto.getName());
    }
    //hotel starPoint
    public void setStarPoint(Long hotelId){
        var reviews = reviewRepository.findByHotelId(hotelId);
        final double[] sum = {0};
        reviews.forEach(e->{
            sum[0] += e.getReviewPoint();
        });
        sum[0] = sum[0] / reviews.size();
        double sums = Math.round(sum[0] * 100.0) / 100.0;
        Hotel hotel = hotelRepository.findById(hotelId).get();
        hotel.setStarRating(sums);
        hotelRepository.save(hotel);
    }
}
