<template>
  <div  v-loading.fullscreen.lock="fullscreenLoading">
    <el-row type="flex" class="row-bg" justify="center">
      <el-col :span="18">
        <el-card>
          <el-row type="flex" class="row-bg" justify="end">
              <el-col :span="6"><strong>课程名称：</strong>{{ courseinfo.name }}</el-col>
              <el-col :span="5"><strong>授课教师：</strong>{{ courseinfo.teacher.username }}</el-col>
              <el-col :span="6"><strong>教师职称：</strong>{{ courseinfo.teacher.title.name }}</el-col>
              <el-col :span="7"><strong>所在院系：</strong>{{ courseinfo.department.name }}</el-col>
            </el-row>
        </el-card>
      </el-col>
    </el-row>
    <el-row type="flex" class="row-bg" justify="center">
      <el-col :span="18">
        <el-card>
          <timetable-comp :TimeTable="TimeTable"></timetable-comp>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import TimetableComp from "@/components/TimetableComp.vue";
import axios from "axios";

export default {
  name: "CourseMsg",
  props: {
    course_id: Number
  },
  data: function() {
    return {
      courseinfo: "",
      TimeTable: "",
      fullscreenLoading: true
    };
  },
  methods:{
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
        case "Search":
          url = "http://localhost:8181/Util/CourseMsg";
          break;
      }
      var that = this;
      axios.post(url, params).then(function(resp) {
        if (resp.status == 200) {
          console.log(resp)
          if (option == "Search") {
            that.courseinfo = resp.data;
            that.TimeTable = resp.data.timetable;
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
  components: {
    TimetableComp
  },
  created(){
    console.log("CourseMsg")
    console.log(this.course_id)
    let params = new FormData();
    params.append("Courseid", this.course_id)
    console.log(typeof this.course_id)
    this.PostData("Search", params, () => {
      this.fullscreenLoading = false;
    });
  }
};
</script>

