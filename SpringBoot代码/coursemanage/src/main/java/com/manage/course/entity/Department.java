package com.manage.course.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


/**
 * author: zhang
 * date: 2020年4月13日20:57:29
 * 院系表
 */

@Entity
@Data
public class Department implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer department_id;
    private String Name;

    public Department(){

    }
    public Department(Integer department_id){
        this.department_id = department_id;
    }

    public Integer getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(Integer department_id) {
        this.department_id = department_id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return "Department{" +
                "department_id=" + department_id +
                ", Name='" + Name + '\'' +
                '}';
    }
}
