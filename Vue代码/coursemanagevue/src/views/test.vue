<template>
  <div>
    <el-upload
      class="upload-demo"
      ref="upload"
      action
      :limit="1"
      :http-request="fileUpload"
      :on-exceed="handleExceed"
      :before-remove="beforeRemove"
      :file-list="fileList"
      :auto-upload="false"
    >
      <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
      <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
    </el-upload>
    <el-button slot="trigger" style="width:100%" type="primary" @click="fileDown">点击下载</el-button>
  </div>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {
      fileList: [{name:"测试文件.txt"}]
    };
  },
  methods: {
    fileUpload(params) {
      let file = params.file;
      let fileParams = new FormData();
      fileParams.append("file", file);
      this.PostData("upload", fileParams, () => {
        console.log("成功");
      });
    },
    fileDown() {
      this.PostData("download", null, () => {
        console.log("下载成功");
      });
    },
    submitUpload() {
      this.$refs.upload.submit();
    },
    handleExceed() {
      this.$message.warning(`当前限制上传 1 个文件`);
    },
    beforeRemove(file) {
      console.log(file, this.fileList);
      return this.$confirm(`确定移除 ${file.name}？`);
    },
    handlePreview(file) {
      console.log(file);
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
      let url = "";
      let config = {};
      switch (option) {
        case "upload":
          url = "http://localhost:8181/Util/Upload";
          config = { headers: { 'Content-Type': 'multipart/form-data' } };
          break;
        case "download":
          url = "http://localhost:8181/Util/Download";
          config = {responseType:'blob' };
          break;
      }
      var that = this;
      axios.post(url, params, config).then(function(resp) {
        if (resp.status == 200) {
          if (option == "upload") {
            if (resp.data) {
              that.$message.success("上传成功");
              that.fileList = [];
            } else {
              that.$message.error("上传失败");
            }
          } else if (option == "download") {
            console.log("获取下载文件");
            console.log(resp);
            let blob = resp.data;
            let url = window.URL.createObjectURL(blob);
            let el = document.createElement("a");
            el.href = url;
            el.download = "无限流代码.txt";
            el.hidden = true;
            document.body.appendChild(el);
            el.click();
            document.body.removeChild(el);
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
  }
};
</script>