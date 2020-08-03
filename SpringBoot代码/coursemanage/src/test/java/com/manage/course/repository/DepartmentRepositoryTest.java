package com.manage.course.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DepartmentRepositoryTest {
    @Autowired
    DepartmentRepository departmentRepository;

    @Test
    void findadd(){
        System.out.println(departmentRepository.findAll());
    }

//    @Test
//    void findalltest(){
//        System.out.println(departmentRepository.findall("体育学院"));
//    }

    @Test
    void findall(){
        System.out.println(departmentRepository.findAll());
    }
}