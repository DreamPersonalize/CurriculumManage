package com.manage.course.repository;

import com.manage.course.entity.Classes;
import com.manage.course.entity.Course;
import com.manage.course.entity.Courselect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CourselectRepositoryTest {
    @Autowired
    CourselectRepository courselectRepository;

    @Test
    void test(){
        Courselect courselect = new Courselect();
        courselect.setCourse(new Course(1));
        courselect.setClasses(new Classes(1));
        System.out.println(courselectRepository.saveAndFlush(courselect));
    }

    @Test
    void testFindByCourse(){
        System.out.println(courselectRepository.findByCourse(1));
    }
}