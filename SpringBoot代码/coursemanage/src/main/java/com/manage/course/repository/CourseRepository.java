package com.manage.course.repository;

import com.manage.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    //根据教师获取所有课程信息
    @Query("select c from Course c, User u where c.Teacher = u.user_id and u.user_id = :Userid")
    List<Course> findByTeacher(@Param("Userid")Integer Userid);
}
