package com.manage.course.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TaskpubRepositoryTest {
    @Autowired
    TaskpubRepository taskpubRepository;

    @Test
    void test(){
        System.out.println(taskpubRepository.FindAllByCourse(1));
    }
}