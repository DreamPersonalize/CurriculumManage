package com.manage.course.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * author: zhang
 * date: 2020年4月16日15:46:47
 * 成绩表
 */

@Entity
@Data
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer achievement_id;
    private String Name;

    public Integer getAchievement_id() {
        return achievement_id;
    }

    public void setAchievemenet_id(Integer achievemenet_id) {
        this.achievement_id = achievemenet_id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return "Achievement{" +
                "achievemenet_id=" + achievement_id +
                ", Name='" + Name + '\'' +
                '}';
    }
}
