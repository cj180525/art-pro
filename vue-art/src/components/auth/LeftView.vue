<template>
  <div class="left-view">
    <img class="left-bg" src="@imgs/login/lf_bg.png" />

    <div class="logo">
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#iconsys-zhaopian-copy"></use>
      </svg>
      <h1 class="title">{{ systemName }}</h1>
    </div>

    <div class="img-view">
      <img class="img" src="@imgs/login/lf_icon2.png" />
      <h1> {{ $t('login.leftView.title') }} </h1>
      <p> {{ $t('login.leftView.subTitle') }} </p>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { SystemInfo } from '@/config/setting'

  const systemName = SystemInfo.name
</script>

<style lang="scss" scoped>
  .left-view {
    box-sizing: border-box;
    position: relative;
    height: 100%;
    width: 100%;
    padding: 20px;

    .left-bg {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      object-fit: cover; // 图片或视频适应容器尺寸，且保持原始比例裁切超出的部分，常用于做背景封面图效果
    }

    .logo {
      position: relative;
      display: flex;
      align-items: center;
      z-index: 100;

      .icon {
        width: 40px;
        height: 40px;
      }

      .title {
        margin: 3px 0 0 10px;
        font-size: 22px;
        font-weight: 400;
        color: var(--art-text-gray-100);
      }
    }

    .img-view {
      z-index: 100;
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      display: flex;
      flex-direction: column; /* 垂直排列 */
      justify-content: center; /* 水平居中 */
      align-items: center; /* 垂直居中 */

      .img {
        max-width: 300px;
        display: block;
      }

      h1 {
        font-size: 26px;
        font-weight: 400;
        color: #f9f9f9;
      }

      p {
        margin-top: 10px;
        font-size: 14px;
        color: #c4cada;
      }
    }
  }
</style>
