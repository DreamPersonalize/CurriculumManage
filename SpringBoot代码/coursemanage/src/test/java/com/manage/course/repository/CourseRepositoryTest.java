package com.manage.course.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class CourseRepositoryTest {
    @Autowired
    CourseRepository courseRepository;

    @Test
    void test(){
        System.out.println(courseRepository.findAll());
    }
}