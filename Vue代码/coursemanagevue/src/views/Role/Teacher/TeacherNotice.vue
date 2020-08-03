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
    <el-dialog title="添加班级" :visible.sync="AddDataPanel" width="30%" center>
      <el-form :model="form" :rules="rules" ref="AddForm">
        <el-form-item label="名称" prop="name" placeholder="请输入班级名称" :label-width="formLabelWidth">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="概要" prop="content" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form.content"></el-input>
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
        <el-form-item label="名称" prop="name" placeholder="请输入班级名称" :label-width="formLabelWidth">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="概要" prop="content" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form.content"></el-input>
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
  name: "TeacherNotice",
  props: {
    userinfo: Object,
    course_id: Number
  },
  data: function() {
    return {
      search: "",
      database: [], //数据库数据
      loading: true,
      AddDataPanel: false, //添加面板
      EditDataPanel: false, //编辑面板
      formLabelWidth: "120px",
      form: {
        notice_id: 0,
        name: "",
        content: "",
        releasetime: ""
      },
      rules: {
        name: [
          { required: true, message: "公告名称不能为空", trigger: "blur" },
          {
            min: 2,
            max: 25,
            message: "长度在 2 到 25个字符之间",
            trigger: "blur"
          }
        ]
      },
      
      tableHead: [
        {
          prop: "name",
          label: "公告名称"
        },
        {
          prop: "releasetime",
          label: "发布时间"
        }
      ]
    };
  },
  computed: {
    //表格数据
    tableData: function() {
      let Database = this.database;
      console.log("database");
      console.log(Database);
      let Search = this.search;
      let arr = [];
      //进行数据修改
      for (let i = 0; i < Database.length; i++) {
        let tmp = Object.assign({}, Database[i]);
        tmp.releasetime = String(tmp.releasetime).substr(0, 10); //截取时间字符串
        arr.push(tmp)
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
    },

    
  },
  methods: {
    //清空表单
    CleanForm() {
      this.form = {
        notice_id: 0,
        name: "",
        content: "",
        releasetime: ""
      };
    },

    //今日签到统计
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

    //获取当前时间
    // GetDate() {
    //   var date = new Date();
    //   var year = date.getFullYear();
    //   var month = date.getMonth() + 1;
    //   var day = date.getDate();
    //   if (month < 10) {
    //     month = "0" + month;
    //   }
    //   if (day < 10) {
    //     day = "0" + day;
    //   }
    //   var nowDate = year + "-" + month + "-" + day;
    //   return nowDate;
    // },

    ClickAdd() {
      this.AddDataPanel = true;
      this.CleanForm();
    },
    ClickEdit(Row) {
      this.EditDataPanel = true;
      this.form = Object.assign({}, Row);
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
              //添加方法
              let AddParams = this.form;
              AddParams["releasetime"] = new Date();
              AddParams["user"] = this.userinfo.user_id;
              AddParams["course"] = this.course_id;
              this.PostData("Add", AddParams, () => {
                this.AddDataPanel = false;
                this.loading = true;
                //查询数据
                this.PostData("Search", this.SearchParam, () => {
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
              console.log(this.form);
              let EditParams = this.form;
              this.PostData("Edit", EditParams, () => {
                this.EditDataPanel = false;
                //查询数据
                this.PostData("Search", this.SearchParam, () => {});
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
      this.$confirm("此操作将永久删除该信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          //删除方法
          let DeleteParams = new FormData();
          DeleteParams.append("noticeid", RowData.notice_id);
          this.PostData("Delete", DeleteParams, () => {
            this.EditDataPanel = false;
            //查询数据
            this.PostData("Search", this.SearchParam);
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
      let url = "http://localhost:8181/Teacher/Notice" + String(option); //请求数据库地址
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
      }
      var that = this;
      axios.post(url, params).then(function(resp) {
        if (resp.status == 200) {
          if (String(option) == "Search") {
            that.database = resp.data;
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
    //查询所有公告信息--后台方法
    let params = new FormData();
    params.append("course_id", this.course_id);
    this.PostData("Search", params, () => {
      this.loading = false;
    });
  }
};
</script>
