package com.manage.course.controller;

import com.manage.course.entity.*;
import com.manage.course.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * author:zhang
 * date:2020年4月17日19:33:18
 * 管理员界面管理
 */

@RestController
@RequestMapping("/Admin")
public class AdminHandler {
    @Autowired
    NoticeRepository noticeRepository;

    @Autowired
    ClassesRepository classesRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    TimetableRepository timetableRepository;

    @Autowired
    CourselectRepository courselectRepository;

    /**
     * 公告管理
     * @param user_id 用户id
     * @return
     */
    @PostMapping("/NoticeSearch")
    public List<Notice> NoticeSearch(@RequestParam("userid") Integer user_id){
        noticeRepository.flush();
        List<Notice> NoticeList = noticeRepository.FindNoticeByUser(user_id);
        System.out.println("调用了一次查询");
        return NoticeList;
    }

    /**
     *
     * @param notice 公告
     * @return
     */
    @PostMapping("/NoticeAdd")
    public Boolean NoticeAdd(@RequestBody Notice notice){
//        int result = noticeRepository.AddNotice(notice.getUser(), notice.getName(), notice.getCourse(), notice.getContent(), notice.getReleasetime());
//        if(result == 1){
//            return true;
//        }else {
//            return false;
//        }
        Notice result = noticeRepository.saveAndFlush(notice);
        if(result != null){
            return true;
        }else {
            return false;
        }
    }

    /**
     *
     * @param notice 公告
     * @return
     */
    @PostMapping("/NoticeEdit")
    public Boolean NoticeEdit(@RequestBody Notice notice){
        Notice result = noticeRepository.saveAndFlush(notice);
        if(result != null){
            return true;
        }else {
            return false;
        }
    }

    /**
     *
     * @param noticeid 公告id
     * @return
     */
    @PostMapping("/NoticeDelete")
    public Boolean NoticeDelete(@RequestParam("noticeid") Integer noticeid){
        noticeRepository.deleteById(noticeid);
        noticeRepository.flush();
        return true;
    }

    /**
     * 班级管理
     * @return 所有班级信息
     */
    @PostMapping("/ClassesSearch")
    public List<Classes> ClassesSearch(){
        classesRepository.flush();
        List<Classes> ClassesList = classesRepository.findAll();
        System.out.println("调用了一次查询");
        return ClassesList;
    }

    /**
     * @param classes 班级
     * @return
     */
    @PostMapping("/ClassesAdd")
    public Boolean ClassesAdd(@RequestBody Classes classes){
//        int result = noticeRepository.AddNotice(notice.getUser(), notice.getName(), notice.getCourse(), notice.getContent(), notice.getReleasetime());
//        if(result == 1){
//            return true;
//        }else {
//            return false;
//        }
        Classes result = classesRepository.saveAndFlush(classes);
        if(result != null){
            return true;
        }else {
            return false;
        }
    }

    /**
     *
     * @param classes   班级
     * @return
     */
    @PostMapping("/ClassesEdit")
    public Boolean ClassesEdit(@RequestBody Classes classes){
        Classes result = classesRepository.saveAndFlush(classes);
        if(result != null){
            return true;
        }else {
            return false;
        }
    }

    /**
     *
     * @param classesid 班级id
     * @return
     */
    @PostMapping("/ClassesDelete")
    public Boolean ClassesDelete(@RequestParam("classesid") Integer classesid){
        classesRepository.deleteById(classesid);
        classesRepository.flush();
        return true;
    }

    /**
     * 用户管理
     * @param role  用户角色
     * @return  相应用户列表
     */
    @PostMapping("/UserSearch")
    public List<User> UserSearch(@RequestParam("role") String role){
        userRepository.flush();

        List<User> userList = null; //用户列表
        if(role.equals("teacher")){
            userList = userRepository.findByPower(2);
        }else if(role.equals("student")){
            userList = userRepository.findByPower(3);
        }
        System.out.println("调用了一次查询");
        return userList;
    }

    /**
     *
     * @param user  用户
     * @return
     */
    @PostMapping("/UserAdd")
    public Boolean UserAdd(@RequestBody User user){
        String Account = "";
        Classes classes = null;
        int power = user.getPower().getPower_id();
        if(power == 2){
            Account += "10548";
        }else if(power == 3){
            classes = classesRepository.findById(user.getClasses().getClasses_id()).get();
            user.setGrade(classes.getGrade());
            Account += user.getGrade();
        }

        int Department_id = user.getDepartment().getDepartment_id();
        if(Department_id < 10){
            Account += "0"+Department_id;
        }else{
            Account += Department_id;
        }
        if(power == 3){
            String classesName = classes.getName();
            Account += classesName.substring(2, 4);
        }
        List<User> userList = userRepository.findByPowerDepartment(user.getPower().getPower_id(), Department_id);

        //判断是否是第一个
        if(userList.size() == 0){
            Account += "01";
        }else{
            String NewSerial = "";
            String LastAccount = userList.get(userList.size()-1).getAccount();
            int Serial = Integer.valueOf(LastAccount.substring(LastAccount.length()-2));
            Serial += 1;
            if(Serial < 10){
                NewSerial = "0"+String.valueOf(Serial);
            }else{
                NewSerial = String.valueOf(Serial);
            }
            Account += NewSerial;
        }

        user.setAccount(Account);
        user.setPassword(Account);
        User result = userRepository.saveAndFlush(user);
        if(result != null){
            return true;
        }else {
            return false;
        }
    }

    /**
     *
     * @param User 用户
     * @return
     */
    @PostMapping("/UserEdit")
    public Boolean UserEdit(@RequestBody User User){
        User result = userRepository.saveAndFlush(User);
        if(result != null){
            return true;
        }else {
            return false;
        }
    }

    /**
     *
     * @param userid 用户id
     * @return
     */
    @PostMapping("/UserDelete")
    public Boolean UserDelete(@RequestParam("userid") Integer userid){
        userRepository.deleteById(userid);
        userRepository.flush();
        return true;
    }

    /**
     * 课程管理
     * @return 所有课程信息
     */
    @PostMapping("/CourseSearch")
    public List<Course> CourseSearch(){
        courseRepository.flush();
        List<Course> CourseList = courseRepository.findAll();
        System.out.println("调用了一次查询");
        return CourseList;
    }

    /**
     *
     * @param params 传输数据
     * @return
     */
    @PostMapping("/CourseAdd")
    public Boolean CourseAdd(@RequestBody ModelCourseData params){
        Timetable timetable = new Timetable();
        Course course = new Course();

        //将参数数据赋给课程表
        for (List<Integer> item : params.getTiming()){
            switch (item.get(0)){
                case 1: timetable.setMon(tranString(timetable.getMon(), item.get(1)));break;
                case 2: timetable.setTue(tranString(timetable.getTue(), item.get(1)));break;
                case 3: timetable.setWed(tranString(timetable.getWed(), item.get(1)));break;
                case 4: timetable.setThu(tranString(timetable.getThu(), item.get(1)));break;
                case 5: timetable.setFri(tranString(timetable.getFri(), item.get(1)));break;
            }
        }
        timetable.setStart_time(params.getTimetable().get(0));
        timetable.setEnd_time(params.getTimetable().get(1));
        Timetable timetResult = timetableRepository.saveAndFlush(timetable);
        if(timetResult != null){
            course.setName(params.getName());
            course.setContent(params.getContent());
            course.setDepartment(new Department(params.getDepartment()));
            course.setTeacher(new User(params.getTeacher()));
            course.setPublish(params.getPublish());
            course.setTimetable(timetResult);

            Course courseResult = courseRepository.saveAndFlush(course);
            if(courseResult != null){
                for (Integer item : params.getClasses()){
                    Courselect courselect = new Courselect();
                    courselect.setCourse(courseResult);
                    courselect.setClasses(new Classes(item));
                    courselectRepository.saveAndFlush(courselect);
                }
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     *
     * @param params 课程信息
     * @return
     */
    @PostMapping("/CourseEdit")
    public Boolean CourseEdit(@RequestBody ModelCourseData params){
        Timetable timetable = new Timetable();
        Course course = new Course();

        //将参数数据赋给课程表
        for (List<Integer> item : params.getTiming()){
            switch (item.get(0)){
                case 1: timetable.setMon(tranString(timetable.getMon(), item.get(1)));break;
                case 2: timetable.setTue(tranString(timetable.getTue(), item.get(1)));break;
                case 3: timetable.setWed(tranString(timetable.getWed(), item.get(1)));break;
                case 4: timetable.setThu(tranString(timetable.getThu(), item.get(1)));break;
                case 5: timetable.setFri(tranString(timetable.getFri(), item.get(1)));break;
            }
        }
        timetable.setTimetable_id(params.getTimetable_id());
        timetable.setStart_time(params.getTimetable().get(0));
        timetable.setEnd_time(params.getTimetable().get(1));
        Timetable timetResult = timetableRepository.saveAndFlush(timetable);
        if(timetResult != null){
            course.setCourse_id(params.getCourse_id());
            course.setName(params.getName());
            course.setContent(params.getContent());
            course.setDepartment(new Department(params.getDepartment()));
            course.setTeacher(new User(params.getTeacher()));
            course.setPublish(params.getPublish());
            course.setTimetable(timetResult);

            Course courseResult = courseRepository.saveAndFlush(course);
            if(courseResult != null){
                List<Courselect> Allcourselect = courselectRepository.findByCourse(courseResult.getCourse_id());
                //删除原有的不需要的旧数据
                for (Courselect cs : Allcourselect) {
                    boolean flag = true;
                    for (Integer item : params.getClasses()) {
                        if (cs.getClasses().getClasses_id() == item) {
                            flag = false;
                        }
                    }
                    if(flag){
                        courselectRepository.deleteById(cs.getCourselect_id());
                    }
                }
                //添加新修改的新数据
                for (Integer item : params.getClasses()){
                    boolean flag = true;
                    for (Courselect cs : Allcourselect){
                        if(cs.getClasses().getClasses_id() == item){
                            flag = false;
                        }
                    }
                    if(flag){
                        Courselect courselect = new Courselect();
                        courselect.setCourse(courseResult);
                        courselect.setClasses(new Classes(item));
                        courselectRepository.saveAndFlush(courselect);
                    }
                }
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     *
     * @param courseid 课程id
     * @return
     */
    @PostMapping("/CourseDelete")
    public Boolean CourseDelete(@RequestParam("courseid") Integer courseid){
        courseRepository.deleteById(courseid);
        courseRepository.flush();
        return true;
    }

    public String tranString(String str, Integer i){
        if(str == null){
            return ""+i;
        }else{
            return str+i;
        }
    }


}
