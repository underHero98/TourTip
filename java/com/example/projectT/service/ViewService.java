package com.example.projectT.service;

import com.example.projectT.api.KakaoAddrChangeApiUtil;
import com.example.projectT.api.dbreg.HotelImgRestApiUtil;
import com.example.projectT.domain.entity.View;
import com.example.projectT.dto.ViewDto;
import com.example.projectT.repository.ViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ViewService {
    private final ViewRepository viewRepository;
    private final KakaoAddrChangeApiUtil kakaoAddrChangeApiUtil;
    private final HotelImgRestApiUtil hotelImgRestApiUtil;

    public void register(ViewDto viewDto) {

        if (viewRepository.findByName(viewDto.getName()).isEmpty()){
            if (viewDto.getPosX() == 0 || viewDto.getPosY() == 0) {
                ArrayList<Double> xy = kakaoAddrChangeApiUtil.changeAddr(viewDto.getAddr());
                viewDto.setPosX(xy.get(0));
                viewDto.setPosY(xy.get(1));
            }
            entityToDto(viewRepository.save(dtoToEntity(viewDto)));
        } else {
            System.out.println("ERROR");
        }

         var viewDelete = viewRepository.findByPosXBetweenAndPosYBetween(0,0.01,0,0.01);
        if(!viewDelete.isEmpty()){
            viewDelete.forEach(e->{
                viewRepository.delete(e);
            });
        }
    }

    public View dtoToEntity(ViewDto viewDto) {
        var entity = View.builder()
                .addr(viewDto.getAddr())
                .theme(viewDto.getTheme())
                .name(viewDto.getName())
                .tel(viewDto.getTel())
                .pk(viewDto.getPk())
                .reserv(viewDto.isReserv())
                .open(viewDto.getOpen())
                .tag(viewDto.getTag())
                .facilities(viewDto.getFacilities())
                .note(viewDto.getNote())
                .posX(viewDto.getPosX())
                .posY(viewDto.getPosY())
                .build();
        return entity;
    }

    public ViewDto entityToDto(View view) {
        var dto = ViewDto.builder()
                .id(view.getId())
                .addr(view.getAddr())
                .theme(view.getTheme())
                .name(view.getName())
                .tel(view.getTel())
                .pk(view.getPk())
                .reserv(view.isReserv())
                .open(view.getOpen())
                .tag(view.getTag())
                .facilities(view.getFacilities())
                .note(view.getNote())
                .posX(view.getPosX())
                .posY(view.getPosY())
                .build();
        return dto;
    }

    public List<View> findByXY(double x, double y){
        List<View> viewList = viewRepository
                .findByPosXBetweenAndPosYBetween(x-0.1,x+0.1,y-0.1,y+0.1);
        return viewList;
    }
    public List<String> findImg(ViewDto viewDto){
        return hotelImgRestApiUtil.searchImg(viewDto.getName());
    }
}
