<template>
  <div class="page-content">
    <el-card class="card" shadow="hover" v-for="(item, index) in tableData.data" :key="index">
      <div class="card-header">
        <span>{{ item.name }}</span>
      </div>
      <div class="card-body">
        <p>在线用户数: {{ item.onlineCount }}</p>
        <p>总用户数: {{ item.totalCount }}</p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
  const tableData = reactive({
    data: [
      { name: '用户1', onlineCount: 10, totalCount: 100 },
      { name: '用户2', onlineCount: 20, totalCount: 200 },
      { name: '用户3', onlineCount: 30, totalCount: 300 }
    ]
  })
</script>

<style lang="less" scoped></style>
