<template>
  <el-container class="bgimg" v-loading.fullscreen.lock="fullscreenLoading">
    <el-header>
      <el-row type="flex" class="row-bg" justify="end">
        <!-- 导航栏 -->
        <el-col :span="12">
          <el-menu
            :default-active="activeIndex"
            class="el-menu-demo"
            mode="horizontal"
            @select="handleSelect"
            background-color="#545c64"
            text-color="#fff"
            active-text-color="#ffd04b"
          >
            <el-menu-item index="/course">课程选择</el-menu-item>
          </el-menu>
        </el-col>
        <userinfo-comp :userinfo="userinfo"></userinfo-comp>
      </el-row>
    </el-header>
    <el-main>
      <notice-comp :noticeinfo="noticeinfo"></notice-comp>
      <el-row
        v-for="(item, index) in courses"
        :key="index"
        type="flex"
        class="row-bg"
        justify="center"
      >
        <el-col :span="18">
          <el-button @click="Jump(item.course_id)" class="buttonel" plain>
            <el-row type="flex" class="row-bg" justify="end">
              <el-col :span="5">{{ item.name }}</el-col>
              <el-col :span="7">{{ item.department }}</el-col>
              <el-col :span="5">{{ item.classes }}</el-col>
              <el-col :span="6">{{ item.teacher }}</el-col>
              <el-col :span="1">
                <i class="el-icon-arrow-right"></i>
              </el-col>
            </el-row>
          </el-button>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
import UserinfoComp from "@/components/UserinfoComp.vue";
import NoticeComp from "@/components/NoticeComp.vue";
import axios from "axios";

export default {
  name: "CourseChoose",
  data: function() {
    return {
      fullscreenLoading: true,
      activeIndex: "/course",
      userinfo: {},
      noticeinfo: {},
      courses: []
    };
  },
  components: {
    NoticeComp,
    UserinfoComp
  },
  methods: {
    Jump(course_id) {
      let user = this.$route.query.userinfo;
      if (user.power.power_id == 2) {
        //老师
        this.$router.push({
          path: "/teacher",
          query: {
            userinfo: user,
            course_id: course_id
          }
        });
      } else if (user.power.power_id == 3) {
        //学生
        this.$router.push({
          path: "/student",
          query: {
            userinfo: user,
            course_id: course_id
          }
        });
      } else {
        this.$message({
          showClose: true,
          type: "error",
          message: "系统错误"
        });
      }
    },
    /**
     * 请求后台
     * option：操作类型
     * params：请求参数
     *
     */
    PostData(option, params, callback) {
      console.log(option);
      console.log(params);
      let url = "";
      switch (option) {
        case "SearchN":
          url = "http://localhost:8181/Util/NewSchoolNotice";
          break;
        case "SearchC":
          url = "http://localhost:8181/Util/GetAllCourse";
          break;
      }
      var that = this;
      axios.post(url, params).then(function(resp) {
        if (resp.status == 200) {
          console.log(resp);
          if (option == "SearchN") {
            that.noticeinfo = resp.data;
          } else if (option == "SearchC") {
            that.courses = resp.data;
          }
        } else {
          that.$message({
            showClose: true,
            type: "error",
            message: "服务器错误!"
          });
        }
        callback();
      });
    }
  },
  created() {
    //获取用户信息
    const that = this;
    var user = this.$route.query.userinfo;
    that.userinfo = user;
    let params = new FormData();
    params.append("Userid", user.user_id);
    this.PostData("SearchC", params, () => {
      this.fullscreenLoading = false;
      this.PostData("SearchN", null, () => {});
    });
  }
};
</script>

<style>
.el-header {
  background-color: #545c64;
}

.bgimg {
  height: 768px;
  background-image: url("../../assets/bg5.png");
  background-size: 100% 100%;
}
.buttonel {
  width: 100%;
  height: 60px;
  margin-top: 10px;
}
</style>

