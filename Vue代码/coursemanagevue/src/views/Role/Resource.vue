<template>
  <div>
    <!-- 资源-未审核 -->
    <el-row v-if="userinfo.power.power_id == 2">
      <table-comp :tableHead="tableHead" :tableData="ExamineTableData">
        <template v-slot:search>
          <strong>资源审核</strong>
          <el-input placeholder="请键入内容进行搜索" v-model="ExamineSearch" clearable></el-input>
        </template>
        <template v-slot:other>
          <el-table-column align="right">
            <template slot-scope="scope">
              <el-button-group>
                <el-button type="warning" icon="el-icon-tickets" @click="ExamineSeeData(scope.row)"></el-button>
                <el-button type="danger" icon="el-icon-close" @click="DeleteData(scope.row)"></el-button>
              </el-button-group>
            </template>
          </el-table-column>
        </template>
      </table-comp>
    </el-row>

    <!-- 资源-未审核-查看 -->
    <el-dialog title="信息审核" :visible.sync="ExamineSeeDataPanel" width="30%" center>
      <el-form :model="form" ref="ExamineEditForm">
        <el-form-item label="名称" prop="name" :label-width="formLabelWidth">
          <el-input v-model="form.name" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="概要" prop="content" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form.content" disabled></el-input>
        </el-form-item>
        <el-form-item label="文件" prop="fileName" :label-width="formLabelWidth">
          <el-input v-model="form.fileName" disabled></el-input>
          <el-button
            type="primary"
            icon="el-icon-download"
            style="width:100%"
            @click="ClickDownFile"
          >下载</el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="ExamineSeeDataPanel = false">取 消</el-button>
        <el-button type="primary" @click="ExaminePassResource()">通 过</el-button>
      </div>
    </el-dialog>

    <!-- 资源-已审核 -->
    <table-comp :tableHead="tableHead" :tableData="DownTableData">
      <template v-slot:search>
        <strong>资源下载</strong>
        <el-input placeholder="请键入内容进行搜索" v-model="DownSearch" clearable></el-input>
      </template>
      <template v-slot:other>
        <el-table-column align="right">
          <template slot="header">
            <el-button type="success" icon="el-icon-plus" round @click="DownClickAdd()"></el-button>
          </template>
          <template slot-scope="scope">
            <el-button-group>
              <el-button type="warning" icon="el-icon-view" @click="DownClickSee(scope.row)"></el-button>
              <el-button
                type="danger"
                icon="el-icon-close"
                @click="DeleteData(scope.row)"
                v-if="userinfo.power.power_id == 2"
              ></el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </template>
    </table-comp>

    <!-- 资源-添加 -->
    <el-dialog title="上传资源" :visible.sync="DownAddDataPanel" width="30%" center>
      <el-form :model="form" :rules="rules" ref="DownAddForm">
        <el-form-item label="名称" prop="name" placeholder="请输入文件名称" :label-width="formLabelWidth">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="概要" prop="content" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form.content"></el-input>
        </el-form-item>
        <el-upload
          class="upload-demo"
          ref="upload"
          action
          :limit="1"
          :http-request="DownAddData"
          :on-exceed="handleExceed"
          :before-remove="beforeRemove"
          :file-list="fileList"
          :auto-upload="false"
        >
          <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
        </el-upload>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="DownAddDataPanel = false">取 消</el-button>
        <el-button type="primary" @click="submitUpload">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 资源-已审核-查看 -->
    <el-dialog title="资源" :visible.sync="DownSeeDataPanel" width="30%" center>
      <el-form :model="form" :rules="rules" ref="DownEditForm">
        <el-form-item label="名称" prop="name" placeholder="请输入文件名称" :label-width="formLabelWidth">
          <el-input v-model="form.name" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="概要" prop="content" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form.content" disabled></el-input>
        </el-form-item>
        <el-form-item label="文件" prop="fileName" :label-width="formLabelWidth">
          <el-input v-model="form.fileName" disabled></el-input>
          <el-button type="primary" icon="el-icon-view" style="width:100%" @click="ClickDownFile">下载</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import TableComp from "@/components/TableComp.vue";
import axios from "axios";

export default {
  name: "Resource",
  props: {
    userinfo: Object,
    course_id: Number
  },
  data: function() {
    return {
      ExamineSearch: "",
      DownSearch: "",
      Database: [],
      fileList: [],
      loading: true,
      ExamineSeeDataPanel: false, //审核查看面板
      DownAddDataPanel: false, //添加面板
      DownSeeDataPanel: false, //资源查看面板
      formLabelWidth: "60px",
      form: {
        resource_id: 0,
        name: "",
        content: "",
        releasetime: "",
        fileName: ""
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
          label: "文件名"
        },
        {
          prop: "userName",
          label: "上传人"
        },
        {
          prop: "releasetime",
          label: "上传时间"
        }
      ]
    };
  },
  computed: {
    //未审核资源
    ExamineTableData: function() {
      let Database = this.Database;
      console.log("database");
      console.log(Database);
      let Search = this.ExamineSearch;
      let arr = [];
      //进行数据修改
      for (let i = 0; i < Database.length; i++) {
        let tmp = Object.assign({}, Database[i]);
        if (tmp.state == 0) {
          tmp.releasetime = String(tmp.releasetime).substr(0, 10); //截取时间字符串
          tmp["userName"] = tmp.user.username;
          tmp["fileName"] = tmp.enclosure.name;
          arr.push(tmp);
        }
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

    //已审核资源
    DownTableData: function() {
      let Database = this.Database;
      console.log("database");
      console.log(Database);
      let Search = this.DownSearch;
      let arr = [];
      //进行数据修改
      for (let i = 0; i < Database.length; i++) {
        let tmp = Object.assign({}, Database[i]);
        if (tmp.state == 1) {
          tmp.releasetime = String(tmp.releasetime).substr(0, 10); //截取时间字符串
          tmp["userName"] = tmp.user.username;
          tmp["fileName"] = tmp.enclosure.name;
          arr.push(tmp);
        }
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
        resource_id: 0,
        name: "",
        content: "",
        releasetime: "",
        fileName: ""
      };
    },

    //未审核-资源查看
    ExamineSeeData(Row) {
      this.ExamineSeeDataPanel = true;
      this.form = Object.assign({}, Row);
    },

    //资源下载
    ClickDownFile() {
      let fileName = this.form.fileName;
      let params = new FormData();
      params.append("fileName", fileName);
      params.append("scene", "resource");
      this.PostData("Download", params, () => {});
    },

    //文件数量限制
    handleExceed() {
      this.$message.warning(`当前限制上传 1 个文件`);
    },
    //文件移除
    beforeRemove(file) {
      console.log(file, this.fileList);
      return this.$confirm(`确定移除 ${file.name}？`);
    },

    //资源上传点击
    DownClickAdd() {
      this.DownAddDataPanel = true;
      this.CleanForm();
      this.fileList = [];
    },
    //资源上传确认
    submitUpload() {
      this.$refs.upload.submit();
    },
    //已审核-资源查看
    DownClickSee(Row) {
      this.DownSeeDataPanel = true;
      this.form = Object.assign({}, Row);
    },

    //未审核-资源审核通过
    ExaminePassResource() {
      let params = new FormData();
      console.log(this.form.resource_id);
      params.append("Resource_id", this.form.resource_id);
      this.PostData("RExamine", params, () => {
      });
    },

    //添加数据方法
    DownAddData(params) {
      this.$confirm("是否添加该信息?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          //添加方法
          let form = this.form;
          let file = params.file;
          let formData = {};
          let AddParams = new FormData();
          console.log("开始赋值");
          console.log(file);
          formData["course"] = {
            course_id: this.course_id
          };
          formData["name"] = form.name;
          formData["content"] = form.content;
          formData["user"] = this.userinfo;
          formData["releasetime"] = new Date();
          console.log(formData);
          console.log("添加数据AddParams");
          let blob = new Blob([JSON.stringify(formData)], {
            type: "application/json"
          });
          console.log(blob);
          AddParams.append(
            "formData",
            new Blob([JSON.stringify(formData)], { type: "application/json" })
          );
          AddParams.append("file", file);
          this.PostData("Upload", AddParams, () => {});
        })
        .catch(() => {
          this.DownAddDataPanel = false;
          this.$message({
            showClose: true,
            type: "info",
            message: "添加错误"
          });
        });
    },

    //资源删除
    DeleteData(RowData) {
      this.$confirm("此操作将永久删除该资源, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          //删除方法
          let DeleteParams = new FormData();
          DeleteParams.append("Resource_id", RowData.resource_id);
          this.PostData("Delete", DeleteParams, () => {});
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
      let url = "http://localhost:8181/Util/Resource";
      let config = {};
      switch (option) {
        case "Search":
          url += "Search";
          break;
        case "Upload":
          url += "Upload";
          config = { headers: { "Content-Type": "multipart/form-data" } };
          break;
        case "Download":
          url += "Download";
          config = { responseType: "blob" };
          break;
        case "RExamine":
          url += "Examine";
          break;
        case "Delete":
          url += "Delete";
          break;
      }
      var that = this;
      axios.post(url, params, config).then(function(resp) {
        if (resp.status == 200) {
          console.log("resp");
          console.log(resp);
          if (option == "Search") {
            that.Database = resp.data;
          } else if (option == "Upload") {
            if (resp.data) {
              that.$message({
                showClose: true,
                type: "success",
                message: "已提交!"
              });
              that.DownAddDataPanel = false;
              //查询数据
              let params = new FormData();
              params.append("course_id", that.course_id)
              that.PostData("Search", params, () => {});
            } else {
              that.$message({
                showClose: true,
                type: "error",
                message: "系统错误!"
              });
            }
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
          } else if (option == "RExamine") {
            if (resp.data) {
              that.$message({
                showClose: true,
                type: "success",
                message: "资源已通过申请!"
              });
              that.ExamineSeeDataPanel = false;
              //查询数据
              let params = new FormData();
              params.append("course_id", that.course_id)
              that.PostData("Search", params, () => {});
            } else {
              that.$message({
                showClose: true,
                type: "error",
                message: "系统错误!"
              });
            }
          } else if (option == "Delete") {
            if (resp.data) {
              
              //查询数据
              let params = new FormData();
              params.append("course_id", that.course_id)
              that.PostData("Search", params, () => {
                that.ExamineSeeDataPanel = false;
              that.DownSeeDataPanel = false;
                that.$message({
                showClose: true,
                type: "success",
                message: "删除成功!"
              });
              });
            } else {
              that.$message({
                showClose: true,
                type: "error",
                message: "删除失败!"
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
    //查询所有课程信息--后台方法
    this.PostData("Search", params, () => {});
  }
};
</script>

<style scoped>
.el-row {
  margin-bottom: 10px;
}
</style>
