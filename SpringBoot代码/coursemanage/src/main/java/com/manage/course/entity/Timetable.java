package com.manage.course.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * author: zhang
 * date: 2020年4月13日20:57:29
 * 课表
 */

@Entity
@Data
public class Timetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer timetable_id;

    private Date Start_time;
    private Date End_time;
    private String Mon;
    private String Tue;
    private String Wed;
    private String Thu;
    private String Fri;

    public Integer getTimetable_id() {
        return timetable_id;
    }

    public void setTimetable_id(Integer timetable_id) {
        this.timetable_id = timetable_id;
    }

    public Date getStart_time() {
        return Start_time;
    }

    public void setStart_time(Date start_time) {
        Start_time = start_time;
    }

    public Date getEnd_time() {
        return End_time;
    }

    public void setEnd_time(Date end_time) {
        End_time = end_time;
    }

    public String getMon() {
        return Mon;
    }

    public void setMon(String mon) {
        Mon = mon;
    }

    public String getTue() {
        return Tue;
    }

    public void setTue(String tue) {
        Tue = tue;
    }

    public String getWed() {
        return Wed;
    }

    public void setWed(String wed) {
        Wed = wed;
    }

    public String getThu() {
        return Thu;
    }

    public void setThu(String thu) {
        Thu = thu;
    }

    public String getFri() {
        return Fri;
    }

    public void setFri(String fri) {
        Fri = fri;
    }

    @Override
    public String toString() {
        return "Timetable{" +
                "timetable_id=" + timetable_id +
                ", Start_time=" + Start_time +
                ", End_time=" + End_time +
                ", Mon='" + Mon + '\'' +
                ", Tue='" + Tue + '\'' +
                ", Wed='" + Wed + '\'' +
                ", Thu='" + Thu + '\'' +
                ", Fri='" + Fri + '\'' +
                '}';
    }
}
