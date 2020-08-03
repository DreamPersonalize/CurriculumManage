<template>
  <!-- 用户头像 -->
  <el-col :span="1">
    <div class="block" @click="drawer = true" style="margin-top:10px;">
      <el-avatar :size="small" :src="circleUrl"></el-avatar>
    </div>
    <!-- 抽屉信息框 -->
    <el-drawer
      title="用户信息"
      :visible.sync="drawer"
      :direction="rtl"
      :size="'10%'"
      :show-close="false"
      :before-close="handleClose"
    >
      <el-container style="height: 100%">
        <el-main>
          <el-row
            type="flex"
            class="row-bg"
            justify="center"
            v-for="(item, index) in User"
            :key="index"
          >
            <el-col :span="12" style="display:flex; justify-content:center; align-items:center;">
              <p>{{ item }}</p>
            </el-col>
          </el-row>
        </el-main>
        <el-footer>
          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="24">
              <el-button type="danger" @click="exit" plain>退出登录</el-button>
            </el-col>
          </el-row>
        </el-footer>
      </el-container>
    </el-drawer>
  </el-col>
</template>

<script>
export default {
  name: "UserinfoComp",
  props: {
    userinfo: Object
  },
  data: function() {
    return {
      drawer: false,
      circleUrl:
        "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
    };
  },
  
  computed: {
    User: function(){
      let Userinfo = Object.assign({}, this.userinfo);
      console.log("--------------")
      console.log(Userinfo)
      let arr = [];
      arr.push(Userinfo.username);
      arr.push(this.transSex(Userinfo.sex));
      arr.push(Userinfo.age);
      switch(Userinfo.power.power_id){
        case 2: 
          arr.push(Userinfo.department.name);
          arr.push(Userinfo.title.name);
          break;
        case 3:
          arr.push(Userinfo.department.name);
          arr.push(Userinfo.classes.name);
          arr.push(Userinfo.grade);
          break;
      }
      return arr;
    }
  },
  methods: {
    transSex(sex){
      if(sex == 0){
        return "男"
      }else if(sex == 1){
        return "女"
      }else{
        return ""
      }
    },
    exit() {
      this.$router.push({
        path: "/Login"
      });
    }
  }
};
</script>

<style scoped>
.el-button {
  width: 100%;
}
</style>
