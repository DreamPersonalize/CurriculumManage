package com.manage.course.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * author: zhang
 * date: 2020年4月16日17:02:11
 * 作业发布表
 */

@Entity
@Data
public class Taskpub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer taskpub_id;
    @OneToOne
    @JoinColumn(name = "Course")
    private Course Course;
    private String Name;
    private String Content;
    @OneToOne
    @JoinColumn(name = "User")
    private User User;
    private Date Start_time;
    private Date End_time;
    private Date Releasetime;
    private Integer Makeup;
    @OneToOne
    @JoinColumn(name = "Enclosure")
    private Enclosure Enclosure;

    public Integer getTaskpub_id() {
        return taskpub_id;
    }

    public void setTaskpub_id(Integer taskpub_id) {
        this.taskpub_id = taskpub_id;
    }

    public com.manage.course.entity.Course getCourse() {
        return Course;
    }

    public void setCourse(com.manage.course.entity.Course course) {
        Course = course;
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

    public com.manage.course.entity.User getUser() {
        return User;
    }

    public void setUser(com.manage.course.entity.User user) {
        User = user;
    }

    public Date getStart_time() {
        return Start_time;
    }

    public void setStart_time(Date start_time) {
        Start_time = start_time;
    }

    public Date getEnd_time() {
        return End_time;
    }

    public void setEnd_time(Date end_time) {
        End_time = end_time;
    }

    public Date getReleasetime() {
        return Releasetime;
    }

    public void setReleasetime(Date releasetime) {
        Releasetime = releasetime;
    }

    public Integer getMakeup() {
        return Makeup;
    }

    public void setMakeup(Integer makeup) {
        Makeup = makeup;
    }

    public com.manage.course.entity.Enclosure getEnclosure() {
        return Enclosure;
    }

    public void setEnclosure(com.manage.course.entity.Enclosure enclosure) {
        Enclosure = enclosure;
    }

    @Override
    public String toString() {
        return "Taskpub{" +
                "taskpub_id=" + taskpub_id +
                ", Course=" + Course +
                ", Name='" + Name + '\'' +
                ", Content='" + Content + '\'' +
                ", User=" + User +
                ", Start_time=" + Start_time +
                ", End_time=" + End_time +
                ", Releasetime=" + Releasetime +
                ", Makeup=" + Makeup +
                ", Enclosure=" + Enclosure +
                '}';
    }
}
