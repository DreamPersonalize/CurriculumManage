package com.manage.course.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SignlogRepositoryTest {

    @Autowired
    SignlogRepository signlogRepository;

    @Test
    void findall() throws ParseException {
        Date now = new Date();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        Date myDate1 = dateFormat1.parse(dateFormat1.format(now));
//        System.out.println(signlogRepository.FindTodayByCourse(1, 1, myDate1));
        System.out.println(signlogRepository.FindClassesSign(1, 5));
    }
}