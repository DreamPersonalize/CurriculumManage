package com.manage.course.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * author: zhang
 * date: 2020年4月16日16:06:26
 * 课程选择表
 */

@Entity
@Data
public class Courselect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courselect_id;
    @OneToOne
    @JoinColumn(name = "Course")
    private Course Course;
    @OneToOne
    @JoinColumn(name = "Classes")
    private Classes Classes;

    public Integer getCourselect_id() {
        return courselect_id;
    }

    public void setCourselect_id(Integer courselect_id) {
        this.courselect_id = courselect_id;
    }

    public com.manage.course.entity.Course getCourse() {
        return Course;
    }

    public void setCourse(com.manage.course.entity.Course course) {
        Course = course;
    }

    public com.manage.course.entity.Classes getClasses() {
        return Classes;
    }

    public void setClasses(com.manage.course.entity.Classes classes) {
        Classes = classes;
    }

    @Override
    public String toString() {
        return "Courselect{" +
                "courselect_id=" + courselect_id +
                ", Course=" + Course +
                ", Classes=" + Classes +
                '}';
    }
}
