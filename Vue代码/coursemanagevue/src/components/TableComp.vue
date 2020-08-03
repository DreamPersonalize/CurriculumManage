<template>
  <!-- 主要信息卡片 -->
  <el-row type="flex" class="row-bg" justify="center">
    <el-col :span="18">
      <el-card>
        <slot name="search"></slot>
        <el-container style="height:450px;">
          <el-main>
            <el-table :data="tableData"  v-loading="loading" stripe height="400" style="width: 100%;" >
              <el-table-column type="index" sortable :key="Math.random()"></el-table-column>
              <el-table-column
                v-for="(item, index) in tableHead"
                :key="index"
                :prop="item.prop"
                :label="item.label"
                sortable
                align="center"
                show-overflow-tooltip
              ></el-table-column>
              <slot name="other"></slot>
            </el-table>
          </el-main>
        </el-container>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: "TableComp",
  props: {
    tableHead: Array,
    tableData: Function,
    loading: Boolean
  }
};
</script>



