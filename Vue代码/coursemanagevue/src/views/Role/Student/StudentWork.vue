<template>
  <div>
    <el-row type="flex" class="row-bg" justify="center">
      <!-- 提交统计 -->
      <el-col :span="9">
        <el-card>
          <div id="WorknumChart" :style="{ width: '400px', height: '300px' }"></div>
        </el-card>
      </el-col>
      <!-- 成绩统计 -->
      <el-col :span="9">
        <el-card style="margin-left: 10px;">
          <div id="WorkscoreChart" :style="{ width: '400px', height: '300px' }"></div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 历史成绩折线图 -->
    <el-row type="flex" class="row-bg" justify="center">
      <el-col :span="18">
        <el-card>
          <div id="ScorehistoryChart" :style="{ width: '900px', height: '300px' }"></div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 作业详情 -->
    <table-comp :tableHead="tableHead" :tableData="tableData">
      <template v-slot:search>
        <el-input placeholder="请键入内容进行搜索" v-model="search" clearable></el-input>
      </template>
      <template v-slot:other>
        <el-table-column>
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-view" @click="ClickEdit(scope.row)"></el-button>
          </template>
        </el-table-column>
      </template>
    </table-comp>

    <!-- 编辑按钮对话框 -->
    <!-- <el-dialog title="作业查看" :visible.sync="EditDataPanel" width="30%" center>
      <el-form :model="form" :rules="rules" ref="EditForm">
        <el-form-item label="名称" prop="name" :label-width="formLabelWidth">
          <el-input v-model="form.name" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="概要" prop="content" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form.content" disabled></el-input>
        </el-form-item>
        <el-form-item label="开始时间" prop="timeStart" :label-width="formLabelWidth">
          <el-input v-model="form.start_time" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="截止时间" prop="timeEnd" :label-width="formLabelWidth">
          <el-input v-model="form.timeEnde" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="是否可补交" prop="status" :label-width="formLabelWidth">
          <el-switch v-model="form.status" active-color="#13ce66" inactive-color="#ff4949" disabled></el-switch>
        </el-form-item>
        <el-form-item label="已上传文件" :label-width="formLabelWidth">
          <el-button type="primary">已上传的文件，点击下载</el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="EditDataPanel = false">取 消</el-button>
        <el-button type="primary">确 定</el-button>
      </div>
    </el-dialog> -->

    <el-dialog title="作业详情" :visible.sync="EditDataPanel" width="30%" center>
      <el-form :model="form" :rules="rules" ref="PubEditForm">
        <el-form-item label="名称" prop="name" placeholder="请输入作业名称" :label-width="formLabelWidth">
          <el-input v-model="form.name" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="概要" prop="content" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form.content" disabled></el-input>
        </el-form-item>
        <el-form-item label="时间" prop="time" :label-width="formLabelWidth">
          <el-date-picker
            v-model="form.time"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd"
            style="width: 100%"
            disabled
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="补签" prop="statu" :label-width="formLabelWidth">
          <el-switch v-model="form.statu" disabled></el-switch>
        </el-form-item>
        <el-form-item label="作业文件" prop="name" :label-width="formLabelWidth">
          <el-input v-model="form.fileList" autocomplete="off" style="width:70%" disabled></el-input>
          <el-button icon="el-icon-download" @click="fileDown" type="success" plain></el-button>
        </el-form-item>
        <el-form-item label="上传文件" :label-width="formLabelWidth">
          <el-upload
            class="upload-demo"
            ref="upedit"
            action
            :limit="1"
            :http-request="PubEditData"
            :on-preview="fileDown"
            :on-exceed="handleExceed"
            :before-remove="remove"
            :file-list="form.fileList"
            :auto-upload="false"
          >
            <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="EditDataPanel = false">取 消</el-button>
        <el-button type="primary" @click="submitUpedit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import TableComp from "@/components/TableComp.vue";
import axios from "axios";

export default {
  name: "StudentWork",
  props: {
    userinfo: Object,
    course_id: Number
  },
  data: function() {
    return {
      search: "",
      database: [], //数据库数据
      taskpubList: [], //课程所有发布作业
      tasksubList: [], //个人所有提交的作业
      EditDataPanel: false,
      formLabelWidth: "120px",
      form: {
        tasksub_id: null,
        taskpub_id: null,
        name: "",
        content: "",
        start_time: "",
        end_time: "",
        fileList: [],
        subfile: [],
        statu: true,
        releasetime: "",
        time: ""
      },
      tableHead: [
        {
          prop: "name",
          label: "作业名"
        },
        {
          prop: "start_time",
          label: "开始时间"
        },
        {
          prop: "end_time",
          label: "结束时间"
        },
        {
          prop: "makeup",
          label: "是否可补交"
        },
        {
          prop: "submitStatu",
          label: "提交情况"
        },
        {
          prop: "achievement",
          label: "成绩"
        }
      ]
    };
  },
  computed: {
    tableData: function() {
      //表格数据
      let TaskpubList = this.taskpubList;
      let TasksubList = this.tasksubList;
      let Search = this.search;
      let arr = [];
      //进行数据修改
      for (let i = 0; i < TaskpubList.length; i++) {
        let tmp = Object.assign({}, TaskpubList[i]);
        tmp.start_time = String(tmp.start_time).substr(0, 10);
        tmp.end_time = String(tmp.end_time).substr(0, 10);
        tmp.releasetime = String(tmp.releasetime).substr(0, 10); //截取时间字符串
        if (tmp.makeup == 0) {
          tmp["statu"] = false;
          tmp.makeup = "否";
        } else {
          tmp["statu"] = true;
          tmp.makeup = "是";
        }
        tmp["time"] = [tmp.start_time, tmp.end_time];
        tmp["fileList"] = tmp.enclosure.name;
        tmp["subfile"] = [];
        tmp["submitStatu"] = "未提交";
        tmp["achievement"] = "不及格";
        for (let item in TasksubList) {
          if (TasksubList[item].task.taskpub_id == TaskpubList[i].taskpub_id) {
            tmp["submitStatu"] = "已提交";
            tmp["achievement"] = TasksubList[item].achievement.name;
            tmp["subfile"] = [{ name: TasksubList[item].enclosure.name }];
          }
        }
        console.log(tmp);
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
    },
    SearchParam: function() {
      let params = new FormData();
      params.append("course_id", this.course_id);
      params.append("user_id", this.userinfo.user_id);
      return params;
    },
    worknumData: function() {
      let TaskpubList = this.taskpubList;
      let TasksubList = this.tasksubList;
      let DrawData = [
        //echart提交图数据
        { value: 0, name: "已提交" },
        { value: 0, name: "未提交" }
      ];
      for (let i in TaskpubList) {
        let flag = true;
        for (let j in TasksubList) {
          if (TasksubList[j].task.taskpub_id == TaskpubList[i].taskpub_id) {
            flag = false;
          }
        }
        if (flag) {
          DrawData[1].value++;
        } else {
          DrawData[0].value++;
        }
      }
      console.log("worknumData", DrawData);
      return DrawData;
    },

    workscoreData: function() {
      let TaskpubList = this.taskpubList;
      let TasksubList = this.tasksubList;
      let DrawData = [
        //echart提交图数据
        { value: 0, name: "优" },
        { value: 0, name: "良" },
        { value: 0, name: "及格" },
        { value: 0, name: "不及格" }
      ];
      for (let i in TasksubList) {
        if (TasksubList[i].achievement.achievement_id == 1) {
          DrawData[0].value++;
        } else if (TasksubList[i].achievement.achievement_id == 2) {
          DrawData[1].value++;
        }
        if (TasksubList[i].achievement.achievement_id == 3) {
          DrawData[2].value++;
        }
      }
      DrawData[3].value =
        TaskpubList.length -
        (DrawData[0].value + DrawData[1].value + DrawData[2].value);
      console.log("workscoreData", DrawData);
      return DrawData;
    },
    scorehistoryData: function() {
      let TaskpubList = this.taskpubList;
      let TasksubList = this.tasksubList;
      let DrawData = {
        //echart提交图数据
        xData: [],
        yData: []
      };
      for (let j in TaskpubList) {
        DrawData.xData.push(TaskpubList[j].name);
        let score = "不及格";
        for (let i in TasksubList) {
          if (TasksubList[i].task.taskpub_id == TaskpubList[j].taskpub_id) {
            score = TasksubList[i].achievement.name;
          }
        }
        DrawData.yData.push(score);
      }
      console.log("scorehistoryData", DrawData);
      return DrawData;
    }
  },
  methods: {
    //清空表单
    CleanForm() {
      this.form = {
        tasksub_id: null,
        taskpub_id: null,
        name: "",
        content: "",
        start_time: "",
        end_time: "",
        fileList: [],
        subfile: [],
        statu: true,
        releasetime: ""
      };
    },

    drawLine() {
      var echarts = require("echarts");
      var WorknumChart = echarts.init(document.getElementById("WorknumChart"));
      var WorkscoreChart = echarts.init(
        document.getElementById("WorkscoreChart")
      );
      var ScorehistoryChart = echarts.init(
        document.getElementById("ScorehistoryChart")
      );
      WorknumChart.setOption({
        title: {
          text: "提交统计",
          left: "left"
        },
        tooltip: {
          trigger: "item",
          formatter: "{b} : {c}次 ({d}%)"
        },
        legend: {
          orient: "vertical",
          left: "right",
          data: ["已提交", "未提交"]
        },
        series: [
          {
            name: "提交统计",
            type: "pie",
            radius: "60%",
            center: ["50%", "50%"],
            data: this.worknumData,
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
      WorkscoreChart.setOption({
        title: {
          text: "成绩统计",
          left: "right"
        },
        tooltip: {
          trigger: "item",
          formatter: "{b} : {c}次 ({d}%)"
        },
        legend: {
          orient: "vertical",
          left: "left",
          data: ["优", "良", "及格", "不及格"]
        },
        series: [
          {
            name: "成绩统计",
            type: "pie",
            radius: "60%",
            center: ["60%", "50%"],
            data: this.workscoreData,
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
      ScorehistoryChart.setOption({
        title: {
          text: "历史成绩情况",
          left: "center"
        },
        tooltip: {
          trigger: "axis",
          formatter: "作业名称：{b}<br/>作业成绩：{c}"
        },
        xAxis: {
          type: "category",
          boundaryGap: false,
          data: this.scorehistoryData.xData
        },
        yAxis: {
          type: "category",
          boundaryGap: false,
          data: ["不及格", "及格", "良", "优"]
        },
        series: [
          {
            name: "成绩",
            type: "line",
            stack: "总量",
            data: this.scorehistoryData.yData
          }
        ]
      });
    },

    //获取当前时间
    GetDate() {
      var date = new Date();
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

    //文件数量限制
    handleExceed() {
      this.$message.warning(`当前限制上传 1 个文件`);
    },
    //点击文件列表时进行下载
    fileDown() {
      let fileName = this.form.fileList;
      let params = new FormData();
      params.append("fileName", fileName);
      params.append("scene", "task");
      this.PostData("Download", params, () => {});
    },
    //文件移除
    beforeRemove(file) {
      console.log(file, this.fileList);
      return this.$confirm(`确定移除 ${file.name}？`);
    },

    //资源上传确认
    submitUpload() {
      this.$refs.upload.submit();
    },

    ClickEdit(Row) {
      this.EditDataPanel = true;
      this.form = Object.assign({}, Row);
    },

    //修改数据方法
    PubEditData(params) {
      var RowData = this.rowData; //获取当前行数据
      console.log(RowData);
      this.$confirm("是否修改此信息?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          //修改数据
          console.log(this.form);
          let form = this.form;
          let file = params.file;
          let formData = {};
          let AddParams = new FormData();
          formData["taskpub_id"] = form.taskpub_id;
          formData["course"] = {
            course_id: this.course_id
          };
          formData["name"] = form.name;
          formData["content"] = form.content;
          formData["user"] = this.userinfo;
          formData["start_time"] = form.time[0];
          formData["end_time"] = form.time[1];
          formData["releasetime"] = new Date();
          if (form.statu) {
            formData["makeup"] = 1;
          } else {
            formData["makeup"] = 0;
          }
          AddParams.append(
            "formData",
            new Blob([JSON.stringify(formData)], { type: "application/json" })
          );
          AddParams.append("file", file);
          this.PostData("Edit", AddParams, () => {
            this.PubEditDataPanel = false;
            this.loading = true;
            this.PostData("Search", this.SearchParam, () => {
              this.loading = false;
            });
          });
        })
        .catch(() => {
          this.PubEditDataPanel = false;
          this.$message({
            showClose: true,
            type: "info",
            message: "已取消修改"
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
      console.log(option);
      console.log(params);
      let distr = "";
      let config = {};
      let url = "http://localhost:8181/Student/Tasksub"; //请求数据库地址
      switch (option) {
        case "AllTaskpub":
          url = "http://localhost:8181/Util/AllTaskpub";
          break;
        case "Search":
          url += "Search";
          break;
        case "Delete":
          distr = "删除";
          break;
        case "Download":
          url = "http://localhost:8181/Util/ResourceDownload";
          config = { responseType: "blob" };
          break;
      }
      var that = this;
      axios.post(url, params, config).then(function(resp) {
        console.log("option:" + option + " resp:", resp);
        if (resp.status == 200) {
          if (option == "AllTaskpub") {
            that.taskpubList = resp.data;
          } else if (option == "Search") {
            that.tasksubList = resp.data;
          } else if (option == "Download") {
            let blob = resp.data;
            let url = window.URL.createObjectURL(blob);
            let el = document.createElement("a");
            el.href = url;
            el.download = that.form.fileName;
            el.hidden = true;
            document.body.appendChild(el);
            el.click();
            document.body.removeChild(el);
          } else if (resp.data) {
            that.$message({
              showClose: true,
              type: "success",
              message: distr + "成功!"
            });
          } else {
            that.$message({
              showClose: true,
              type: "error",
              message: distr + "失败!"
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
    TableComp
  },
  created() {
    //查询所有作业信息--后台方法
    this.PostData("AllTaskpub", this.SearchParam, () => {
      this.PostData("Search", this.SearchParam, () => {
        this.drawLine();
      });
    });
  }
};
</script>

<style scoped>
.el-card {
  margin-top: 10px;
}
</style>
