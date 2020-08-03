package com.manage.course.repository;

import com.manage.course.entity.Classes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ClassesRepositoryTest {
    @Autowired
    ClassesRepository classesRepository;

    @Test
    void findall(){
        List<Classes> test = classesRepository.findAll();
        System.out.println(classesRepository.findAll());
    }

    @Test
    void findByDepart(){
        List<Classes> test = classesRepository.findByDepart(13);
        System.out.println(test);
    }

    @Test
    void findByid(){
    }
}