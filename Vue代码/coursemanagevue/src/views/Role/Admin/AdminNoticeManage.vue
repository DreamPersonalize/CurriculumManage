<template>
  <div>
    <!-- 公告信息卡片 -->
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
  name: "AdminNoticeManage",
  props: {
    userid: Number
  },
  data: function() {
    return {
      search: "",
      loading: true,
      database: [], //数据库数据
      formLabelWidth: "120px",
      AddDataPanel: false, //添加面板
      EditDataPanel: false, //编辑面板
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
    }
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

    ClickAdd() {
      this.AddDataPanel = true;
      this.CleanForm();
    },
    ClickEdit(RowData) {
      this.EditDataPanel = true;
      this.form = Object.assign({}, RowData);
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
              let AddParams = this.form;
              AddParams["releasetime"] = new Date();
              AddParams["user"] = Number(this.userid);
              this.PostData("Add", AddParams, () => {
                this.AddDataPanel = false;
                this.loading = true;
                //查询数据
                let SearchParams = new FormData();
                SearchParams.append("userid", this.userid);
                this.PostData("Search", SearchParams, () => {
                  this.loading = false;
                });
              });
            })
            .catch(() => {
              this.AddDataPanel = false;
              this.$message({
                showClose: true,
                type: "warning",
                message: "添加错误！"
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
                let SearchParams = new FormData();
                SearchParams.append("userid", this.userid);
                this.PostData("Search", SearchParams);
              });
            })
            .catch(() => {
              this.EditDataPanel = false;
              this.$message({
                showClose: true,
                type: "warning",
                message: "系统错误！"
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
          //删除数据
          let DeleteParams = new FormData();
          DeleteParams.append("noticeid", RowData.notice_id);
          this.PostData("Delete", DeleteParams, () => {
            this.EditDataPanel = false;
            //查询数据
            let SearchParams = new FormData();
            SearchParams.append("userid", this.userid);
            this.PostData("Search", SearchParams);
          });
        })
        .catch(() => {
          this.$message({
            showClose: true,
            type: "warning",
            message: "系统错误！"
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
      let url = "http://localhost:8181/Admin/Notice" + String(option); //请求数据库地址
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
    params.append("userid", this.userid);
    this.PostData("Search", params, () => {
      this.loading = false;
    });
  }
};
</script>
