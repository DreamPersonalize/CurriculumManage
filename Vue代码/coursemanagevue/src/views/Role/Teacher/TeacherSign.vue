<template>
  <div>
    <el-row el-row type="flex" class="row-bg" justify="center">
      <el-col :span="18">
        <el-card>
          <el-row el-row type="flex" class="row-bg" justify="space-around">
            <el-col :span="10">
              <div id="SigntodayChart" :style="{ width: '100%', height: '250px' }"></div>
              <el-select
                v-model="classSelect"
                placeholder="班级选择"
                style="width:50%;margin-top:50px;"
              >
                <el-option
                  v-for="item in classesList"
                  :key="item.classes_id"
                  :label="item.name"
                  :value="item.classes_id"
                ></el-option>
              </el-select>
              <el-select
                v-model="lessonSelect"
                placeholder="课程选择"
                style="width:50%;margin-top:50px;"
              >
                <el-option
                  v-for="(item, index) in lessons"
                  :key="index"
                  :label="item"
                  :value="item"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="13">
              <el-card>
                <el-container style="height: 300px;">
                  <el-main>
                    <el-input v-model="search" width="100px" placeholder="输入关键字搜索" />
                    <el-table :data="TodayTableData" stripe style="width: 100%">
                      <el-table-column type="index"></el-table-column>
                      <el-table-column prop="account" label="学号"></el-table-column>
                      <el-table-column prop="name" label="姓名"></el-table-column>
                      <el-table-column width="110px;">
                        <template slot-scope="scope">
                          <el-dropdown
                            size="mini"
                            split-button
                            type="primary"
                            @command="handleCommand(scope.row)"
                            @click="SupplySign(scope.row)"
                          >
                            补签
                            <el-dropdown-menu slot="dropdown">
                              <el-dropdown-item>请假</el-dropdown-item>
                            </el-dropdown-menu>
                          </el-dropdown>
                        </template>
                      </el-table-column>
                    </el-table>
                  </el-main>
                </el-container>
              </el-card>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
    <el-row el-row type="flex" class="row-bg" justify="center">
      <el-col :span="18">
        <el-card>
          <div id="SignhistoryChart" :style="{ width: '900px', height: '300px' }"></div>
        </el-card>
      </el-col>
    </el-row>
    <table-comp :tableHead="tableHead" :tableData="HistoryTableData">
      <template v-slot:search>
        <el-input placeholder="请键入内容进行搜索" v-model="searchHistory" clearable></el-input>
      </template>
    </table-comp>
  </div>
</template>

<script>
import TableComp from "@/components/TableComp.vue";
import axios from "axios";

export default {
  name: "TeacherSign",
  props: {
    userinfo: Object,
    course_id: Number
  },
  data: function() {
    return {
      lessons: "",
      classesList: [], //所有班级
      classesSign: [], //班级历史签到信息
      classesStudentList: [], //班级所有学生列表
      classesSignStudent: [],
      classSelect: "", //当前班级选择
      lessonSelect: "", //当前课程节数选择
      searchToday: "", //今日未签到人员搜索
      searchHistory: "", //历史签到信息搜索
      totalLesson: "", //折线图原始数据
      statisticsTodayData: [
        //echart扇形图数据
        { value: 0, name: "签到" },
        { value: 0, name: "未签到" },
        { value: 0, name: "请假" },
        { value: 0, name: "补签" }
      ],
      statisticsHistoryData: {
        xData: [],
        yData: []
      }, //echart折线图数据
      tableHead: [
        {
          prop: "classes",
          label: "班级"
        },
        {
          prop: "account",
          label: "学号"
        },
        {
          prop: "name",
          label: "姓名"
        },
        {
          prop: "signtime",
          label: "签到时间"
        },
        {
          prop: "signtype",
          label: "签到状态"
        }
      ]
    };
  },
  computed: {
    TodayTableData: function() {
      // let Database = this.database;
      let Search = this.searchToday;
      let arr = [];
      let ClassesStudentList = this.classesStudentList;
      let ClassesSignStudent = this.classesSignStudent;
      for (let i in ClassesStudentList) {
        let flag = true;
        for (let j in ClassesSignStudent) {
          if (
            ClassesStudentList[i].user_id == ClassesSignStudent[j].user.user_id
          ) {
            flag = false;
          }
        }
        if (flag) {
          arr.push(ClassesSignStudent[i]);
        }
      }

      //进行数据修改
      // for (let i = 0; i < Database.length; i++) {
      //   let tmp = Object.assign({}, Database[i]);
      //   tmp.releasetime = String(tmp.releasetime).substr(0, 10); //截取时间字符串
      //   arr.push(tmp)
      // }

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
    },

    HistoryTableData: function() {
      //表格数据
      let ClassesSign = this.classesSign;
      let Search = this.searchHistory;
      let arr = [];
      //进行数据修改
      for (let i = 0; i < ClassesSign.length; i++) {
        let tmp = Object.assign({}, ClassesSign[i]);
        let showdata = {
          classes: tmp.classes.name,
          account: tmp.user.account,
          name: tmp.user.username,
          signtime: String(tmp.signtime).substr(0, 10),
          signtype: tmp.signtype.name
        };
        arr.push(showdata);
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
    },
    SearchParam: function() {
      let params = new FormData();
      params.append("course_id", this.course_id);
      return params;
    }
  },

  watch: {
    classSelect: function(val) {
      console.log("班级改变了-----------------------" + val);
      let params = new FormData();
      params.append("course_id", this.course_id);
      params.append("classes_id", val);
      if (this.lessons != "") {
        params.append("lesson", Number(this.lessonSelect));
        this.PostData("ClassesAllStudent", params, () => {
          this.PostData("ClassesSignStudent", params, () => {
            this.statisticsToday();
            this.drawLine();
          });
        });
      }

      this.PostData("ClassesSign", params, () => {
        this.PostData("TotalLesson", params, () => {
          this.statisticsHistory();
          this.drawLine();
        });
      });
    },
    lessonSelect: function(val) {
      let params = new FormData();

      params.append("course_id", this.course_id);
      params.append("classes_id", this.classes_id);
      params.append("lesson", Number(val));
        this.PostData("ClassesAllStudent", params, () => {
          this.PostData("ClassesSignStudent", params, () => {
            this.statisticsToday();
            this.drawLine();
          });
        });
    }
  },
  methods: {
    statisticsToday() {
      let ClassesStudentList = this.classesStudentList; //班级所有学生列表
      let ClassesSignStudent = this.classesSignStudent; //班级已进行签到的学生
      let DrawData = [
        { value: 0, name: "签到" },
        { value: 0, name: "补签" },
        { value: 0, name: "请假" },
        { value: 0, name: "未签到" }
      ];
      for (let student in ClassesSignStudent) {
        if (student.lesson == this.lessonSelect) {
          if (student.signtype == 1) {
            DrawData[0].value++;
          } else if (student.signtype == 2) {
            DrawData[1].value++;
          } else if (student.signtype == 3) {
            DrawData[2].value++;
          }
        }
      }
      DrawData[3].value = ClassesStudentList.length - ClassesSignStudent.length;
      this.statisticsTodayData = DrawData;
    },
    statisticsHistory() {
      let TotalLesson = this.totalLesson;
      let statisticsHistoryData = {
        xData: [],
        yData: []
      };
      for (let i = 0; i < TotalLesson.length; i++) {
        let coursetime = String(TotalLesson[i].coursetime).substr(0, 10);
        statisticsHistoryData.xData.push(coursetime);
        statisticsHistoryData.yData.push(TotalLesson[i].count);
      }
      console.log("historyEdit", statisticsHistoryData);
      this.StatisticsHistoryData = statisticsHistoryData;
    },

    getClassesName(classes_id) {
      let ClassesList = this.classesList;
      for (let item in ClassesList) {
        console.log(item);
        if (ClassesList[item].classes_id == classes_id) {
          return ClassesList[item].name;
        }
      }
      return "";
    },

    drawLine() {
      var echarts = require("echarts");
      var SigntodayChart = echarts.init(
        document.getElementById("SigntodayChart")
      );
      var SignhistoryChart = echarts.init(
        document.getElementById("SignhistoryChart")
      );
      SigntodayChart.setOption({
        title: {
          text: "今日签到统计",
          left: "left"
        },
        tooltip: {
          trigger: "item",
          formatter: "{b} : {c}次 ({d}%)"
        },
        legend: {
          orient: "vertical",
          left: "right",
          data: ["签到", "未签到", "请假", "补签"]
        },
        series: [
          {
            name: "签到统计",
            type: "pie",
            radius: "60%",
            center: ["50%", "60%"],
            data: this.statisticsTodayData,
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

      SignhistoryChart.setOption({
        title: {
          text: "历史签到情况",
          left: "center"
        },
        tooltip: {
          trigger: "axis",
          formatter:
            "班级：" +
            this.getClassesName(this.classSelect) +
            "<br/>签到人数：{c}<br/>{a}{b}"
        },
        xAxis: {
          type: "category",
          boundaryGap: false,
          data: this.StatisticsHistoryData.xData
        },
        yAxis: {
          type: "value",
          boundaryGap: false
        },
        series: [
          {
            name: "课程时间：",
            type: "line",
            stack: "总量",
            data: this.StatisticsHistoryData.yData
          }
        ]
      });
    },

    handleCommand(RowData) {
      // console.log(Row)
      this.SupplySign(RowData, "ill");
    },

    SupplySign(RowData, SignType) {
      console.log(RowData);
      let msg = "";
      let signType = 2;
      if (SignType == "ill") {
        msg = "病假";
        signType = 3;
      }

      this.$confirm("是否进行" + msg + "补签?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let params = {
            course: {
              course_id: this.course_id
            },
            classes: {
              classes_id: Number(this.classes_id)
            },
            user: {
              user_id: this.userinfo.user_id
            },
            coursetime: new Date(),
            lesson: this.lessonSelect,
            signtype: signType
          };
          this.PostData("Sign", params, () => {
            let searchParam = new FormData();
            searchParam.append("course_id", this.course_id);
            searchParam.append("classes_id", this.classSelect);
            searchParam.append("lesson", Number(this.lessonSelect));
            this.PostData("ClassesAllStudent", params, () => {
              this.PostData("ClassesSignStudent", params, () => {
                this.statisticsToday();
                this.drawLine();
              });
            });
          });
        })
        .catch(() => {
          this.$message({
            showClose: true,
            type: "info",
            message: "已取消补签"
          });
        });
    },
    /**
     * 请求后台
     * option：操作类型
     * params：请求参数
     *
     */
    PostData(option, params, callback) {
      let url = "http://localhost:8181/Teacher/Sign" + String(option); //请求数据库地址
      var that = this;
      axios.post(url, params).then(function(resp) {
        console.log("option:" + option + " resp:", resp);
        if (resp.status == 200) {
          if (String(option) == "Search") {
            that.database = resp.data;
          } else if (option == "Check") {
            if (resp.data == "NoLesson") {
              that.lessons = "";
              that.$message({
                showClose: true,
                type: "warning",
                message: "今日无需签到！"
              });
            } else {
              let arr = [];
              let Lessons = String(resp.data).split("");
              for (let item in Lessons) {
                arr.push(Number(Lessons[item]));
              }
              that.lessons = Lessons;
            }
          } else if (option == "Classes") {
            that.classesList = resp.data;
            that.classSelect = resp.data[0].classes_id;
          } else if (option == "ClassesAllStudent") {
            that.classesStudentList = resp.data;
          } else if (option == "ClassesSign") {
            that.classesSign = resp.data;
          } else if (option == "TotalLesson") {
            that.totalLesson = resp.data;
            that.lessonSelect = resp.data[0];
          } else if (option == "Sign") {
            if (resp.data) {
              that.$message({
                showClose: true,
                type: "success",
                message: "补签成功!"
              });
            } else {
              that.$message({
                showClose: true,
                type: "error",
                message: "补签失败!"
              });
            }
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
    TableComp
  },
  created() {
    let params = new FormData();
    params.append("course_id", this.course_id);
    this.PostData("Check", params, () => {
      this.PostData("Classes", this.SearchParam, () => {
        // console.log(this.classesList[0].classes_id+"++++++++++++");
        params.append("classes_id", Number(this.classesList[0].classes_id));
        params.append("lesson", Number(this.lessonSelect));
        if (this.lessons != "") {
          this.PostData("ClassesAllStudent", params, () => {
            this.PostData("ClassesSignStudent", params, () => {
              this.statisticsToday();
              this.drawLine();
            });
          });
        }

        this.PostData("ClassesSign", params, () => {
          this.PostData("TotalLesson", params, () => {
            this.statisticsHistory();
            this.drawLine();
          });
        });
      });
    });
  }
};
</script>

<style scoped>
.el-row {
  margin-top: 10px;
}
</style>
