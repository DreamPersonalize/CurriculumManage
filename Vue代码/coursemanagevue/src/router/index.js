import Vue from "vue";
import VueRouter from "vue-router";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "index",
    component: () => import("../App.vue")
  },
  {
    path: "/test",
    name: "test",
    component: () => import("../views/test.vue")
  },
  
  // ----------我 是 一 个 莫 得 感 情 的 分 界 线------------------
  {
    path: "/login",
    name: "login",
    component: () => import("../views/Login.vue")
  },
  {
    path: "/course",
    name: "course",
    component: () => import("../views/Role/CourseChoose.vue")
  },
  {
    path: "/admin",
    name: "Admin",
    component: () => import("../views/Role/Admin/Administrator.vue"),
    children: [
      {
        path: "/admin/course",
        name: "课程管理",
        component: () => import("../views/Role/Admin/AdminCourseManage.vue")
      },
      {
        path: "/admin/classes",
        name: "班级管理",
        component: () => import("../views/Role/Admin/AdminClassesManage.vue")
      },
      {
        path: "/admin/teacher",
        name: "教师管理",
        component: () => import("../views/Role/Admin/AdminTeacherManage.vue")
      },
      {
        path: "/admin/student",
        name: "学生管理",
        component: () => import("../views/Role/Admin/AdminStudentManage.vue")
      },
      {
        path: "/admin/notice",
        name: "公告管理",
        component: () => import("../views/Role/Admin/AdminNoticeManage.vue")
      }
    ]
  },

  {
    path: "/student",
    name: "Student",
    component: () => import("../views/Role/Student/Student.vue"),
    children: [
      {
        path: "/student/course/msg",
        name: "课程信息",
        component: () => import("../views/Role/CourseMsg.vue")
      },
      {
        path: "/student/course/sign",
        name: "签到",
        component: () => import("../views/Role/Student/StudentSign.vue")
      },
      {
        path: "/student/course/work",
        name: "作业",
        component: () => import("../views/Role/Student/StudentWork.vue")
      },
      {
        path: "/student/course/resource",
        name: "资源",
        component: () => import("../views/Role/Resource.vue")
      }
    ]
  },
  {
    path: "/teacher",
    name: "Teacher",
    component: () => import("../views/Role/Teacher/Teacher.vue"),
    children: [
      {
        path: "/teacher/course/msg",
        name: "课程信息",
        component: () => import("../views/Role/CourseMsg.vue")
      },
      {
        path: "/teacher/course/sign",
        name: "签到",
        component: () => import("../views/Role/Teacher/TeacherSign.vue")
      },
      {
        path: "/teacher/course/work",
        name: "作业",
        component: () => import("../views/Role/Teacher/TeacherWork.vue")
      },
      {
        path: "/teacher/course/notice",
        name: "公告",
        component: () => import("../views/Role/Teacher/TeacherNotice.vue")
      },
      {
        path: "/teacher/course/resource",
        name: "资源",
        component: () => import("../views/Role/Resource.vue")
      }
    ]
  }
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes
});

export default router;
