package com.example.projectT.api.dbreg;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HotelRegApiUtilTest {

    @Autowired HotelRegApiUtil hotelRegApiUtil;

    @Test
    public void dataInit() {
        hotelRegApiUtil.initDaeguHotelData();
    }
}