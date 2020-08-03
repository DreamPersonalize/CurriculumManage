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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/Student")
public class StudentHandler {
    @Autowired
    SignlogRepository signlogRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    NoticeRepository noticeRepository;

    @Autowired
    TasksubRepository tasksubRepository;

    @Autowired
    EnclosureRepository enclosureRepository;

    /**
     * 今日签到判断
     * @param course_id 课程id
     * @param user_id 用户id
     * @return
     * @throws ParseException
     */
    @PostMapping("/SignCheck")
    public String SignCheck(@RequestParam("course_id")Integer course_id, @RequestParam("user_id")Integer user_id) throws ParseException {
        //获取课程时间
        Timetable timetable = courseRepository.findById(course_id).get().getTimetable();

        //获取当前时间
        Date now = new Date();

        //判断当前时间是否需要签到
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
                    if(JudgeLesson(lessons, now)){
                        //查询签到记录判断是否需签到
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date nowDate = dateFormat.parse(dateFormat.format(now));
                        List<Signlog> SignList = signlogRepository.FindTodayByCourse(course_id,user_id, nowDate);
                        String[] arr = lessons.split("");
                        for (int i = 0; i< arr.length; i++) {
                            for(Signlog signlog : SignList){
                                if(String.valueOf(signlog.getLesson()).equals(arr[i])){
                                    return "Sign";
                                }
                            }
                        }
                        //历史记录没有则返回需签到
                            return "NoSign";
                    }
                }
            }
        }
        return "NoLesson";
    }

    /**
     * 获取历史签到信息
     * @param course_id 课程id
     * @param user_id 用户id
     * @return
     */
    @PostMapping("/SignHistory")
    public List<Signlog> SignHistory(@RequestParam("course_id")Integer course_id, @RequestParam("user_id")Integer user_id){
        return signlogRepository.FindAllByCourse(course_id, user_id);
    }

    /**
     * 判断当前时间是否有课
     * @param lessons 课程安排
     * @param now 当前时间
     * @return
     */
    public boolean JudgeLesson(String lessons, Date now) {
        //获取当前小时数
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int minute = rightNow.get(Calendar.MINUTE);
        //获取当天所有课程节数
        String[] arr = lessons.split("");
        for (int i = 0; i< arr.length; i++){
            switch (arr[i]){
                case "1":
                    if(hour == 8){
                        return true;
                    }
                    if(hour == 9 && minute < 41){
                        return true;
                    }
                    break;
                case "2":
                    if(hour == 10){
                        return true;
                    }
                    if(hour == 11 && minute < 41){
                        return true;
                    }
                    break;
                case "3":
                    if(hour == 14){
                        return true;
                    }
                    if(hour == 15 && minute < 41){
                        return true;
                    }
                    break;
                case "4":
                    if(hour == 16){
                        return true;
                    }
                    if(hour == 17 && minute < 41){
                        return true;
                    }
                    break;
                case "5":
                    if(hour == 18){
                        return true;
                    }
                    if(hour == 19 && minute < 41){
                        return true;
                    }
                    break;
                case "6":
                    if(hour == 20){
                        return true;
                    }
                    if(hour == 21 && minute < 41){
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    /**
     * 获取个人所有作业信息
     * @param course_id
     * @param user_id
     * @return
     */
    @PostMapping("/GetAllTask")
    public List<Tasksub> GetAllTask(@RequestParam("course_id")Integer course_id, @RequestParam("user_id")Integer user_id){
        return tasksubRepository.FindPersonTask(course_id, user_id);
    }

    /**
     * 学生签到
     * @param sign  签到信息
     * @return
     */
    @PostMapping("/Sign")
    public boolean Sign(@RequestBody Signlog sign){
        Signlog signlog = signlogRepository.saveAndFlush(sign);
        if(signlog != null){
            return true;
        }
        return false;
    }

    /**
     * 获取作业提交列表
     * @param course_id 课程id
     * @return
     */
    @PostMapping("/TasksubSearch")
    public List<Tasksub> TasksubSearch(@Param("course_id")Integer course_id, @Param("user_id")Integer user_id){
        List<Tasksub> Tasksubs = tasksubRepository.FindPersonTask(course_id, user_id);
        return Tasksubs;
    }

    /**
     * 提交作业
     * @param file  附件
     * @param formData
     * @return
     */
    @PostMapping("/TasksubAdd")
    public Boolean ResourceUpload(@RequestPart("file") MultipartFile file, @RequestPart("formData")Tasksub formData){
        String filePath = "D:/test/task/";
        String fileName = file.getOriginalFilename();
        //查重
        List<Enclosure> allEnclosure = enclosureRepository.findAll();
        for(Enclosure e : allEnclosure){
            if(e.equals(fileName)){
                return false;
            }
        }
        //存储到本地
        boolean store = UtilTool.uploading(file, filePath);
        if(store){
            Enclosure enclosure = enclosureRepository.saveAndFlush(new Enclosure(file.getOriginalFilename()));
            if(enclosure != null){
                Tasksub ts = formData;
                ts.setEnclosure(enclosure);
                Tasksub result = tasksubRepository.saveAndFlush(ts);
            }
        }else {
            return false;
        }
        return true;
    }

    /**
     * 作业修改
     * @param file  文件
     * @param formData 修改信息
     * @return
     */
    @PostMapping("/TaskEdit")
    public Boolean TaskEdit(@RequestPart("file") MultipartFile file, @RequestPart("formData")Tasksub formData){
        String filePath = "D:/test/resource/";
        String fileName = file.getOriginalFilename();
        //查重
        List<Enclosure> allEnclosure = enclosureRepository.findAll();
        for(Enclosure e : allEnclosure){
            if(e.equals(fileName)){
                return false;
            }
        }
        //存储到本地
        boolean store = UtilTool.uploading(file, filePath);
        if(store){
            Enclosure enclosure = enclosureRepository.saveAndFlush(new Enclosure(file.getOriginalFilename()));
            if(enclosure != null){
                Tasksub ts = formData;
                ts.setEnclosure(enclosure);
                Tasksub result = tasksubRepository.saveAndFlush(ts);
            }
        }else {
            return false;
        }
        return true;
    }



}
