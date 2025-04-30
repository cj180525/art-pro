<template>
  <el-menu router :unique-opened="true" :default-active="activeMenu" :collapse="isClose">
    <el-menu-item index="/">
      <el-icon class="logo"><SuitcaseLine /></el-icon>
      <span class="logo">Art Design Pro</span>
    </el-menu-item>

    <menu-item v-for="item in menuList" :item="item" />
  </el-menu>
</template>

<script setup>
  import { useRoute } from 'vue-router'
  import { useMenuStore } from '@/stores/menu'

  const props = defineProps({ isClose: Boolean })

  // 返回当前路由路径作为默认激活菜单
  const route = useRoute()
  const activeMenu = computed(() => route.path)

  // 获取菜单列表
  const menuStore = useMenuStore()
  const menuList = menuStore.getMenuList
</script>

<style lang="scss" scoped>
  .el-menu {
    height: 100vh;
  }

  .logo {
    font-size: 24px;
    margin-right: 8px;
  }
</style>
