package com.manage.course.repository;

import com.manage.course.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Integer> {
    //根据课程获取公告列表
    @Query("select r from Resource r, Course c where r.Course = c.course_id and c.course_id = :course_id")
    List<Resource> FindAllByCourse(@Param("course_id")Integer course_id);
}
