package com.example.projectT.service;

import com.example.projectT.repository.HotelRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HotelServiceTest {

    @Autowired HotelService hotelService;
    @Autowired HotelRepository hotelRepository;

    @Test
    public void weatherInfoTest() {

        var hotelDto = hotelService.entityToDto(hotelRepository.findById(1L).get());
        var info = hotelService.weatherInfo(hotelDto, LocalDate.of(2023, 4, 7));

        System.out.println(info);
    }
}