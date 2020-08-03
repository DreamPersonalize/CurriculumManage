package com.manage.course.entity;

import java.util.Date;

public class ModelTotalLesson {
    private Date coursetime;
    private Integer count;

    public Date getCoursetime() {
        return coursetime;
    }

    public void setCoursetime(Date coursetime) {
        this.coursetime = coursetime;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ModelTotalLesson{" +
                "coursetime=" + coursetime +
                ", count=" + count +
                '}';
    }
}
