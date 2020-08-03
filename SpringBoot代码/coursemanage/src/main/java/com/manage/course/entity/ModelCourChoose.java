package com.manage.course.entity;

import java.util.List;

public class ModelCourChoose {
    private Integer Course_id;
    private String Name;
    private String Department;
    private String Teacher;
    private String Classes;

    public Integer getCourse_id() {
        return Course_id;
    }

    public void setCourse_id(Integer course_id) {
        Course_id = course_id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getTeacher() {
        return Teacher;
    }

    public void setTeacher(String teacher) {
        Teacher = teacher;
    }

    public String getClasses() {
        return Classes;
    }

    public void setClasses(String classes) {
        Classes = classes;
    }

    @Override
    public String toString() {
        return "ModelCourChoose{" +
                "Course_id=" + Course_id +
                ", Name='" + Name + '\'' +
                ", Department='" + Department + '\'' +
                ", Teacher='" + Teacher + '\'' +
                ", Classes=" + Classes +
                '}';
    }
}
