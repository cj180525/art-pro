<script setup>

</script>

<template>
  <div style="width: 100vw;height: 100vh;overflow: hidden;display: flex">
    <div style="flex: 1;background-color: red">
      <el-image style="width: 100%;height: 100%" fit="cover"
        src="https://cdn.pixabay.com/photo/2023/07/14/18/23/color-pencils-8127500_1280.jpg"/>
    </div>
    <div class="welcome-card">
      <div style="font-size: 30px;font-weight: bold">欢迎！欢迎！</div>
      <div style="margin-top: 10px">您的！欢迎！欢迎欢迎欢迎欢迎欢迎欢迎欢迎欢迎欢迎欢迎！</div>
      <div style="margin-top: 5px">好用你还不快点来！</div>
    </div>
    <div class="right-card">
      <router-view v-slot="{ Component }">
        <transition name="el-fade-in-linear" mode="out-in">
          <component :is="Component"/>
        </transition>
      </router-view>
    </div>

  </div>
</template>

<style scoped>
.welcome-card {
  position: absolute;
  bottom: 30px;
  left: 30px;
  color: black;
  text-shadow: 0 0 10px aqua;
}

.right-card {
  width: 400px;
  z-index: 1;
  background-color: var(--el-bg-color);
}
</style>