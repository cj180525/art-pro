<template>
  <el-sub-menu v-if="hasChildren" :index="item.path">
    <template #title>
      <i class="iconfont-sys" v-html="item.meta?.icon" />
      <!-- <span class="item-title">{{ item.meta?.title }}</span> -->
      <span class="item-title">{{ item.name }}</span>
    </template>
    <menu-item v-for="child in item.children" :index="child.path" :item="child" />
  </el-sub-menu>

  <el-menu-item v-else :index="item.path" @click="item.meta?.type === 5 ? openExternal(item.meta?.link) : null">
    <i class="iconfont-sys" v-html="item.meta?.icon" />
    <!-- <span class="item-title">{{ item.meta?.title }}</span> -->
    <span class="item-title">{{ item.name }}</span>
  </el-menu-item>
</template>

<script setup>
  const props = defineProps({
    item: Object
  })

  const hasChildren = props.item.children && props.item.children.length > 0

  // 防止原页面跳转，只打开新窗口
  const openExternal = (link) => {
    window.open(link, '_blank')
  }
</script>

<style lang="scss" scoped>
  .item-title {
    font-size: 15px;
  }
  .iconfont-sys {
    font-size: 20px;
    padding-right: 8px;
  }
</style>
