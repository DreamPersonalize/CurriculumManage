<template>
  <el-row type="flex" class="row-bg" justify="center">
    <el-col :span="18">
      <el-card class="box-card">
        <el-collapse v-model="activeNames" @change="handleChange">
          <el-collapse-item>
            <template slot="title">
              <p style="font-weight:bold">《{{ Noticeinfo.name }}》---{{ Noticeinfo.releasetime }}</p>
            </template>
            <div>{{ Noticeinfo.content }}</div>
          </el-collapse-item>
        </el-collapse>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: "NoticeComp",
  props: {
    noticeinfo: Object
  },
  computed: {
    Noticeinfo(){
      let noticeinfo = this.noticeinfo;
      if(noticeinfo == null){
        noticeinfo["name"] = "暂无公告";
        noticeinfo["content"] = "暂无公告";
      }else{
        noticeinfo["releasetime"] = String(noticeinfo.releasetime).substr(0, 10);
      }
      return noticeinfo;
    }
  }
};
</script>

