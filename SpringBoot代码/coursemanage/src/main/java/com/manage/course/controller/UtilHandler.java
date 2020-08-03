package com.manage.course.controller;

import com.manage.course.entity.*;
import com.manage.course.repository.*;
import com.manage.course.util.UtilTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * author:zhang
 * date:2020年4月23日23:07:29
 * 通用工具类
 */

@RestController
@RequestMapping("/Util")
public class UtilHandler {
    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    TitleRepository titleRepository;

    @Autowired
    ClassesRepository classesRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CourselectRepository courselectRepository;

    @Autowired
    NoticeRepository noticeRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    EnclosureRepository enclosureRepository;

    @Autowired
    ResourceRepository resourceRepository;

    @Autowired
    TaskpubRepository taskpubRepository;
    /**
     * 获取所有院系id
     * @return
     */
    @PostMapping("/Department")
    public List<Department> DepartmentFindAll(){
        departmentRepository.flush();
        List<Department> NoticeList = departmentRepository.findAll();
        System.out.println();
        return NoticeList;
    }

    /**
     * 获取所有职称信息
     * @return
     */
    @PostMapping("/Title")
    public List<Title> TitleFindAll(){
        titleRepository.flush();
        List<Title> TitleList = titleRepository.findAll();
        System.out.println();
        return TitleList;
    }

    /**
     * 获取院系内所有班级
     * @param Departmentid 院系id
     * @return
     */
    @PostMapping("/DepartmentClasses")
    public List<Classes> ClassesFindByDepartment(@RequestParam("Departmentid")Integer Departmentid){
        classesRepository.flush();
        List<Classes> ClassesList = classesRepository.findByDepart(Departmentid);
        System.out.println();
        return ClassesList;
    }

    /**
     * 获取院系内所有教师信息
     * @param Departmentid 院系id
     * @return
     */
    @PostMapping("/DepartmentTeacher")
    public List<User> TeacherFindByDepartment(@RequestParam("Departmentid")Integer Departmentid){
        userRepository.flush();
        List<User> TeacherList = userRepository.findByPowerDepartment(2, Departmentid);
        System.out.println();
        return TeacherList;
    }

    /**
     * 获取课程所选班级id
     * @param Courseid 课程id
     * @return
     */
    @PostMapping("/CourseClasses")
    public List<Integer> ClassesFindByCourse(@RequestParam("Courseid")Integer Courseid){
        courselectRepository.flush();
        List<Courselect> courselects = courselectRepository.findByCourse(Courseid);
        List classesList = new ArrayList<Integer>();
        for(Courselect item : courselects){
            classesList.add(item.getClasses().getClasses_id());
        }
        System.out.println();
        return classesList;
    }

    /**
     * 获取学校公告
     * @return
     */
    @PostMapping("/NewSchoolNotice")
    public Notice NewSchoolNotice(){
        List<Notice> noticeList = noticeRepository.FindSchoolNotice(1);
        Notice newSchoolNotice = noticeList.get(noticeList.size()-1);
        return newSchoolNotice;
    }

    /**
     * 获取课程公告
     * @param course_id
     * @return
     */
    @PostMapping("/NewCourseNotice")
    public Notice NewCourseNotice(@RequestParam("course_id")Integer course_id){
        List<Notice> noticeList = noticeRepository.FindCourseNotice(course_id);
        Notice newCourseNotice = noticeList.get(noticeList.size()-1);
        return newCourseNotice;
    }

    /**
     * 获取用户所有课程信息
     * @param Userid
     * @return
     */
    @PostMapping("/GetAllCourse")
    public List<ModelCourChoose> CourseList(@RequestParam("Userid")Integer Userid){
        List<ModelCourChoose> mccList = new ArrayList<>();
        User user = userRepository.findById(Userid).get();
        if(user != null){
            if(user.getPower().getPower_id() == 2){ //教师身份
                List<Course> courseList = courseRepository.findByTeacher(user.getUser_id());
                mccList = CourseClassesTrans(courseList);
            }else if(user.getPower().getPower_id() == 3){   //学生身份
                List<Courselect> courselects = courselectRepository.findByClasses(user.getClasses().getClasses_id());
                if(courselects != null){
                    List<Course> courseList = new ArrayList<Course>();
                    for (Courselect cs : courselects){
                        courseList.add(cs.getCourse());
                    }
                    mccList = CourseClassesTrans(courseList);
                }
            }
        }
        return mccList;
    }

    /**
     * 根据课程id获取课程信息
     * @param Courseid 课程信息
     * @return
     */
    @PostMapping("/CourseMsg")
    public Course GetTimeTable(@RequestParam("Courseid")Integer Courseid){
        Course course = courseRepository.findById(Courseid).get();
        return course;
    }

    /**
     * 将课程列表转化为传输模板
     * @param courseList 课程列表
     * @return
     */
    public List<ModelCourChoose> CourseClassesTrans(List<Course> courseList){
        List<ModelCourChoose> mccList = new ArrayList<>();
        for(Course course : courseList){
            ModelCourChoose mcc = new ModelCourChoose();
            courselectRepository.flush();
            List<Courselect> courselects = courselectRepository.findByCourse(course.getCourse_id());
            String classesList = "";
            for(int i = 0; i< courselects.size(); i++){
                classesList += courselects.get(i).getClasses().getName();
                if(i != courselects.size()-1){
                    classesList += "、";
                }
            }
            mcc.setCourse_id(course.getCourse_id());
            mcc.setName(course.getName());
            mcc.setDepartment(course.getDepartment().getName());
            mcc.setTeacher(course.getTeacher().getUsername());
            mcc.setClasses(classesList);
            mccList.add(mcc);
        }

        System.out.println();
        return mccList;
    }

    /**
     * 获取资源列表
     * @param course_id 课程id
     * @return
     */
    @PostMapping("/ResourceSearch")
    public List<Resource> ResourceSearch(@Param("course_id")Integer course_id){
        List<Resource> resourceList = resourceRepository.FindAllByCourse(course_id);
//        if(State.equals("Pass")){
//            resourceList = resourceRepository.FindByPass(1);
//        }else {
//            resourceList = resourceRepository.FindByPass(0);
//        }

        return resourceList;
    }


    /**
     * 资源上传
     * @param file 上传的文件

     * @return
     */
    @PostMapping("/ResourceUpload")
    public Boolean ResourceUpload(@RequestPart("file") MultipartFile file, @RequestPart("formData")Resource formData){
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
                User user = formData.getUser();
                Resource re = formData;
                re.setEnclosure(enclosure);
                //学生上传的设置待审核状态
                if(user.getPower().getPower_id() == 2){  //教师上传资源
                    re.setState(1);
                }else{
                    re.setState(0);
                }
                Resource result = resourceRepository.saveAndFlush(re);
            }
        }else {
            return false;
        }
        return true;
    }

    /**
     * 资源下载
     * @param response
     * @param fileName 文件名
     * @param scene 下载场景
     * @throws UnsupportedEncodingException
     */
    @PostMapping("/ResourceDownload")
    public void ResourceDownload(HttpServletResponse response, @RequestParam("fileName")String fileName, @RequestParam("scene")String scene) throws UnsupportedEncodingException {
        String filePath = "D:/test";
        if(scene.equals("resource")){
            filePath += "/resource/";
        }else{
            filePath += "/task/";
        }
        File file = new File(filePath+fileName);
        if(file.exists()){
            System.out.println("文件存在");
            response.setContentType("application/octet-stream");
            response.setHeader("content-type", "application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "utf-8"));
            byte[] buffer = new byte[1024];
            //输出流
            try {
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1){
                    os.write(buffer);
                    i = bis.read(buffer);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            System.out.println("文件不存在");
        }
    }
    /**
     * 资源审核
     * @param Resource_id 资源id
     * @return
     */
    @PostMapping("/ResourceExamine")
    public Boolean ResourceExamine(@RequestParam("Resource_id")Integer Resource_id){
        Resource re = resourceRepository.findById(Resource_id).get();
        re.setState(1);
        Resource result = resourceRepository.saveAndFlush(re);
        if(result != null){
            return true;
        }

        return false;
    }


        /**
         * 资源修改
         * @param file 上传的文件
         * @param course 课程id
         * @param Resourse_id 资源id
         * @param name 文件名称
         * @param content 文件概述
         * @param user_id 用户id
         * @param releaseTime 提交时间
         * @return
         */
//    @PostMapping("/ResourceUpdate")
//    public Boolean ResourceUpdate(@RequestParam("file") MultipartFile file, @RequestParam("Resourse_id") Integer Resourse_id, @RequestParam("Course") Integer course, @RequestParam("Name") String name, @RequestParam("Content") String content, @RequestParam("user_id") Integer user_id, @RequestParam("ReleaseTime") Date releaseTime){
//        String filePath = "D:/test/resource/";
//        String fileName = file.getOriginalFilename();
//        //查重
//        List<Enclosure> allEnclosure = enclosureRepository.findAll();
//        for(Enclosure e : allEnclosure){
//            if(e.equals(fileName)){
//                return false;
//            }
//        }
//        //存储到本地
//        boolean store = UtilTool.uploading(file, filePath);
//        if(store){
//            Enclosure enclosure = enclosureRepository.saveAndFlush(new Enclosure(file.getOriginalFilename()));
//            if(enclosure != null){
//                User user = userRepository.findById(user_id).get();
//                Resource re = new Resource();
//                re.setResource_id(Resourse_id);
//                re.setCourse(new Course(course));
//                re.setName(name);
//                re.setContent(content);
//                re.setUser(user);
//                re.setEnclosure(enclosure);
//                re.setReleasetime(releaseTime);
//                //学生上传的设置待审核状态
//                if(user.getPower().getPower_id() == 2){  //教师上传资源
//                    re.setState(1);
//                }else{
//                    re.setState(0);
//                }
//                Resource result = resourceRepository.saveAndFlush(re);
//            }
//        }else {
//            return false;
//        }
//        return true;
//    }

    /**
     * 删除资源
     * @param Resource_id   资源id
     * @return
     */
    @PostMapping("/ResourceDelete")
    public boolean ResourceDelete(@RequestParam("Resource_id")Integer Resource_id){
        String filePath = "D:/test/resource/";
        String filename = resourceRepository.findById(Resource_id).get().getEnclosure().getName();
        Integer enclosure_id = resourceRepository.findById(Resource_id).get().getEnclosure().getEnclosure_id();
        //删除存储的文件
        if(UtilTool.FileDelete(filePath, filename)){
            resourceRepository.deleteById(Resource_id);
            enclosureRepository.deleteById(enclosure_id);
            resourceRepository.flush();
            return true;
        }
        return false;
    }

    /**
     * 获取作业发布列表
     * @param course_id 课程id
     * @return
     */
    @PostMapping("/AllTaskpub")
    public List<Taskpub> TaskpubSearch(@Param("course_id")Integer course_id){
        List<Taskpub> taskpubs = taskpubRepository.FindAllByCourse(course_id);
        return taskpubs;
    }

    /**
     * 获取课程总次数
     * @param course_id 课程id
     * @return
     */
    @PostMapping("/GetLessonTotal")
    public Integer GetLessonTotal(Integer course_id){
        int count = 0;
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
            System.out.println("开始运行");
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
                if(lessons != null || lessons != ""){
                    count += lessons.split("").length;
                }
            }
            starTime.setDate(starTime.getDate() + 1);
        }
        return count;
    }

}
