package com.example.projectT.service;

import com.example.projectT.api.KakaoAddrChangeApiUtil;
import com.example.projectT.api.dbreg.HotelImgRestApiUtil;
import com.example.projectT.domain.entity.Food;
import com.example.projectT.dto.FoodDto;
import com.example.projectT.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private KakaoAddrChangeApiUtil kakaoAddrChangeApiUtil;
    private final HotelImgRestApiUtil hotelImgRestApiUtil;
    public void register(FoodDto foodDto) {

        if (foodRepository.findByName(foodDto.getName()).isEmpty()){
            ArrayList<Double> xy= kakaoAddrChangeApiUtil.changeAddr(foodDto.getAddr());
            foodDto.setPosX(xy.get(0));
            foodDto.setPosY(xy.get(1));
            entityToDto(foodRepository.save(dtoToEntity(foodDto)));
        } else {
            System.out.println("ERROR");
        }
        if(!foodRepository.findByPosXBetweenAndPosYBetween(0,0.01,0,0.01).isEmpty()){
            var foodDelete = foodRepository.findByPosXBetweenAndPosYBetween(0,0.01,0,0.01);
            foodDelete.forEach(e->{
                foodRepository.delete(e);
            });
        }
    }

    public Food dtoToEntity(FoodDto foodDto) {
        var entity = Food.builder()
                .addr(foodDto.getAddr())
                .category(foodDto.getCategory())
                .name(foodDto.getName())
                .tel(foodDto.getTel())
                .pk(foodDto.getPk())
                .reserv(foodDto.isReserv())
                .open(foodDto.getOpen())
                .menu(foodDto.getMenu())
                .note(foodDto.getNote())
                .posX(foodDto.getPosX())
                .posY(foodDto.getPosY())
                .build();
        return entity;
    }

    public FoodDto entityToDto(Food food) {
        var dto = FoodDto.builder()
                .id(food.getId())
                .addr(food.getAddr())
                .category(food.getCategory())
                .name(food.getName())
                .tel(food.getTel())
                .pk(food.getPk())
                .reserv(food.isReserv())
                .open(food.getOpen())
                .menu(food.getMenu())
                .note(food.getNote())
                .posX(food.getPosX())
                .posY(food.getPosY())
                .build();
        return dto;
    }

    public List<Food> findByXY(double x, double y){
        List<Food> foodList = foodRepository
                .findByPosXBetweenAndPosYBetween(x-0.005,x+0.005,y-0.005,y+0.005);
        return foodList;
    }
    public List<String> findImg(FoodDto foodDto){
        return hotelImgRestApiUtil.searchImg(foodDto.getName());
    }
}
