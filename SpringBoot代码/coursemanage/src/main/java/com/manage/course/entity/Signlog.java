package com.manage.course.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * author: zhang
 * date: 2020年4月16日16:52:17
 * 签到记录表
 */

@Entity
@Data
public class Signlog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer signlog_id;
    @ManyToOne
    @JoinColumn(name = "Course")
    private Course Course;
    @ManyToOne
    @JoinColumn(name = "Classes")
    private Classes Classes;
    @ManyToOne
    @JoinColumn(name = "User")
    private User User;
    private Date Coursetime;
    private Integer Lesson;
    private Date Signtime;
    @OneToOne
    @JoinColumn(name = "Signtype")
    private Signtype Signtype;

    public Integer getSignlog_id() {
        return signlog_id;
    }

    public void setSignlog_id(Integer signlog_id) {
        this.signlog_id = signlog_id;
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

    public com.manage.course.entity.User getUser() {
        return User;
    }

    public void setUser(com.manage.course.entity.User user) {
        User = user;
    }

    public Date getCoursetime() {
        return Coursetime;
    }

    public void setCoursetime(Date coursetime) {
        Coursetime = coursetime;
    }

    public Integer getLesson() {
        return Lesson;
    }

    public void setLesson(Integer lesson) {
        Lesson = lesson;
    }

    public Date getSigntime() {
        return Signtime;
    }

    public void setSigntime(Date signtime) {
        Signtime = signtime;
    }

    public Signtype getSingtype() {
        return Signtype;
    }

    public void setSingtype(Signtype singtype) {
        Signtype = singtype;
    }

    @Override
    public String toString() {
        return "Signlog{" +
                "signlog_id=" + signlog_id +
                ", Course=" + Course +
                ", Classes=" + Classes +
                ", User=" + User +
                ", Coursetime=" + Coursetime +
                ", Lesson=" + Lesson +
                ", Signtime=" + Signtime +
                ", Singtype=" + Signtype +
                '}';
    }
}
