package com.manage.course.repository;

import com.manage.course.entity.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ResourceRepositoryTest {
    @Autowired
    ResourceRepository resourceRepository;

    @Test
    void findbystate(){
        System.out.println(resourceRepository.FindAllByCourse(1));

    }
}