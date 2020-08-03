package com.manage.course.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * author: zhang
 * date: 2020年4月13日20:57:29
 * 权限表
 */

@Entity
@Data
public class Power {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer power_id;
    private String Name;

    public Power(){

    }

    public Power(Integer Powerid){
        power_id = Powerid;
    }
    public Integer getPower_id() {
        return power_id;
    }

    public void setPower_id(Integer power_id) {
        this.power_id = power_id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return "Power{" +
                "power_id=" + power_id +
                ", Name='" + Name + '\'' +
                '}';
    }
}
