package com.manage.course.repository;

import com.manage.course.entity.Courselect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourselectRepository extends JpaRepository<Courselect, Integer> {
    //根据课程获取所有班级信息
    @Query("select cs from Courselect cs, Course c where cs.Course = c.course_id and c.course_id = :Courseid")
    List<Courselect> findByCourse(@Param("Courseid")Integer Courseid);

    //根据班级获取所有课程信息
    @Query("select cs from Courselect cs, Classes c where cs.Classes = c.classes_id and c.classes_id = :classes_id")
    List<Courselect> findByClasses(@Param("classes_id")Integer classes_id);
}
