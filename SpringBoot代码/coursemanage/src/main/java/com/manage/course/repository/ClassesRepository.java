package com.manage.course.repository;

import com.manage.course.entity.Classes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ClassesRepository extends JpaRepository<Classes, Integer> {
    //根据院系获取所有班级信息
    @Query("select c from Classes c, Department d where c.Department=d.department_id and d.department_id = :Department")
    List<Classes> findByDepart(@Param("Department")Integer Department);
}
