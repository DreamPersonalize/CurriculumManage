<template>
  <div>
    <!-- 课程信息卡片 -->
    <table-comp :tableHead="tableHead" :tableData="tableData" :loading="loading">
      <template v-slot:search>
        <el-input placeholder="请键入内容进行搜索" v-model="search" clearable></el-input>
      </template>
      <template v-slot:other>
        <el-table-column align="right">
          <template slot="header">
            <el-button type="success" icon="el-icon-plus" round @click="ClickAdd()"></el-button>
          </template>
          <template slot-scope="scope">
            <el-button-group>
              <el-button type="warning" icon="el-icon-edit" @click="ClickEdit(scope.row)"></el-button>
              <el-button type="danger" icon="el-icon-close" @click="DeleteData(scope.row)"></el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </template>
    </table-comp>

    <!-- 添加按钮弹窗 -->
    <el-dialog title="添加课程" :visible.sync="AddDataPanel" width="30%" center>
      <el-form :model="form" :rules="rules" ref="AddForm">
        <el-form-item label="名称" prop="name" :label-width="formLabelWidth">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="概要" prop="content" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form.content"></el-input>
        </el-form-item>
        <el-form-item label="院系" prop="department" :label-width="formLabelWidth">
          <el-select v-model="form.department" placeholder="请选择所在院系">
            <el-option
              v-for="item in departments"
              :key="item.department_id"
              :label="item.name"
              :value="item.department_id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="班级" prop="classes" :label-width="formLabelWidth">
          <el-select v-model="form.classes" multiple placeholder="请选择班级">
            <el-option
              v-for="item in allclasses"
              :key="item.user_id"
              :label="item.name"
              :value="item.classes_id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="教师" prop="teacher" :label-width="formLabelWidth">
          <el-select v-model="form.teacher" placeholder="请选择课程教授老师">
            <el-option
              v-for="item in allteachers"
              :key="item.user_id"
              :label="item.username"
              :value="item.user_id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="课 程 时 间" label-width="65%"></el-form-item>
        <el-form-item prop="timetable">
          <el-date-picker
            v-model="form.timetable"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy 年 MM 月 dd 日"
            value-format="yyyy-MM-dd"
          ></el-date-picker>
        </el-form-item>
        <el-form-item prop="timing">
          <el-cascader v-model="form.timing" :options="options" :props="props" style="width:100%" collapse-tags clearable></el-cascader>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="AddDataPanel = false">取 消</el-button>
        <el-button type="primary" @click="AddData('AddForm')">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 编辑按钮对话框 -->
    <el-dialog title="信息修改" :visible.sync="EditDataPanel" width="30%" center>
      <el-form :model="form" :rules="rules" ref="EditForm">
        <el-form-item label="名称" prop="name" :label-width="formLabelWidth">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="概要" prop="content" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form.content"></el-input>
        </el-form-item>
        <el-form-item label="院系" prop="department" :label-width="formLabelWidth">
          <el-select v-model="form.department" placeholder="请选择所在院系">
            <el-option
              v-for="item in departments"
              :key="item.department_id"
              :label="item.name"
              :value="item.department_id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="班级" prop="classes" :label-width="formLabelWidth">
          <el-select v-model="form.classes" multiple placeholder="请选择班级" clearable>
            <el-option
              v-for="item in allclasses"
              :key="item.user_id"
              :label="item.name"
              :value="item.classes_id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="教师" prop="teacher" :label-width="formLabelWidth">
          <el-select v-model="form.teacher" placeholder="请选择课程教授老师">
            <el-option
              v-for="item in allteachers"
              :key="item.user_id"
              :label="item.username"
              :value="item.user_id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="课 程 时 间" label-width="65%"></el-form-item>
        <el-form-item prop="timetable">
          <el-date-picker
            v-model="form.timetable"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy 年 MM 月 dd 日"
            value-format="yyyy-MM-dd"
          ></el-date-picker>
        </el-form-item>
        <el-form-item prop="timing">
          <el-cascader v-model="form.timing" :options="options" :props="props" style="width:100%" collapse-tags clearable></el-cascader>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="EditDataPanel = false">取 消</el-button>
        <el-button type="primary" @click="EditData('EditForm')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import TableComp from "@/components/TableComp.vue";
import axios from "axios";

export default {
  name: "AdminCourseManage",

  data: function() {
    return {
      search: "",
      database: [],
      departments: [],
      allclasses: [],
      selectedClasses: [],
      loading: true,
      props: { multiple: true },
      AddDataPanel: false, //添加面板
      EditDataPanel: false, //编辑面板
      formLabelWidth: "100px",
      form: {
        course_id: null,
        name: "",
        content: "",
        department: "",
        classes: "",
        teacher: "",
        publish: "",
        timetable: "",
        timing: ""
      },
      rules: {
        name: [
          { required: true, message: "课程名称不能为空", trigger: "blur" },
          {
            min: 2,
            max: 25,
            message: "长度在 2 到 25个字符之间",
            trigger: "blur"
          }
        ],
        department: [{ required: true, message: "请选择院系", trigger: "blur" }]
      },

      tableHead: [
        {
          prop: "name",
          label: "课程名称"
        },
        {
          prop: "teacherName",
          label: "授课教师"
        },
        {
          prop: "departmentName",
          label: "所属院系"
        },
        {
          prop: "publish",
          label: "发布日期"
        }
      ],
    };
  },
  computed: {
    SearchParam: function() {
      let params = new FormData();
      params.append("role", "student");
      return params;
    },
    alterDepartment: function() {
      return this.form.department;
    },
    
    //表格数据
    tableData: function() {
      let Database = this.database;
      console.log("database");
      console.log(Database);
      const Search = this.search;
      console.log(Search);
      let arr = [];
      // 进行数据修改
      for (let i = 0; i < Database.length; i++) {
        let tmp = Object.assign({}, Database[i]);
        let timingArr = []
        //将课程表数据库存储数据转换为
        for(let key in tmp.timetable){
            switch(key){
            case "mon":{
              let lesson = String(tmp.timetable[key]).split("");
              for(let item in lesson){
                timingArr.push([1, parseInt(lesson[item])])
              }
              break;
            };
            case "tue":{
              let lesson = String(tmp.timetable[key]).split("");
              for(let item in lesson){
                timingArr.push([2, parseInt(lesson[item])])
              }
              break;
            };
            case "wed":{
              let lesson = String(tmp.timetable[key]).split("");
              for(let item in lesson){
                timingArr.push([3, parseInt(lesson[item])])
              }
              break;
            };
            case "thu":{
              let lesson = String(tmp.timetable[key]).split("");
              for(let item in lesson){
                timingArr.push([4, parseInt(lesson[item])])
              }
              break;
            };
            case "fri":{
              let lesson = String(tmp.timetable[key]).split("");
              for(let item in lesson){
                timingArr.push([5, parseInt(lesson[item])])
              }
              break;
            };
          }
        }
        tmp["departmentName"] = tmp.department.name;
        tmp["department"] = tmp.department.department_id;
        tmp["teacherName"] = tmp.teacher.username;
        tmp["teacher"] = tmp.teacher.user_id;
        tmp["publish"] = String(tmp.publish).substr(0, 10); //截取时间字符串
        tmp["timing"] = timingArr;
        tmp["timetable_id"] = tmp.timetable.timetable_id;
        tmp["timetable"] = [tmp.timetable.start_time, tmp.timetable.end_time];
        arr.push(tmp);
      }

      console.log(arr)
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
    options: function(){
      let arr = [];
      let lesson = [];
      for(let i=1; i<7; i++){
        let tmp = {
          value: i,
          label: "第"+i+"节课"
        }
        lesson.push(tmp)
      }
      for(let i = 1; i<6; i++){
        let tmp = {
          value: i,
          label: this.transWeek(i),
          children: lesson
        }
        arr.push(tmp) 
      }
      return arr
    }
  },
  watch: {
    alterDepartment: function(val) {
      let params = new FormData();
      params.append("Departmentid", val);
      this.PostData("SearchC", params, () => {});
      this.PostData("SearchT", params, () => {}); 
    }
    
  },
  methods: {
    //转换成星期
    transWeek(number){
      switch(number){
        case 1: return "周一";
        case 2: return "周二";
        case 3: return "周三";
        case 4: return "周四";
        case 5: return "周五";
      }
    },
    //清空表单
    CleanForm() {
      this.form = {
        course_id: null,
        name: "",
        content: "",
        department: "",
        classes: "",
        teacher: "",
        publish: "",
        timetable: "",
        timing: ""
      };
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

    ClickAdd() {
      this.AddDataPanel = true;
      this.CleanForm();
    },
    ClickEdit(Row) {
      this.CleanForm();
      let params = new FormData();
      this.form = Object.assign({}, Row);
      params.append("Courseid", Row.course_id)
      this.PostData("SearchCC", params, () => {
        this.EditDataPanel = true;
      })
    },

    //添加数据方法
    AddData(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$confirm("是否添加该信息?", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          })
            .then(() => {
              //添加数据
              let AddParams = Object.assign({}, this.form);
              // 将院系id转为对象
              AddParams["publish"] = this.GetDate();

              this.PostData("Add", AddParams, () => {
                this.AddDataPanel = false;
                this.loading = true;
                this.PostData("Search", null, () => {
                  this.loading = false;
                });
              });
            })
            .catch(() => {
              this.AddDataPanel = false;
              this.$message({
                showClose: true,
                type: "info",
                message: "取消添加"
              });
            });
        } else {
          this.$message({
            showClose: true,
            type: "error",
            message: "提交错误!"
          });
        }
      });
    },

    //修改数据方法
    EditData(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          var RowData = this.rowData; //获取当前行数据
          console.log(RowData);
          this.$confirm("是否修改此信息?", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          })
            .then(() => {
              //修改数据
              let EditParams = Object.assign({}, this.form);
              console.log("EditParams");
              console.log(EditParams);
              
              delete EditParams.departmentName;
              delete EditParams.classesName;
              this.PostData("Edit", EditParams, () => {
                this.EditDataPanel = false;
                this.loading = true;
                //查询数据
                this.PostData("Search", null, () => {
                  this.loading = false;
                });
              });
            })
            .catch(() => {
              this.EditDataPanel = false;
              this.$message({
                showClose: true,
                type: "info",
                message: "已取消修改"
              });
            });
        } else {
          this.$message({
            showClose: true,
            type: "error",
            message: "提交错误!"
          });
        }
      });
    },

    //删除数据方法
    DeleteData(RowData) {
      console.log(RowData);
      this.$confirm("此操作将永久删除该信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          //删除数据
          let DeleteParams = new FormData();
          DeleteParams.append("courseid", RowData.course_id);
          this.PostData("Delete", DeleteParams, () => {
            this.EditDataPanel = false;
            this.loading = true;
            //查询数据
            this.PostData("Search", null, () => {
              this.loading = false;
            });
          });
        })
        .catch(() => {
          this.$message({
            showClose: true,
            type: "info",
            message: "已取消删除"
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
      let url = "http://localhost:8181/Admin/Course" + String(option); //请求数据库地址
      switch (option) {
        case "Add":
          distr = "添加";
          break;
        case "Edit":
          distr = "修改";
          break;
        case "Delete":
          distr = "删除";
          break;
        case "SearchD":
          url = "http://localhost:8181/Util/Department";
          break;
        case "SearchC":
          url = "http://localhost:8181/Util/DepartmentClasses";
          break;
        case "SearchT":
          url = "http://localhost:8181/Util/DepartmentTeacher";
          break;
        case "SearchCC":
          url = "http://localhost:8181/Util/CourseClasses";
          break;
      }
      var that = this;
      axios.post(url, params).then(function(resp) {
        if (resp.status == 200) {
          console.log(resp);
          if (option == "Search") {
            that.database = resp.data;
          } else if (option == "SearchD") {
            that.departments = resp.data;
          } else if (option == "SearchC") {
            that.allclasses = resp.data;
          } else if (option == "SearchT") {
            that.allteachers = resp.data;
          } else if (option == "SearchCC") {
            that.$set(that.form, "classes", resp.data)
          }else if (resp.data) {
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
    //查询所有课程信息--后台方法
    this.PostData("Search", null, () => {
      this.loading = false;
      this.PostData("SearchD", null, () => {});
    });
  }
};
</script>

