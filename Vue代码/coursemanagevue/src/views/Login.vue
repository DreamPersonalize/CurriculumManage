<template>
  <el-container>
    <el-main>
      <el-card>
        <div class="LoginPanel">用户登录</div>
        <el-divider>
          <i class="el-icon-s-custom"></i>
        </el-divider>
        <el-form
          :model="ruleForm"
          :rules="rules"
          ref="ruleForm"
          label-width="100px"
          class="demo-ruleForm"
        >
          <el-form-item label="账号" prop="account">
            <el-input v-model="ruleForm.account" placeholder="请输入学号/教职工号/管理员账号" clearable></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="ruleForm.password" placeholder="请输入密码" clearable show-password></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm('ruleForm')">登录</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-main>
  </el-container>
</template>

<script>
import axios from "axios";
import { Message } from "element-ui";

export default {
  name: "Login",
  data: function() {
    return {
      ruleForm: {
        account: "",
        password: ""
      },
      rules: {
        account: [
          { required: true, message: "账号不能为空", trigger: "blur" },
          { min: 4, max: 10, message: "长度在 4 到 10 个字符", trigger: "blur" }
        ],
        password: [
          { required: true, message: "密码不能为空", trigger: "change" }
        ]
      }
    };
  },
  methods: {
    submitForm(formName) {
      const that = this;
      that.$refs[formName].validate(valid => {
        if (valid) {
          axios
            .post("http://localhost:8181/Login/Verification", that.ruleForm)
            .then(function(resp) {
              console.log(resp);
              if (resp.data != "") {
                Message({
                  showClose: true,
                  message: "登录成功",
                  type: "success"
                });
                let userinfo = resp.data;
                console.log("userinfo");
                console.log(userinfo);
                if (userinfo.power.power_id == 1) {
                  that.$router.push({
                    path: "/admin/course",
                    query: {
                      userinfo: userinfo
                    }
                  });
                } else {
                  that.$router.push({
                    path: "/course",
                    query: {
                      userinfo: userinfo
                    }
                  });
                }
              } else if (resp.data == false) {
                Message({
                  showClose: true,
                  message: "账号或密码错误",
                  type: "error"
                });
              }
            });
        } else {
          Message({
            showClose: true,
            message: "提交错误",
            type: "error"
          });
          return false;
        }
      });
    }
  }
};
</script>

<style scoped>
.el-container {
  height: 768px;
  background-image: url("../assets/bg1.png");
  background-size: 100% 100%;
}
.LoginPanel {
  width: 100%;
  text-align: center;
}
.el-card {
  width: 400px;
}
.el-main {
  display: flex;
  justify-content: center;
  align-items: center;
}
.el-button {
  width: 100%;
}
.el-form {
  margin-left: -30px;
}
</style>
