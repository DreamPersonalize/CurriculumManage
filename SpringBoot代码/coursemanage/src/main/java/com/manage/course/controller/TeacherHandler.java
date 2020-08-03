package com.manage.course.controller;

import com.manage.course.entity.*;
import com.manage.course.repository.*;
import com.manage.course.util.UtilTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/Teacher")
public class TeacherHandler {
    @Autowired
    NoticeRepository noticeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SignlogRepository signlogRepository;

    @Autowired
    TaskpubRepository taskpubRepository;

    @Autowired
    EnclosureRepository enclosureRepository;

    @Autowired
    CourselectRepository courselectRepository;

    @Autowired
    ClassesRepository classesRepository;

    @Autowired
    CourseRepository courseRepository;

    /**
     * 公告管理
     *
     * @param course_id 课程id
     * @return
     */
    @PostMapping("/NoticeSearch")
    public List<Notice> NoticeSearch(@RequestParam("course_id") Integer course_id) {
        noticeRepository.flush();
        List<Notice> NoticeList = noticeRepository.FindCourseNotice(course_id);
        System.out.println("调用了一次查询");
        return NoticeList;
    }

    /**
     * @param notice 公告
     * @return
     */
    @PostMapping("/NoticeAdd")
    public Boolean NoticeAdd(@RequestBody Notice notice) {
        Notice result = noticeRepository.saveAndFlush(notice);
        if (result != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param notice 公告
     * @return
     */
    @PostMapping("/NoticeEdit")
    public Boolean NoticeEdit(@RequestBody Notice notice) {
        Notice result = noticeRepository.saveAndFlush(notice);
        if (result != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param noticeid 公告id
     * @return
     */
    @PostMapping("/NoticeDelete")
    public Boolean NoticeDelete(@RequestParam("noticeid") Integer noticeid) {
        noticeRepository.deleteById(noticeid);
        noticeRepository.flush();
        return true;
    }

    /**
     * 获取所有学生信息
     *
     * @param classes_id
     * @return
     */
    @PostMapping("/GetClassStudent")
    public List<User> GetClassStudent(@RequestParam("classes_id") Integer classes_id) {
        return userRepository.findByClasses(classes_id);
    }

    /**
     * 教师进行补签
     *
     * @param sign
     * @return
     */
    @PostMapping("/Sign")
    public boolean SupplementarySign(@RequestBody Signlog sign) {
        Signlog signlog = signlogRepository.saveAndFlush(sign);
        if (signlog != null) {
            return true;
        }
        return false;
    }

    @PostMapping("/TaskpubSearch")
    public List<Taskpub> ResourceUpload(@RequestParam("course_id") Integer course_id) {
        return taskpubRepository.FindAllByCourse(course_id);
    }

    /**
     * 添加作业
     *
     * @param file     附件
     * @param formData
     * @return
     */
    @PostMapping("/TaskpubAdd")
    public Boolean ResourceUpload(@RequestPart("file") MultipartFile file, @RequestPart("formData") Taskpub formData) {
        String filePath = "D:/test/task/";
        String fileName = file.getOriginalFilename();
        //查重
        List<Enclosure> allEnclosure = enclosureRepository.findAll();
        for (Enclosure e : allEnclosure) {
            if (e.equals(fileName)) {
                return false;
            }
        }
        //存储到本地
        boolean store = UtilTool.uploading(file, filePath);
        if (store) {
            Enclosure enclosure = enclosureRepository.saveAndFlush(new Enclosure(file.getOriginalFilename()));
            if (enclosure != null) {
                Taskpub tp = formData;
                tp.setEnclosure(enclosure);
                Taskpub result = taskpubRepository.saveAndFlush(tp);
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * 作业评判
     * @return
     */
//    @PostMapping("/ResourceExamine")
//    public Boolean ResourceExamine(@RequestParam("Resource_id")Integer Resource_id){
//        Resource re = resourceRepository.findById(Resource_id).get();
//        re.setState(1);
//        Resource result = resourceRepository.saveAndFlush(re);
//        if(result != null){
//            return true;
//        }
//
//        return false;
//    }

    /**
     * 作业修改
     *
     * @param file     文件
     * @param formData 修改信息
     * @return
     */
    @PostMapping("/TaskpubEdit")
    public Boolean TaskEdit(@RequestPart("file") MultipartFile file, @RequestPart("formData") Taskpub formData) {
        String filePath = "D:/test/task/";
        String fileName = file.getOriginalFilename();
        //获取数据库原文件名称
        String fileNameData = taskpubRepository.findById(formData.getTaskpub_id()).get().getEnclosure().getName();
        if (fileName.equals(fileNameData)) {
            Taskpub result = taskpubRepository.saveAndFlush(formData);
        } else {
            //查重
            List<Enclosure> allEnclosure = enclosureRepository.findAll();
            for (Enclosure e : allEnclosure) {
                if (e.equals(fileName)) {
                    return false;
                }
            }
            //存储到本地
            boolean store = UtilTool.uploading(file, filePath);
            if (store) {
                Enclosure enclosure = enclosureRepository.saveAndFlush(new Enclosure(file.getOriginalFilename()));
                if (enclosure != null) {
                    Taskpub tp = formData;
                    tp.setEnclosure(enclosure);
                    Taskpub result = taskpubRepository.saveAndFlush(tp);
                }
            } else {
                return false;
            }
        }

        return true;
    }

    /**
     * 删除作业
     *
     * @param taskpub_id 作业发布id
     * @return
     */
    @PostMapping("/TaskpubDelete")
    public boolean ResourceDelete(@RequestParam("taskpub_id") Integer taskpub_id) {
        String filePath = "D:/test/task/";
        String filename = taskpubRepository.findById(taskpub_id).get().getEnclosure().getName();
        Integer enclosure_id = taskpubRepository.findById(taskpub_id).get().getEnclosure().getEnclosure_id();
        //删除存储的文件
        if (UtilTool.FileDelete(filePath, filename)) {
            taskpubRepository.deleteById(taskpub_id);
            enclosureRepository.deleteById(enclosure_id);
            taskpubRepository.flush();
            return true;
        }
        return false;
    }

    /**
     * 今日签到判断
     * @param course_id 课程id
     * @return
     * @throws ParseException
     */
    @PostMapping("/SignCheck")
    public String SignCheck(@RequestParam("course_id")Integer course_id) throws ParseException {
        //获取课程时间
        Timetable timetable = courseRepository.findById(course_id).get().getTimetable();

        //获取当前时间
        Date now = new Date();

        //判断今天是否需要签到
        if(now.compareTo(timetable.getStart_time()) != -1){
            if(now.compareTo(timetable.getEnd_time()) != 1){
                String nowWeek = UtilTool.getWeekOfDate(now);
                String lessons = "";
                switch (nowWeek){
                    case "Mon":
                        lessons = timetable.getMon();break;
                    case "Tue":
                        lessons = timetable.getTue();break;
                    case "Wed":
                        lessons = timetable.getWed();break;
                    case "Thu":
                        lessons = timetable.getThu();break;
                    case "Fri":
                        lessons = timetable.getFri();break;
                }
                if(lessons!=null && lessons != ""){
                    //根据当前时间判断是否在课程时间内
                    return lessons;
                }
            }
        }
        return "NoLesson";
    }

    /**
     * 获取课程需要签到的所有班级
     *
     * @param course_id 课程id
     * @return
     */
    @PostMapping("/SignClasses")
    public List<Classes> FindClasses(@RequestParam("course_id") Integer course_id) {
        List<Courselect> courselectList = courselectRepository.findByCourse(course_id);
        List<Classes> classesList = new ArrayList<>();
        for (Courselect courselect : courselectList) {
            classesList.add(courselect.getClasses());
        }
        return classesList;
    }

    /**
     * 获取班级所有学生信息
     *
     * @param classes_id 班级id
     * @return
     */
    @PostMapping("/SignClassesAllStudent")
    public List<User> FindClassesAllStudent(@Param("classes_id") Integer classes_id) {
        return userRepository.findByClasses(classes_id);
    }

    /**
     * 获取班级所有今日签到的信息
     * @param course_id
     * @param classes_id
     * @return
     * @throws ParseException
     */
    @PostMapping("/SignClassesSignStudent")
    public List<Signlog> FindClassesSignStudent(@RequestParam("course_id") Integer course_id, @Param("classes_id") Integer classes_id, @Param("lesson") Integer lesson) throws ParseException {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = dateFormat.parse(dateFormat.format(now));
        List<Signlog> signlogList = signlogRepository.FindClassesSignToday(course_id, classes_id, nowDate, lesson);
        return signlogList;
    }

    /**
     * 获取班级历史签到信息
     * @param course_id 课程id
     * @param classes_id 班级id
     * @return
     * @throws ParseException
     */
    @PostMapping("/SignClassesSign")
    public List<Signlog> FindClassesSign(@RequestParam("course_id") Integer course_id, @Param("classes_id") Integer classes_id) throws ParseException {
        List<Signlog> signlogList = signlogRepository.FindClassesSign(course_id, classes_id);
        return signlogList;
    }

    /**
     * 获取折线图所需数据
     * @param course_id 课程id
     * @param classes_id 班级id
     * @return
     * @throws ParseException
     */
    @PostMapping("/SignTotalLesson")
    public List<ModelTotalLesson> SignTotalLesson(@RequestParam("course_id") Integer course_id, @Param("classes_id") Integer classes_id) throws ParseException {
        List<ModelTotalLesson> totalLessons = new ArrayList<>();
        Timetable timetable = courseRepository.findById(course_id).get().getTimetable();
        /*
		 此处一定要new两个时间对象进行接收传递来的参数，否则会在之后保存请假的起止日期时
		 发现用户选择的起止日期和数据库中的日期不一致，原因就在于下面的代码会操作开始日期
		 并使其发生改变，而结束日期不会变化，但是还是都统一处理一下的好（强迫症~）
		  */
        Date starTime = new Date(timetable.getStart_time().getTime());
        Date endTime = new Date(timetable.getEnd_time().getTime());
        Date nowTime = new Date();

        //判断当前日期是否超出课程结束时间
        if(nowTime.compareTo(endTime) == 1){
            nowTime = endTime;
        }

        while (starTime.compareTo(nowTime) < 1){
            String week = UtilTool.getWeekOfDate(starTime);
            if(!( week.equals("Sun") || week.equals("Sat") )){
                String lessons = "";
                switch (week){
                    case "Mon":
                        lessons = timetable.getMon();break;
                    case "Tue":
                        lessons = timetable.getTue();break;
                    case "Wed":
                        lessons = timetable.getWed();break;
                    case "Thu":
                        lessons = timetable.getThu();break;
                    case "Fri":
                        lessons = timetable.getFri();break;
                }
                if(lessons != null && lessons != ""){
                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                    Date myDate1 = dateFormat1.parse(dateFormat1.format(starTime));
                    ModelTotalLesson modelTotalLesson = new ModelTotalLesson();
                    modelTotalLesson.setCoursetime(myDate1);
                    modelTotalLesson.setCount(signlogRepository.FindSignToday(course_id, classes_id, myDate1).size());
                    totalLessons.add(modelTotalLesson);
                }
            }
            starTime.setDate(starTime.getDate() + 1);
        }
        return totalLessons;
    }



}
