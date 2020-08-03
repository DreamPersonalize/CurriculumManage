<template>
  <el-container>
    <el-header>
      <navigation-comp
        :userpage="userpage"
        :userinfo="userinfo"
        :activeIndex="activeIndex"
      ></navigation-comp>
    </el-header>
    <el-main>
      <!-- 主体内容 -->
      <router-view :departments="departments" :userid="userid"></router-view>
    </el-main>
  </el-container>
</template>

<script>
import NavigationComp from "@/components/NavigationComp.vue";

export default {
  name: "Administrator",
  data: function() {
    return {
      userpage: "Admin",
      userid: '',
      userinfo: {},
      activeIndex: "/admin/course",
      departments: [
        {
          department_id: 1,
          Name: "数学与计算机学院"
        },
        {
          department_id: 2,
          Name: "商学院"
        },
        {
          department_id: 3,
          Name: "电气与信息工程学院"
        },
        {
          department_id: 4,
          Name: "计算机科学与工程学院"
        },
        {
          department_id: 5,
          Name: "法学与公共管理学院"
        }
      ]
    };
  },
  components: {
    NavigationComp
  },
  created(){
    //获取用户信息
    const that = this;
    var user = this.$route.query.userinfo;
    that.userid = user.user_id;
    that.userinfo = user;
  }
};
</script>

<style scoped>
.el-container {
  height: 768px;
  background-image: url("../../../assets/bg3.png");
  background-size: 100% 100%;
}
</style>
