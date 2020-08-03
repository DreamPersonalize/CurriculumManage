package com.manage.course.repository;

import com.manage.course.entity.Taskpub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskpubRepository extends JpaRepository<Taskpub, Integer> {
    //获取课程所有发布的作业
    @Query("select t from Taskpub t, Course c where t.Course = c.course_id and c.course_id = :course_id" )
    List<Taskpub> FindAllByCourse(@Param("course_id")Integer course_id);
}
