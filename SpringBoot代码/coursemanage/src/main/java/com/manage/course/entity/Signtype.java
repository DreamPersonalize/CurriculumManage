package com.manage.course.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * author: zhang
 * date: 2020年4月16日16:57:09
 * 签到类型表
 */

@Entity
@Data
public class Signtype {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer signtype_id;
    private String Name;

    public Integer getSigntype_id() {
        return signtype_id;
    }

    public void setSigntype_id(Integer signtype_id) {
        this.signtype_id = signtype_id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return "Signtype{" +
                "signtype_id=" + signtype_id +
                ", Name='" + Name + '\'' +
                '}';
    }
}
