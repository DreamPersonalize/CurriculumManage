package com.manage.course.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * author: zhang
 * date: 2020年4月13日20:57:29
 * 用户表
 */

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;
    private String Account;
    private String Password;
    @OneToOne
    @JoinColumn(name = "Power")
    private Power Power;
    private String Username;
    private Integer Sex;
    private Integer Age;
    @OneToOne
    @JoinColumn(name = "Department")
    private Department Department;
    @OneToOne
    @JoinColumn(name = "Classes")
    private Classes Classes;
    private String Grade;

    @OneToOne
    @JoinColumn(name = "Title")
    private Title Title;

    public User(){

    }

    public User(Integer user_id){
        this.user_id = user_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public com.manage.course.entity.Power getPower() {
        return Power;
    }

    public void setPower(com.manage.course.entity.Power power) {
        Power = power;
    }

    public Integer getSex() {
        return Sex;
    }

    public void setSex(Integer sex) {
        Sex = sex;
    }

    public Integer getAge() {
        return Age;
    }

    public void setAge(Integer age) {
        Age = age;
    }

    public com.manage.course.entity.Department getDepartment() {
        return Department;
    }

    public void setDepartment(com.manage.course.entity.Department department) {
        Department = department;
    }

    public com.manage.course.entity.Classes getClasses() {
        return Classes;
    }

    public void setClasses(com.manage.course.entity.Classes classes) {
        Classes = classes;
    }

    public String getGrade() {
        return Grade;
    }

    public void setGrade(String grade) {
        Grade = grade;
    }

    public com.manage.course.entity.Title getTitle() {
        return Title;
    }

    public void setTitle(com.manage.course.entity.Title title) {
        Title = title;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", Account='" + Account + '\'' +
                ", Password='" + Password + '\'' +
                ", Power=" + Power +
                ", Username='" + Username + '\'' +
                ", Sex=" + Sex +
                ", Age=" + Age +
                ", Department=" + Department +
                ", Classes=" + Classes +
                ", Grade='" + Grade + '\'' +
                ", Title=" + Title +
//                ", Signlog=" + Signlog +
                '}';
    }
}
