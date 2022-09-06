package com.example.member;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

//@SpringBootTest
class MemberApplicationTests {
SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:MM:ss");

    @Test
    void contextLoads() {
        long l = System.currentTimeMillis();
        System.out.println("l = " + l);
        //1661959172
        //1661959521395
        //1661959689005
        Date date = new Date();
        String format2 = simpleDateFormat.format(date);
        System.out.println("format2 = " + format2);
        String format1 = DateFormatUtils.format(date, "yyyy-MM-dd HH:MM:ss");
        System.out.println("format = " + format1);
        date.setTime(1661959172L*1000);
        String format = DateFormatUtils.format(date, "yyyy-MM-dd HH:MM:ss");
        System.out.println("format = " + format);
    }

}
