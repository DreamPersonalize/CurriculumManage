package com.manage.course.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * author: zhang
 * date: 2020年4月13日20:57:29
 * 职称表
 */
@Entity
@Data
public class Title {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer title_id;
    private String Name;

    public Integer getTitle_id() {
        return title_id;
    }

    public void setTitle_id(Integer title_id) {
        this.title_id = title_id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return "Title{" +
                "title_id=" + title_id +
                ", Name='" + Name + '\'' +
                '}';
    }
}
