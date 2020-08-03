package com.manage.course.repository;

import com.manage.course.entity.Notice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class NoticeRepositoryTest {
    @Autowired
    NoticeRepository noticeRepository;

    @Test
    void findall(){
        System.out.println(noticeRepository.findAll());
    }
    @Test
    void findallbyuser(){
        System.out.println(noticeRepository.FindNoticeByUser(2));
    }

//    @Test
//    void add(){
//        Notice n = new Notice();
//        n.setUser(1);
//        n.setName("测试test");
//        n.setContent("测试test");
//        n.setReleasetime(new Date());
//        int result = noticeRepository.AddNotice(1, "测试test", 1, "测试test", new Date());
//        System.out.println(result);
//    }
    @Test
    void schoolnotice(){
        List<Notice> nl = noticeRepository.FindSchoolNotice(1);
        System.out.println();
    }

    @Test
    void coursenotice(){
        List<Notice> nl = noticeRepository.FindCourseNotice(1);
        System.out.println();
    }
}