package com.manage.course.entity;

import java.util.Date;

/**
 * 历史记录
 */
public class H {
    private Integer history_id; //记录id
    private Integer user_id;    //用户id
    private double refractive;  //折光率
    private String result;      //甜度结果
    private Date date;          //检测日期

    public Integer getHistory_id() {
        return history_id;
    }

    public void setHistory_id(Integer history_id) {
        this.history_id = history_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public double getRefractive() {
        return refractive;
    }

    public void setRefractive(double refractive) {
        this.refractive = refractive;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "H{" +
                "history_id=" + history_id +
                ", user_id=" + user_id +
                ", refractive=" + refractive +
                ", result='" + result + '\'' +
                ", date=" + date +
                '}';
    }
}
