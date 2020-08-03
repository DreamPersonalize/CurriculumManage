<template>
  <div>
    <!-- 公告 -->
    <notice-comp :noticeinfo="noticeinfo"></notice-comp>
    <!-- 签到及可视化 -->
    <el-row type="flex" class="row-bg" justify="center">
      <el-col :span="7" style="display:flex; justify-content:center; align-items:center;">
        <el-button
          type="primary"
          :style="{width:SignBtn.width, height:SignBtn.height, backgroundColor: SignBtn.BackgroundColor}"
          :disabled="SignBtn.Disabled"
          circle
          @click="Sign"
        >
          {{SignStatu}}
          <br />
          <br />
          {{SignTime}}
        </el-button>
      </el-col>
      <el-col :span="10">
        <el-card class="box-card">
          <div id="SignChart" :style="{ width: '500px', height: '250px' }"></div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 签到详情 -->
    <table-comp :tableHead="tableHead" :tableData="tableData">
      <template v-slot:search>
        <el-input placeholder="请键入内容进行搜索" v-model="search" clearable></el-input>
      </template>
      <template v-slot:other>
        <el-table-column prop="signtype" align="right" sortable :key="Math.random()"></el-table-column>
      </template>
    </table-comp>
  </div>
</template>

<script>
import NoticeComp from "@/components/NoticeComp.vue";
import TableComp from "@/components/TableComp.vue";
import axios from "axios";

export default {
  name: "StudentSign",
  // mounted() {
  //   this.drawLine();
  // },
  props: {
    userinfo: Object,
    course_id: Number
  },
  data: function() {
    return {
      SignStatu: "",
      SignTime: "",
      LessonToal: 0,
      noticeinfo: {},
      database: [], //数据库数据
      drawData: [], //echart图形数据
      fullscreenLoading: true,
      SignBtn: {
        width: "200px",
        height: "200px",
        BackgroundColor: "#409EFF",
        Disabled: true
      },
      tableHead: [
        {
          prop: "coursetime",
          label: "课程日期"
        },
        {
          prop: "lesson",
          label: "课程节数"
        },
        {
          prop: "signtime",
          label: "签到时间"
        }
      ]
    };
  },
  computed: {
    tableData: function() {
      //表格数据
      let Database = this.database;
      console.log("database");
      console.log(Database);
      let Search = this.search;
      let arr = [];

      //进行数据修改
      for (let i = 0; i < Database.length; i++) {
        let tmp = Object.assign({}, Database[i]);
        tmp["coursetime"] = String(tmp.coursetime).substr(0, 10);
        tmp["signtime"] = String(tmp.signtime)
          .substr(0, 19)
          .split("T")[1]; //截取时间字符串
        tmp["signtype"] = tmp.signtype.name;
        arr.push(tmp);
      }

      //万能搜索
      if (Search) {
        return arr.filter(data => {
          return Object.keys(data).some(key => {
            return (
              String(data[key])
                .toLowerCase()
                .indexOf(Search) > -1
            );
          });
        });
      }
      return arr;
    }
  },
  methods: {
    ComputeDrawData() {
      let Database = this.database;
      console.log(Database);
      let DrawData = [
        { value: 0, name: "签到" },
        { value: 0, name: "补签" },
        { value: 0, name: "请假" },
        { value: 0, name: "未签到" }
      ];

      //进行数据统计
      for (let i = 0; i < Database.length; i++) {
        let tmp = Object.assign({}, Database[i]);
        if (tmp.signtype.signtype_id == 1) {
          DrawData[0].value++;
        } else if (tmp.signtype.signtype_id == 2) {
          DrawData[1].value++;
        } else if (tmp.signtype.signtype_id == 3) {
          DrawData[2].value++;
        }
      }
      DrawData[3].value =
        this.LessonToal -
        (DrawData[0].value + DrawData[0].value + DrawData[0].value);
      console.log(DrawData);
      this.drawData = DrawData;
    },
    // 绘制图形表格
    drawLine() {
      console.log("------------------------draw");
      console.log(this.drawData);
      var echarts = require("echarts");
      var SignChart = echarts.init(document.getElementById("SignChart"));
      SignChart.setOption({
        title: {
          text: "签到统计",
          left: "left"
        },
        tooltip: {
          trigger: "item",
          formatter: "{b} : {c}次 ({d}%)"
        },
        legend: {
          orient: "vertical",
          left: "right",
          data: ["签到", "补签", "请假", "未签到"]
        },
        series: [
          {
            name: "签到统计",
            type: "pie",
            radius: "60%",
            center: ["50%", "40%"],
            // data: [
            //   { value: 2, name: "签到" },
            //   { value: 0, name: "补签" },
            //   { value: 0, name: "请假" },
            //   { value: 88, name: "未签到" }
            // ],
            data: this.drawData,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: "rgba(0, 0, 0, 0.5)"
              }
            }
          }
        ]
      });
    },

    //时间格式转换
    GetDate(date) {
      var year = date.getFullYear();
      var month = date.getMonth() + 1;
      var day = date.getDate();
      if (month < 10) {
        month = "0" + month;
      }
      if (day < 10) {
        day = "0" + day;
      }
      var nowDate = year + "-" + month + "-" + day;
      return nowDate;
    },

    //根据时间判断当前是第几节课
    TimeToLesson(date) {
      let hour = date.getHours();
      let lesson = 0;
      if (hour > 7 && hour < 10) {
        lesson = 1;
      } else if (hour > 9 && hour < 12) {
        lesson = 2;
      } else if (hour > 13 && hour < 16) {
        lesson = 3;
      } else if (hour > 15 && hour < 18) {
        lesson = 4;
      } else if (hour > 17 && hour < 20) {
        lesson = 5;
      } else if (hour > 19 && hour < 22) {
        lesson = 6;
      }
      return lesson;
    },

    Sign() {
      let date = new Date();
      let coursetime = this.GetDate(date);
      let lesson = this.TimeToLesson(date);
      let params = {
        course: {
          course_id: this.course_id
        },
        classes: this.userinfo.classes,
        user: this.userinfo,
        coursetime: coursetime,
        signtime: date,
        lesson: lesson,
        signtype: {
          signtype_id: 1
        }
      };
      this.PostData("Sign", params, () => {
        this.SignStatu = "已签到";
        this.SignTime = this.GetDate();
        this.SignBtn.BackgroundColor = "#67C23A";
        this.SignBtn.Disabled = true;
      });
    },
    /**
     * 请求后台
     * option：操作类型
     * params：请求参数
     *
     */
    PostData(option, params, callback) {
      console.log("option");
      console.log(option);
      let url = "http://localhost:8181/Student/";
      switch (option) {
        case "SignCheck":
          url += "SignCheck";
          break;
        case "Notice":
          url = "http://localhost:8181/Util/NewCourseNotice";
          break;
        case "Sign":
          url += "Sign";
          break;
        case "SignHistory":
          url += "SignHistory";
          break;
        case "GetLessonTotal":
          url = "http://localhost:8181/Util/GetLessonTotal";
          break;
      }
      var that = this;
      axios.post(url, params).then(function(resp) {
        if (resp.status == 200) {
          console.log("resp");
          console.log(resp);
          if (option == "SignCheck") {
            switch (resp.data) {
              case "NoLesson":
                that.SignStatu = "当前不需要签到";
                that.SignBtn.BackgroundColor = "#666666";
                that.SignBtn.Disabled = true;
                break;
              case "NoSign":
                that.SignStatu = "点击签到";
                that.SignBtn.Disabled = false;
                break;
              case "Sign":
                that.SignStatu = "已签到";
                that.SignBtn.BackgroundColor = "#67C23A";
                that.SignBtn.Disabled = true;
                break;
            }
          } else if (option == "Notice") {
            that.noticeinfo = resp.data;
          } else if (option == "Sign") {
            if (resp.data) {
              that.$message({
                showClose: true,
                type: "success",
                message: "签到成功!"
              });
            } else {
              that.$message({
                showClose: true,
                type: "error",
                message: "签到失败!"
              });
            }
          } else if (option == "SignHistory") {
            that.database = resp.data;
          } else if (option == "GetLessonTotal") {
            that.LessonToal = resp.data;
          } else {
            that.$message({
              showClose: true,
              type: "error",
              message: "系统错误!"
            });
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
    NoticeComp,
    TableComp
  },
  created() {
    const that = this;
    let params = new FormData();
    params.append("course_id", that.course_id);
    params.append("user_id", that.userinfo.user_id);
    that.PostData("SignCheck", params, () => {
      that.PostData("SignHistory", params, () => {
        that.PostData("GetLessonTotal", params, () => {
          that.ComputeDrawData();
          that.drawLine();
        });
      });
    });
    that.PostData("Notice", params, () => {});
  }
};
</script>

<style scoped>
.el-row {
  margin-bottom: 10px;
}
</style>
