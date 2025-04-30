<template>
  <div class="copyright-content">
    <!-- ICP Link -->
    <a v-if="icp" :href="icpLink || 'javascript:void(0)'" target="_blank">
      {{ icp }}
    </a>

    <!-- Copyright Text -->
    Copyright © {{ date }}

    <!-- Company Link -->
    <a v-if="companyName" :href="companySiteLink || 'javascript:void(0)'" target="_blank">
      {{ companyName }}
    </a>
  </div>
</template>

<script lang="ts" setup>
  interface Props {
    companyName?: string
    companySiteLink?: string
    date?: string
    icp?: string
    icpLink?: string
  }

  withDefaults(defineProps<Props>(), {
    companyName: 'Art Pro',
    companySiteLink: '',
    date: '2024',
    icp: '',
    icpLink: 'http://www.baidu.com'
  })
</script>

<style lang="scss" scoped>
  .copyright-content {
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 14px;
    color: var(--art-gray-600);
    margin-top: 40px;

    a {
      color: var(--art-gray-600);
      text-decoration: none;
      margin-left: 5px;
      &:hover {
        text-decoration: underline;
      }
    }
  }
</style>
