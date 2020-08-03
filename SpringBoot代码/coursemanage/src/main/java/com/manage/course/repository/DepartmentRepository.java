package com.manage.course.repository;

import com.manage.course.entity.Courselect;
import com.manage.course.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    //根据课程获取班级信息
    @Query("select cs from Courselect cs, Course c where cs.Course = c.course_id and c.course_id = :Courseid")
    List<Courselect> findByPower(@Param("Courseid")Integer Courseid);
}
