package com.manage.course.repository;

import com.manage.course.entity.Timetable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class TimetableRepositoryTest {
    @Autowired
    TimetableRepository timetableRepository;

    @Test
    void testadd(){
        Timetable timetable = new Timetable();
        timetable.setStart_time(new Date());
        timetable.setEnd_time(new Date());
        timetable.setMon(timetable.getMon()+""+1);
        timetable.setTue("23");
        timetable.setWed("34");
        timetable.setThu("45");
        timetable.setFri("56");
        timetableRepository.saveAndFlush(timetable);
    }

}