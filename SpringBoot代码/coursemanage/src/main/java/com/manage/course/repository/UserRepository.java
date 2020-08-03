package com.manage.course.repository;

import com.manage.course.entity.Department;
import com.manage.course.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    //获取用户基本信息
    @Query("select u from User u where u.Account = :Account")
    User findUserinfo(@Param("Account")String Account);

    //根据用户角色获取所有用户信息
    @Query("select u from User u, Power p where u.Power = p.power_id and p.power_id = :Power")
    List<User> findByPower(@Param("Power")Integer Power);

    //获取院系教师或学生信息
    @Query("select u from User u, Power p, Department d where u.Power = p.power_id and p.power_id = :Power and u.Department = d.department_id and d.department_id = :Department")
    List<User> findByPowerDepartment(@Param("Power")Integer Power, @Param("Department")Integer Department);

    //根据班级信息获取所有学生信息
    @Query("select u from User u, Classes c where u.Classes = c.classes_id and c.classes_id = :classes_id")
    List<User> findByClasses(@Param("classes_id")Integer classes_id);
}
