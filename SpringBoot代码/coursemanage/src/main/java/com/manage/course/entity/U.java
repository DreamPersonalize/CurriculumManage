package com.manage.course.entity;

/**
 * 用户
 */
public class U {
    private Integer user_id;    //用户id
    private String username;    //账号
    private String password;    //密码

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "U{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
