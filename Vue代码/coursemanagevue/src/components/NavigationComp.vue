<template>
  <el-row type="flex" class="row-bg" justify="end">
    <slot></slot>
    <!-- 导航栏 -->
    <el-col :span="16">
      <el-menu
        router
        :default-active="activeIndex"
        class="el-menu-demo"
        mode="horizontal"
        @select="handleSelect"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
      >
        <el-menu-item
          v-for="(item, index) in nav"
          :key="index"
          :index="item.path"
          >{{ item.name }}</el-menu-item
        >
      </el-menu>
    </el-col>
    <userinfo-comp :userinfo="userinfo"></userinfo-comp>
  </el-row>
</template>

<script>
import UserinfoComp from "@/components/UserinfoComp.vue";

export default {
  name: "NavigationComp",
  props: {
    userpage: String,
    userinfo: Object,
    activeIndex: String
  },
  computed: {
    //   获取管理员的界面路由信息
    nav: function() {
      // console.log(userpage);
      let navitems = [];
      let routes = this.$router.options.routes;
      //遍历根节点
      for (let i = 0; i < routes.length; i++) {
        let item = routes[i];
        if (item.name == this.userpage) {
          let itemChildrens = item.children;
          //遍历孩子节点
          for (let j = 0; j < itemChildrens.length; j++) {
            let itemChildren = itemChildrens[j];
            navitems.push(itemChildren);
          }
        }
      }
      return navitems;
    }
  },
  components: {
    UserinfoComp
  }
};
</script>

<style>
.el-header {
  background-color: #545c64;
}
</style>
