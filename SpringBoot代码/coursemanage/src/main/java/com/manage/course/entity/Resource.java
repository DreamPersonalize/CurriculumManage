package com.manage.course.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * author: zhang
 * date: 2020年4月16日16:46:40
 * 资源表
 */

@Entity
@Data
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer resource_id;
    @OneToOne
    @JoinColumn(name = "Course")
    private Course Course;
    private String Name;
    private String Content;
    @OneToOne
    @JoinColumn(name = "User")
    private User User;
    @OneToOne
    @JoinColumn(name = "Enclosure")
    private Enclosure Enclosure;
    private Date Releasetime;
    private Integer State;

    public Integer getResource_id() {
        return resource_id;
    }

    public void setResource_id(Integer resource_id) {
        this.resource_id = resource_id;
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

    public Integer getState() {
        return State;
    }

    public void setState(Integer state) {
        State = state;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "resource_id=" + resource_id +
                ", Course=" + Course +
                ", Name='" + Name + '\'' +
                ", Content='" + Content + '\'' +
                ", User=" + User +
                ", Enclosure=" + Enclosure +
                ", ReleaseTime='" + Releasetime + '\'' +
                ", State=" + State +
                '}';
    }
}
