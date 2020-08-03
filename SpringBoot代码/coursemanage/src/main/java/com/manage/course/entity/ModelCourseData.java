package com.manage.course.entity;

import java.util.Date;
import java.util.List;

public class ModelCourseData {
    private Integer Course_id;
    private String Name;
    private String Content;
    private Integer Department;
    private Integer Teacher;
    private Date Publish;
    private Integer Timetable_id;
    private List<Date> Timetable;
    private List<List<Integer>> Timing;
    private List<Integer> Classes;

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

    public Integer getDepartment() {
        return Department;
    }

    public void setDepartment(Integer department) {
        Department = department;
    }

    public Integer getTeacher() {
        return Teacher;
    }

    public void setTeacher(Integer teacher) {
        Teacher = teacher;
    }

    public Date getPublish() {
        return Publish;
    }

    public void setPublish(Date publish) {
        Publish = publish;
    }

    public List<Date> getTimetable() {
        return Timetable;
    }

    public void setTimetable(List<Date> timetable) {
        Timetable = timetable;
    }

    public List<List<Integer>> getTiming() {
        return Timing;
    }

    public void setTiming(List<List<Integer>> timing) {
        Timing = timing;
    }

    public List<Integer> getClasses() {
        return Classes;
    }

    public void setClasses(List<Integer> classes) {
        Classes = classes;
    }

    public Integer getCourse_id() {
        return Course_id;
    }

    public void setCourse_id(Integer courseid) {
        Course_id = courseid;
    }

    public Integer getTimetable_id() {
        return Timetable_id;
    }

    public void setTimetable_id(Integer timetable_id) {
        Timetable_id = timetable_id;
    }

    @Override
    public String toString() {
        return "ModelCourseData{" +
                "Course_id=" + Course_id +
                ", Name='" + Name + '\'' +
                ", Content='" + Content + '\'' +
                ", Department=" + Department +
                ", Teacher=" + Teacher +
                ", Publish=" + Publish +
                ", Timetable_id=" + Timetable_id +
                ", Timetable=" + Timetable +
                ", Timing=" + Timing +
                ", Classes=" + Classes +
                '}';
    }
}
