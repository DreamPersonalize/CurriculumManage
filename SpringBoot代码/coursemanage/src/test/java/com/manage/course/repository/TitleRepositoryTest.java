package com.manage.course.repository;

import com.manage.course.entity.Title;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TitleRepositoryTest {
    @Autowired
    TitleRepository titleRepository;

    @Test
    void findAll(){
        List<Title> titles = titleRepository.findAll();
        System.out.println(titles);
    }
}