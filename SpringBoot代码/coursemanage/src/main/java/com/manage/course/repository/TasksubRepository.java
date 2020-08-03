package com.manage.course.repository;

import com.manage.course.entity.Tasksub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TasksubRepository extends JpaRepository<Tasksub, Integer> {
    //获取个人提交的所有作业
    @Query("select t from Tasksub t, Course c, User u where t.Course = c.course_id and t.User = u.user_id and c.course_id = :course_id and u.user_id = :user_id" )
    List<Tasksub> FindPersonTask(@Param("course_id")Integer course_id, @Param("user_id")Integer user_id);
}
