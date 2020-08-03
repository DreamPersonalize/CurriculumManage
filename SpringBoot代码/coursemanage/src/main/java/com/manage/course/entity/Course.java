package com.manage.course.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * author: zhang
 * date: 2020年4月16日16:02:54
 * 课程表
 */

@Entity
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer course_id;
    private String Name;
    private String Content;
    @OneToOne
    @JoinColumn(name = "Department")
    private Department Department;
    @OneToOne
    @JoinColumn(name = "Teacher")
    private User Teacher;
    @OneToOne
    @JoinColumn(name = "Timetable")
    private Timetable Timetable;
    private Date Publish;

    public Course(){

    }

    public Course(Integer course_id){
        this.course_id = course_id;
    }

    public Integer getCourse_id() {
        return course_id;
    }

    public void setCourse_id(Integer course_id) {
        this.course_id = course_id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public com.manage.course.entity.Department getDepartment() {
        return Department;
    }

    public void setDepartment(com.manage.course.entity.Department department) {
        Department = department;
    }

    public com.manage.course.entity.Timetable getTimetable() {
        return Timetable;
    }

    public void setTimetable(com.manage.course.entity.Timetable timetable) {
        Timetable = timetable;
    }

    public Date getPublish() {
        return Publish;
    }

    public void setPublish(Date publish) {
        Publish = publish;
    }

    public User getTeacher() {
        return Teacher;
    }

    public void setTeacher(User teacher) {
        Teacher = teacher;
    }

    @Override
    public String toString() {
        return "Course{" +
                "course_id=" + course_id +
                ", Name='" + Name + '\'' +
                ", Content='" + Content + '\'' +
                ", Department=" + Department +
                ", Teacher=" + Teacher +
                ", Timetable=" + Timetable +
                ", Publish=" + Publish +
                '}';
    }
}
