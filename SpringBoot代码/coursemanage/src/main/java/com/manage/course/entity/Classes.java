package com.manage.course.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


/**
 * author: zhang
 * date: 2020年4月13日20:57:29
 * 班级表
 */

@Entity
@Data
public class Classes implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer classes_id;

    @OneToOne
    @JoinColumn(name = "Department")
    private Department Department;

    private String Name;
    private String Grade;

    public Classes(){

    }

    public Classes(Integer classes_id){
        this.classes_id = classes_id;
    }
    public Integer getClasses_id() {
        return classes_id;
    }

    public void setClasses_id(Integer classes_id) {
        this.classes_id = classes_id;
    }

    public com.manage.course.entity.Department getDepartment() {
        return Department;
    }

    public void setDepartment(com.manage.course.entity.Department department) {
        Department = department;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getGrade() {
        return Grade;
    }

    public void setGrade(String grade) {
        Grade = grade;
    }


    @Override
    public String toString() {
        return "Classes{" +
                "classes_id=" + classes_id +
                ", Name='" + Name + '\'' +
                ", Grade='" + Grade + '\'' +
                '}';
    }
}
