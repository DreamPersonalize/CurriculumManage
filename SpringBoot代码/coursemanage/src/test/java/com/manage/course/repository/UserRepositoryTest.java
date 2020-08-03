package com.manage.course.repository;

import com.manage.course.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.util.List;

@SpringBootTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void findPassword(){
        System.out.println(userRepository.findUserinfo("2016130605"));
    }

    @Test
    void findAllteacher(){      //获取所有教师
        System.out.println(userRepository.findByPower(2));
    }

    @Test
    void findAllstudent(){      //获取所有学生
        System.out.println(userRepository.findByPower(3));
    }

    @Test
    void AddTest(){
        String Name = "00000000000陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜";
        for(int i = 11; i<31; i++){
            User user = new User();
            user.setAccount("16001306"+i);
            user.setPassword("16001306"+i);
            user.setAge(23);
            Power po = new Power(3);
            user.setPower(po);
            user.setUsername(Name.charAt(i)+"同学");
            user.setSex(1);
            Department de = new Department();
            de.setDepartment_id(13);
            user.setDepartment(de);
            Classes cl = new Classes(5);
            user.setClasses(cl);
            user.setGrade("2016");
            userRepository.saveAndFlush(user);
        }
//        User user = new User();
//        user.setAccount("22222");
//        user.setPassword("22222");
//        user.setAge(20);
//        Power po = new Power(2);
////        po.setName("教师");
////        po.setPower_id(2);
//        user.setPower(po);
//        user.setUsername("测试姓名");
//        user.setSex(1);
//        Department de = new Department();
////        de.setName("计算机科学与工程学院");
//        de.setDepartment_id(13);
//        user.setDepartment(de);
//        Title ti = new Title();
////        ti.setName("副教授");
//        ti.setTitle_id(2);
//        User u = userRepository.saveAndFlush(user);
//        System.out.println(u);
    }

    @Test
    void testID(){
        List<User> listuser = userRepository.findByPowerDepartment(2, 13);
        System.out.println(listuser);
    }

}