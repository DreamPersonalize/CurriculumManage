package com.manage.course.controller;

import com.manage.course.entity.User;
import com.manage.course.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * author：zhang
 * date：2020年4月13日
 * 登录验证
 */

@RestController
@RequestMapping("/Login")
public class LoginHandler {
    @Autowired
    UserRepository userRepository;

    @PostMapping("/Verification")
    public User VerificationAccount(@RequestBody User user){
        if(user.getAccount() == null || user.getPassword() == null){
            return null;
        }
        User userinfo = userRepository.findUserinfo(user.getAccount());

        if(userinfo == null){
            return null;
        }
        String RealPassword = userinfo.getPassword();
        //验证通过
        if(RealPassword.equals(user.getPassword().trim())){
            userinfo.setPassword(null); //密码不能传前台
            return userinfo;
        }

        return null;
    }

}
