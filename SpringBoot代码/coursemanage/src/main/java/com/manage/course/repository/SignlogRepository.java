package com.manage.course.repository;

import com.manage.course.entity.Signlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SignlogRepository extends JpaRepository<Signlog, Integer> {
    //获取个人今日签到记录
    @Query("select s from Signlog s, Course c, User u where s.Course = c.course_id and s.User = u.user_id and c.course_id = :course_id and u.user_id = :user_id and s.Coursetime = :date" )
    List<Signlog> FindTodayByCourse(@Param("course_id")Integer course_id, @Param("user_id")Integer user_id, @Param("date") Date date);

    //获取个人全部签到记录
    @Query("select s from Signlog s, Course c, User u where s.Course = c.course_id and s.User = u.user_id and c.course_id = :course_id and u.user_id = :user_id" )
    List<Signlog> FindAllByCourse(@Param("course_id")Integer course_id, @Param("user_id")Integer user_id);

    //获取课程节数今日签到记录
    @Query("select s from Signlog s, Course c, Classes cl where s.Course = c.course_id and s.Classes = cl.classes_id and c.course_id = :course_id and cl.classes_id = :classes_id and s.Coursetime = :today and s.Lesson = :lesson" )
    List<Signlog> FindClassesSignToday(@Param("course_id")Integer course_id, @Param("classes_id")Integer classes_id, @Param("today")Date today, @Param("lesson")Integer lesson);

    //获取班级今日签到记录
    @Query("select s from Signlog s, Course c, Classes cl where s.Course = c.course_id and s.Classes = cl.classes_id and c.course_id = :course_id and cl.classes_id = :classes_id and s.Coursetime = :today" )
    List<Signlog> FindSignToday(@Param("course_id")Integer course_id, @Param("classes_id")Integer classes_id, @Param("today")Date today);


    //获取班级全部签到记录
    @Query("select s from Signlog s, Course c, Classes cl where s.Course = c.course_id and s.Classes = cl.classes_id and c.course_id = :course_id and cl.classes_id = :classes_id" )
    List<Signlog> FindClassesSign(@Param("course_id")Integer course_id, @Param("classes_id")Integer classes_id);

}
