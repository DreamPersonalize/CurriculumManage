package com.manage.course.repository;

import com.manage.course.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    //获取公告基本信息
    @Query("select n from Notice n where n.User = :User")
    List<Notice> FindNoticeByUser(@Param("User")Integer User);

    //获取学校公告
    @Query(nativeQuery = true, value = "select * from Notice n, User u, Power p where n.User = u.user_id and u.Power = p.power_id and p.power_id = :power_id order by n.ReleaseTime asc")
    List<Notice> FindSchoolNotice(@Param("power_id")Integer power_id);

    //获取课程公告
    @Query(nativeQuery = true, value = "select * from Notice n, Course c where n.Course = c.course_id and c.course_id = :course_id order by n.ReleaseTime asc ")
    List<Notice> FindCourseNotice(@Param("course_id")Integer course_id);

    //添加公告信息       原生sql处理hibernate 一级缓存问题解决方案
//    @Transactional
//    @Modifying(clearAutomatically = true)
//    @Query(nativeQuery = true, value = "insert into notice (User, Name, Course, Content, ReleaseTime) values (:User, :Name, :Course, :Content, :Releasetime)")
//    int AddNotice(@Param("User")Integer User, @Param("Name") String Name, @Param("Course") Integer Course, @Param("Content") String Content, @Param("Releasetime")Date Releasetime);
}
