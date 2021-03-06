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
        <el-form-item label="姓名" prop="username" :label-width="formLabelWidth">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="sex" :label-width="formLabelWidth">
          <el-select v-model="form.sex" placeholder="性别选择">
            <el-option label="男" :value="0"></el-option>
            <el-option label="女" :value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="年龄" prop="age" :label-width="formLabelWidth">
          <el-input v-model="form.age"></el-input>
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
        <el-form-item label="职称" prop="title" :label-width="formLabelWidth">
          <el-select v-model="form.title" placeholder="请选择职称">
            <el-option
              v-for="item in titles"
              :key="item.title_id"
              :label="item.name"
              :value="item.title_id"
            ></el-option>
          </el-select>
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
        <el-form-item label="教职工号" prop="account" :label-width="formLabelWidth">
          <el-input v-model="form.account" disabled></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" :label-width="formLabelWidth">
          <el-input v-model="form.password" placeholder="请输入密码" clearable show-password></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="username" :label-width="formLabelWidth">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="sex" :label-width="formLabelWidth">
          <el-select v-model="form.sex" placeholder="性别选择">
            <el-option label="男" :value="0"></el-option>
            <el-option label="女" :value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="年龄" prop="age" :label-width="formLabelWidth">
          <el-input v-model.number="form.age"></el-input>
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
        <el-form-item label="职称" prop="title" :label-width="formLabelWidth">
          <el-select v-model="form.title" placeholder="请选择职称">
            <el-option
              v-for="item in titles"
              :key="item.title_id"
              :label="item.name"
              :value="item.title_id"
            ></el-option>
          </el-select>
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
  name: "AdminTeacherManage",
  data: function() {
    return {
      search: "",
      database: [],
      departments: [],
      titles: [],
      loading: true,
      AddDataPanel: false, //添加面板
      EditDataPanel: false, //编辑面板
      formLabelWidth: "120px",

      form: {
        user_id: null,
        account: "",
        password: "",
        power: "",
        username: "",
        sex: "",
        age: "",
        department: "",
        title: ""
      },

      rules: {
        username: [
          { required: true, message: "教师名称不能为空", trigger: "blur" },
          {
            min: 2,
            max: 25,
            message: "长度在 2 到 25个字符之间",
            trigger: "blur"
          }
        ],
        department: [
          { required: true, message: "请选择院系", trigger: "blur" }
        ],
        sex: [{ required: true, message: "请选择性别", trigger: "blur" }],
        age: [{ validator: this.checkAge, trigger: "blur" }],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }]
      },

      tableHead: [
        {
          prop: "account",
          label: "教职工号"
        },
        {
          prop: "username",
          label: "姓名"
        },
        {
          prop: "departmentName",
          label: "所属院系"
        },
        {
          prop: "titleName",
          label: "职称"
        }
      ]
    };
  },
  computed: {
    SearchParam: function() {
      let params = new FormData();
      params.append("role", "teacher");
      return params;
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
        tmp["departmentName"] = tmp.department.name;
        tmp["department"] = tmp.department.department_id;
        tmp["titleName"] = tmp.title.name;
        tmp["title"] = tmp.title.title_id;
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
    checkAge(rule, value, callback) {
      value = parseInt(value);
      setTimeout(() => {
        if (!Number.isInteger(value)) {
          callback(new Error("请输入数字值"));
        } else {
          if (value < 10 || value > 99) {
            callback(new Error("请输入合法年龄"));
          } else {
            callback();
          }
        }
      }, 200);
    },
    //清空表单
    CleanForm() {
      this.form = {
        user_id: null,
        account: "",
        password: "",
        power: "",
        username: "",
        sex: "",
        age: "",
        department: "",
        title: ""
      };
      this.password = "";
    },

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
              let Departments = this.departments;
              let Titles = this.titles;
              //添加数据
              let AddParams = Object.assign({}, this.form);
              //将院系id转为对象
              for (let i in Departments) {
                if (Departments[i].department_id == AddParams.department) {
                  AddParams["department"] = Departments[i];
                  break;
                }
              }
              //将职称id转为对象
              for (let i in Titles) {
                if (Titles[i].title_id == AddParams.title) {
                  AddParams["title"] = Titles[i];
                  break;
                }
              }
              //将年龄转化为数字
              AddParams["age"] = parseInt(AddParams["age"]);
              //设置用户角色
              AddParams["power"] = {
                power_id: 2
              };

              this.PostData("Add", AddParams, () => {
                this.AddDataPanel = false;
                this.loading = true;

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
          this.$confirm("是否修改此信息?", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          })
            .then(() => {
              //修改数据
              let Departments = this.departments;
              let Titles = this.titles;
              let EditParams = Object.assign({}, this.form);
              console.log("EditParams");
              console.log(EditParams);
              //将院系id转为对象
              for (let i in Departments) {
                if (Departments[i].department_id == EditParams.department) {
                  EditParams["department"] = Departments[i];
                  break;
                }
              }
              //将职称id转为对象
              for (let i in Titles) {
                if (Titles[i].title_id == EditParams.title) {
                  EditParams["title"] = Titles[i];
                  break;
                }
              }
              delete EditParams.departmentName;
              delete EditParams.titleName;
              this.PostData("Edit", EditParams, () => {
                this.EditDataPanel = false;
                this.loading = true;
                //查询数据
                this.PostData("Search", this.SearchParam, () => {
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
      this.$confirm("此操作将永久删除该信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          //删除数据
          let DeleteParams = new FormData();
          DeleteParams.append("userid", RowData.user_id);
          this.PostData("Delete", DeleteParams, () => {
            this.EditDataPanel = false;
            this.loading = true;
            //查询数据
            this.PostData("Search", this.SearchParam, () => {
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
      let url = "http://localhost:8181/Admin/User" + String(option); //请求数据库地址
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
        case "SearchT":
          url = "http://localhost:8181/Util/Title";
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
          } else if (option == "SearchT") {
            that.titles = resp.data;
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
    this.PostData("Search", this.SearchParam, () => {
      this.loading = false;
      this.PostData("SearchD", null, () => {});
      this.PostData("SearchT", null, () => {});
    });
  }
};
</script>
