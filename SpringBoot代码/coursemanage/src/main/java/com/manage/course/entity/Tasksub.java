package com.manage.course.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * author: zhang
 * date: 2020年4月16日17:11:53
 * 作业上传表
 */

@Entity
@Data
public class Tasksub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tasksub_id;
    @OneToOne
    @JoinColumn(name = "Course")
    private Course Course;
    @ManyToOne
    @JoinColumn(name = "Classes")
    private Classes Classes;
    @OneToOne
    @JoinColumn(name = "User")
    private User User;
    @OneToOne
    @JoinColumn(name = "Enclosure")
    private Enclosure Enclosure;
    private Date Releasetime;
    @OneToOne
    @JoinColumn(name = "Achievement")
    private Achievement Achievement;
    @OneToOne
    @JoinColumn(name = "Task")
    private Taskpub Task;

    public Integer getTasksub_id() {
        return tasksub_id;
    }

    public void setTasksub_id(Integer tasksub_id) {
        this.tasksub_id = tasksub_id;
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

    public com.manage.course.entity.Enclosure getEnclosure() {
        return Enclosure;
    }

    public void setEnclosure(com.manage.course.entity.Enclosure enclosure) {
        Enclosure = enclosure;
    }

    public Date getReleasetime() {
        return Releasetime;
    }

    public void setReleasetime(Date releasetime) {
        Releasetime = releasetime;
    }

    public com.manage.course.entity.Achievement getAchievement() {
        return Achievement;
    }

    public void setAchievement(com.manage.course.entity.Achievement achievement) {
        Achievement = achievement;
    }

    public Taskpub getTask() {
        return Task;
    }

    public void setTask(Taskpub task) {
        Task = task;
    }

    @Override
    public String toString() {
        return "Tasksub{" +
                "tasksub_id=" + tasksub_id +
                ", Course=" + Course +
                ", Classes=" + Classes +
                ", User=" + User +
                ", Enclosure=" + Enclosure +
                ", Releasetime=" + Releasetime +
                ", Achievement=" + Achievement +
                ", Task=" + Task +
                '}';
    }
}
