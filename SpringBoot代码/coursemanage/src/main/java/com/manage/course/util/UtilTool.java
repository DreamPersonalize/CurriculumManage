package com.manage.course.util;

import com.manage.course.entity.Taskpub;
import com.manage.course.entity.Timetable;
import com.manage.course.entity.User;
import com.manage.course.repository.CourseRepository;
import com.manage.course.repository.TaskpubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 工具类
 * 2020年5月11日16:20:53
 */
public class UtilTool {
    @Autowired
    static CourseRepository courseRepository;

    @Autowired
    static TaskpubRepository taskpubRepository;

    /**
     *文件上传
     * @param file 上传的文件
     * @return
     */
    public static Boolean uploading(MultipartFile file, String filepath){
//        String filepath = "D:/test/";
        File targetFile = new File(filepath);
        if(!targetFile.exists()){
            targetFile.mkdir();
        }
        try{
            FileOutputStream out = new FileOutputStream(filepath+file.getOriginalFilename());
            out.write(file.getBytes());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //文件删除
    public static Boolean FileDelete(String filepath, String filename){
        File file = new File(filepath+filename);
        if(file.exists()){
            if (file.delete()){
                return true;
            }
        }
        return false;
    }



    /**
     * 获取当前日期是星期几
     *
     * @param date
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date date) {
        String[] weekDays = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 获取所有作业信息
     * @param course_id
     * @return
     */
    public static List<Taskpub> GetTaskTotal(Integer course_id){
        return taskpubRepository.FindAllByCourse(course_id);
    }


}
