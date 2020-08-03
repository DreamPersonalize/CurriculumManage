<template>
  <div>
    <!-- 班级信息卡片 -->
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
        <el-form-item label="院系" prop="department" :label-width="formLabelWidth">
          <el-select v-model="form.department" placeholder="请选择所在院系">
            <el-option
              v-for="item in departments"
              :key="item.department_id"
              :label="item.name"
              :value="item.department_id"
              clearable
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="年级" prop="grade" placeholder="请填写年级" :label-width="formLabelWidth">
          <el-input v-model="form.grade" autocomplete="off"></el-input>
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
        <el-form-item label="院系" prop="department" :label-width="formLabelWidth">
          <el-select v-model="form.department" placeholder="请选择所在院系">
            <el-option
              v-for="item in departments"
              :key="item.department_id"
              :label="item.name"
              :value="item.department_id"
              clearable
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="年级" prop="grade" placeholder="请填写年级" :label-width="formLabelWidth">
          <el-input v-model="form.grade" autocomplete="off"></el-input>
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
  name: "AdminClassesManage",

  data: function() {
    return {
      search: "",
      loading: true,
      database: [], //数据库数据
      departments: [],
      AddDataPanel: false, //添加面板
      EditDataPanel: false, //编辑面板
      form: {
        classes_id: 0,
        name: "",
        grade: "",
        department: ""
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
      formLabelWidth: "120px",
      tableHead: [
        {
          prop: "name",
          label: "班级名称"
        },
        {
          prop: "departmentName",
          label: "所属院系"
        },
        {
          prop: "grade",
          label: "年级"
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
      const Search = this.search;
      console.log(Search);
      let arr = [];
      // 进行数据修改
      for (let i = 0; i < Database.length; i++) {
        let tmp = Object.assign({}, Database[i]);
        tmp["departmentName"] = tmp.department.name;
        tmp["department"] = tmp.department.department_id;
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
    //清空表单
    CleanForm() {
      this.form = {
        classes_id: 0,
        name: "",
        grade: "",
        department: ""
      };
    },
    //点击添加按钮
    ClickAdd() {
      this.AddDataPanel = true;
      this.CleanForm();
    },
    //点击修改按钮
    ClickEdit(RowData) {
      console.log(RowData);
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
              let Departments = this.departments;
              //添加数据
              let AddParams = this.form;
              //将院系id转为对象
              for (let i in Departments) {
                if (Departments[i].department_id == AddParams.department) {
                  AddParams["department"] = Departments[i];
                  break;
                }
              }
              this.PostData("Add", AddParams, () => {
                this.AddDataPanel = false;
                this.loading = true;
                //查询数据
                let SearchParams = new FormData();
                this.PostData("Search", SearchParams, () => {
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
              let AddParams = this.form;

              //将院系id转为对象
              for (let i in Departments) {
                if (Departments[i].department_id == AddParams.department) {
                  AddParams["department"] = Departments[i];
                  break;
                }
              }
              this.PostData("Edit", AddParams, () => {
                this.EditDataPanel = false;
                this.loading = true;
                //查询数据
                let SearchParams = new FormData();
                this.PostData("Search", SearchParams, () => {
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
          DeleteParams.append("classesid", RowData.classes_id);
          this.PostData("Delete", DeleteParams, () => {
            this.EditDataPanel = false;
            this.loading = true;
            //查询数据
            let SearchParams = new FormData();
            this.PostData("Search", SearchParams, () => {
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
      let url = "http://localhost:8181/Admin/Classes" + String(option); //请求数据库地址
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
      }
      var that = this;
      axios.post(url, params).then(function(resp) {
        if (resp.status == 200) {
          if (option == "Search") {
            console.log(resp);
            that.database = resp.data;
          } else if (option == "SearchD") {
            console.log(resp);
            that.departments = resp.data;
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
    //查询所有班级信息--后台方法
    let params = new FormData();
    console.log("---------------------")
    console.log(params)
    //获取院系信息
    this.PostData("Search", params, () => {
      this.loading = false;
      this.PostData("SearchD", params, () => {});
    });
  }
};
</script>
