<template>
  <el-container class="bgimg">
    <el-header>
      <navigation-comp :userpage="userpage" :userinfo="userinfo" :activeIndex="activeIndex">
        <template>
          <el-col :span="8" style="display:flex; justify-content:left; align-items:center;">
            <el-button icon="el-icon-arrow-left" class="circlebtn" @click="jump()" circle plain></el-button>
          </el-col>
        </template>
      </navigation-comp>
    </el-header>
    <el-main>
      <!-- 主体内容 -->
      <router-view :course_id="course_id" :userinfo="userinfo"></router-view>
    </el-main>
  </el-container>
</template>

<script>
import NavigationComp from "@/components/NavigationComp.vue";

export default {
  name: "Student",
  data: function() {
    return {
      userinfo: this.$route.query.userinfo,
      course_id: this.$route.query.course_id,
      userpage: "Student",
      activeIndex: "/student/course/sign"
    };
  },
  components: {
    NavigationComp
  },
  methods: {
    jump() {
      this.$router.push({
        path: "/course",
        query: {
          userinfo: this.userinfo
        }
      });
    }
  }
};
</script>

<style scoped>
.bgimg {
  height: 768px;
  background-image: url("../../../assets/bg4.png");
  background-size: 100% 100%;
}
.circlebtn {
  background-color: #545c64;
}
</style>
