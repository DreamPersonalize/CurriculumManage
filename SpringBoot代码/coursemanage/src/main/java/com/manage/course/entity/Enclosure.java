package com.manage.course.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * author: zhang
 * date: 2020年4月16日16:37:04
 * 附件表
 */

@Entity
@Data
public class Enclosure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer enclosure_id;
    private String Name;
    private String Type;

    public Enclosure(){

    }

    public Enclosure(String name){
        this.Name = name;
    }

    public Integer getEnclosure_id() {
        return enclosure_id;
    }

    public void setEnclosure_id(Integer enclosure_id) {
        this.enclosure_id = enclosure_id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    @Override
    public String toString() {
        return "Enclosure{" +
                "enclosure_id=" + enclosure_id +
                ", Name='" + Name + '\'' +
                ", Type='" + Type + '\'' +
                '}';
    }
}
