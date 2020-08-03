package com.manage.course.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author: zhang
 * date: 2020年4月16日16:46:15
 * 公告表
 */

@Entity
@Data
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notice_id;
    private Integer User;
    private Integer Course;
    private String Name;
    private String Content;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date Releasetime;

    public Integer getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(Integer notice_id) {
        this.notice_id = notice_id;
    }

    public Integer getUser() {
        return User;
    }

    public void setUser(Integer user) {
        User = user;
    }

    public Integer getCourse() {
        return Course;
    }

    public void setCourse(Integer course) {
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

    public Date getReleasetime() {
        return Releasetime;
    }

    public void setReleasetime(Date releasetime) {
        Releasetime = releasetime;
    }

    @Override
    public String toString() {
        return "Notice{" +
                "notice_id=" + notice_id +
                ", User=" + User +
                ", Course=" + Course +
                ", Name='" + Name + '\'' +
                ", Content='" + Content + '\'' +
                ", Releasetime=" + Releasetime +
                '}';
    }
}
