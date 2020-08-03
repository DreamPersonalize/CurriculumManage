<template>
  <el-table
    :data="TableData"
    border="small"
    :row-style="{height: '90px'}"
    style="width: 100%;"
    :cell-style="cellstyle"
  >
    <el-table-column prop="week" :label="Remark" align="center"></el-table-column>
    <el-table-column label="上午" align="center">
      <el-table-column label="一" align="center">
        <el-table-column prop="lessonOne" label="08:00-09:40" align="center"></el-table-column>
      </el-table-column>
      <el-table-column label="二" align="center">
        <el-table-column prop="lessonTwo" label="10:00-11:40" align="center"></el-table-column>
      </el-table-column>
    </el-table-column>
    <el-table-column label="下午" align="center">
      <el-table-column label="三" align="center">
        <el-table-column prop="lessonThree" label="14:00-15:40" align="center"></el-table-column>
      </el-table-column>
      <el-table-column label="四" align="center">
        <el-table-column prop="lessonFour" label="16:00-17:40" align="center"></el-table-column>
      </el-table-column>
    </el-table-column>
    <el-table-column label="下午" align="center">
      <el-table-column label="五" align="center">
        <el-table-column prop="lessonFive" label="18:00-19:40" align="center"></el-table-column>
      </el-table-column>
      <el-table-column label="六" align="center">
        <el-table-column prop="lessonSix" label="20:00-21:40" align="center"></el-table-column>
      </el-table-column>
    </el-table-column>
  </el-table>
</template>

<script>
export default {
  name: "TimetableComp",
  props: {
    TimeTable: Object
  },

  computed: {
    //转化后的表格数据
    TableData: function() {
      let timeTable = Object.assign({}, this.TimeTable);
      let arr = [];
      let sortarr = [];
      let weekinfo = "周一,周二,周三,周四,周五".split(",");
      for (let key in timeTable) {
        if (this.transWeek(key) != "") {
          let tmp = {};
          let lessons = String(timeTable[key]).split("");
          tmp["week"] = this.transWeek(key);
          if (lessons != null) {
            for (let item in lessons) {
              tmp[this.transLesson(lessons[item])] = "";
            }
          }
          arr.push(tmp);
        }
      }
      for (let i = 0; i < weekinfo.length; i++) {
        for (let j = 0; j < arr.length; j++) {
          if (arr[j].week == weekinfo[i]) {
            sortarr.push(arr[j]);
          }
        }
      }
      return sortarr;
    },
    //课程开始和结束时间
    Remark: function() {
      let remark =
        String(this.TimeTable.start_time).substr(0, 10) +
        "————" +
        String(this.TimeTable.end_time).substr(0, 10);
      return remark;
    }
  },
  methods: {
    //星期转化
    transWeek(str) {
      switch (str) {
        case "mon":
          return "周一";
        case "tue":
          return "周二";
        case "wed":
          return "周三";
        case "thu":
          return "周四";
        case "fri":
          return "周五";
        default:
          return "";
      }
    },
    //课程节数转化
    transLesson(num) {
      switch (num) {
        case "1":
          return "lessonOne";
        case "2":
          return "lessonTwo";
        case "3":
          return "lessonThree";
        case "4":
          return "lessonFour";
        case "5":
          return "lessonFive";
        case "6":
          return "lessonSix";
        default:
          return "";
      }
    },
    cellstyle({ row, column }) {
      let style = "";
      for (let key in row) {
        //筛选不为空的单元格
        if (key != "week" && row[key] != null && column.property == key) {
          style = "background: green; color: white";
        }
      }
      return style;
    }
  }
};
</script>