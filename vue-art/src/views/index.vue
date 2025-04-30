<template>
  <div class="frame">
    <el-container>
      <el-aside :width="isCollapse ? '64px' : '240px'">
        <MenuLeft :isClose="isCollapse" />
      </el-aside>
      <el-container>
        <el-header>
          <TopBar />
          <WorkBar />
        </el-header>
        <el-main>
          <router-view v-if="isRefresh" />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts">
  import MenuLeft from '@comps/layout/MenuLeft.vue'
  import TopBar from '@comps/layout/TopBar.vue'
  import WorkBar from '@comps/layout/WorkBar.vue'
  import { computed, watch, nextTick } from 'vue'
  import { useSettingStore } from '@/stores/setting'

  // 是否需要刷新
  const refresh = computed(() => useSettingStore().refresh)

  // 是否折叠菜单
  const isCollapse = computed(() => useSettingStore().isCollapse)

  // 监听刷新状态变化
  const isRefresh = ref(true)
  watch(refresh, () => {
    isRefresh.value = false
    nextTick(() => {
      isRefresh.value = true
    })
  })
</script>

<style lang="scss" scoped>
  .frame {
    height: 100%;
    background: var(--art-bg-color);

    .el-container {
      height: 100%;

      .el-aside {
        transition: all 0.3s ease-in-out;
      }

      .el-container {
        .el-header {
          padding: 0;
          height: 90px;
        }

        .el-main {
          padding-top: 0px;
          padding-bottom: 16px;

          // 子页面默认style
          :deep(.page-content) {
            position: relative;
            box-sizing: border-box;
            padding: 20px;
            overflow: hidden;
            background: var(--art-main-bg-color);
            border: 1px solid var(--art-card-border) !important;
            border-radius: calc(var(--custom-radius) / 2 + 2px) !important;
          }
        }
      }
    }
  }
</style>
