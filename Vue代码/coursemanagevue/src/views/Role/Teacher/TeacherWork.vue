<template>
  <div>
    <!-- 课程信息卡片 -->
    <table-comp :tableHead="tableHead" :tableData="PubTableData" :loading="loading">
      <template v-slot:search>
        <el-input placeholder="请键入内容进行搜索" v-model="search" clearable></el-input>
      </template>
      <template v-slot:other>
        <el-table-column align="right">
          <template slot="header">
            <el-button type="success" icon="el-icon-plus" round @click="PubClickAdd()"></el-button>
          </template>
          <template slot-scope="scope">
            <el-button-group>
              <el-button type="warning" icon="el-icon-edit" @click="PubClickEdit(scope.row)"></el-button>
              <el-button type="danger" icon="el-icon-close" @click="PubDeleteData(scope.row)"></el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </template>
    </table-comp>

    <!-- 添加按钮弹窗 -->
    <el-dialog title="添加作业" :visible.sync="PubAddDataPanel" width="30%" center>
      <el-form :model="form" :rules="rules" ref="AddForm">
        <el-form-item label="名称" prop="name" placeholder="请输入作业名称" :label-width="formLabelWidth">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="概要" prop="content" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form.content"></el-input>
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
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="补签" prop="statu" :label-width="formLabelWidth">
          <el-switch v-model="form.statu"></el-switch>
        </el-form-item>
        <el-form-item label="上传文件" :label-width="formLabelWidth">
          <el-upload
            class="upload-demo"
            ref="upload"
            action
            :limit="1"
            :http-request="PubAddData"
            :on-exceed="handleExceed"
            :before-remove="beforeRemove"
            :file-list="form.fileList"
            :auto-upload="false"
          >
            <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="PubAddDataPanel = false">取 消</el-button>
        <el-button type="primary" @click="submitUpload">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 编辑按钮对话框 -->
    <el-dialog title="信息修改" :visible.sync="PubEditDataPanel" width="30%" center>
      <el-form :model="form" :rules="rules" ref="PubEditForm">
        <el-form-item label="名称" prop="name" placeholder="请输入作业名称" :label-width="formLabelWidth">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="概要" prop="content" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form.content"></el-input>
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
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="补签" prop="statu" :label-width="formLabelWidth">
          <el-switch v-model="form.statu"></el-switch>
        </el-form-item>
        <el-form-item label="上传文件" :label-width="formLabelWidth">
          <el-upload
            class="upload-demo"
            ref="upedit"
            action
            :limit="1"
            :http-request="PubEditData"
            :on-preview="handlePreview"
            :on-exceed="handleExceed"
            :before-remove="beforeRemove"
            :file-list="form.fileList"
            :auto-upload="false"
          >
            <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="PubEditDataPanel = false">取 消</el-button>
        <el-button type="primary" @click="submitUpedit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import TableComp from "@/components/TableComp.vue";
import axios from "axios";

export default {
  name: "TeacherWork",
  props: {
    userinfo: Object,
    course_id: Number
  },
  data: function() {
    return {
      search: "",
      database: [], //数据库数据
      loading: true,
      PubAddDataPanel: false, //添加面板
      PubEditDataPanel: false, //编辑面板
      formLabelWidth: "120px",

      form: {
        taskpub_id: null,
        name: "",
        content: "",
        start_time: "",
        end_time: "",
        fileList: [],
        statu: true,
        releasetime: "",
        time: ""
      },
      rules: {
        name: [
          { required: true, message: "作业名称不能为空", trigger: "blur" },
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
          label: "名称"
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
          label: "是否补交"
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
    PubTableData: function() {
      let Database = this.database;
      console.log("database");
      console.log(Database);
      let Search = this.search;
      let arr = [];
      //进行数据修改
      for (let i = 0; i < Database.length; i++) {
        let tmp = Object.assign({}, Database[i]);
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
        tmp["time"] = [tmp.start_time, tmp.end_time]
        tmp["fileList"] = [{ name: tmp.enclosure.name }];
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
      return params;
    }
  },
  methods: {
    //清空表单
    CleanForm() {
      this.form = {
        taskpub_id: null,
        name: "",
        content: "",
        start_time: "",
        end_time: "",
        fileList: [],
        statu: true,
        releasetime: ""
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

    //文件数量限制
    handleExceed() {
      this.$message.warning(`当前限制上传 1 个文件`);
    },
    //点击文件列表时进行下载
    handlePreview(){
      let fileName = this.form.fileList[0].name;
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

    //资源修改确认
    submitUpedit() {
      this.$refs.upedit.submit();
    },

    PubClickAdd() {
      this.PubAddDataPanel = true;
      this.CleanForm();
    },
    PubClickEdit(Row) {
      this.PubEditDataPanel = true;
      this.form = Object.assign({}, Row);
    },

    //添加数据方法
    PubAddData(params) {
      this.$confirm("是否发布此次作业?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          //添加方法
          console.log("-----------------------add");
          console.log(this.form);
          let form = this.form;
          let file = params.file;
          let formData = {};
          let AddParams = new FormData();
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
          this.PostData("Add", AddParams, () => {
            this.PubAddDataPanel = false;
            this.loading = true;
            this.PostData("Search", this.SearchParam, () => {
              this.loading = false;
            });
          });
        })
        .catch(() => {
          this.PubAddDataPanel = false;
          this.$message({
            showClose: true,
            type: "info",
            message: "取消添加"
          });
        });
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
          console.log("-----------------------edit");
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

    //删除数据方法
    PubDeleteData(RowData) {
      this.$confirm("此操作将永久删除该信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          //删除方法
          let DeleteParams = new FormData();
          DeleteParams.append("taskpub_id", RowData.taskpub_id);
          this.PostData("Delete", DeleteParams, () => {
            this.loading = true;
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
      let config = {};
      let url = "http://localhost:8181/Teacher/Taskpub" + String(option); //请求数据库地址
      switch (option) {
        case "Add":
          distr = "添加";
          config = { headers: { "Content-Type": "multipart/form-data" } };
          break;
        case "Edit":
          distr = "修改";
          config = { headers: { "Content-Type": "multipart/form-data" } };
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
        if (resp.status == 200) {
          if (String(option) == "Search") {
            that.database = resp.data;
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
    this.PostData("Search", this.SearchParam, () => {
      this.loading = false;
    });
  }
};
</script>
