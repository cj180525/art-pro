<template>
  <el-config-provider :size="elSize" :locale="locales[language]" :z-index="3000">
    <RouterView />
  </el-config-provider>
</template>

<script setup lang="ts">
  import zh from 'element-plus/es/locale/lang/zh-cn'
  import en from 'element-plus/es/locale/lang/en'

  import { useUserStore } from '@/stores/user'
  // import { useSettingStore } from '@/stores/setting'
  import { getTokenCookie } from '@/utils/cookie'
  // import { systemUpgrade } from './utils/upgrade'
  import router from '@/router'

  const userStore = useUserStore()
  // const settingStore = useSettingStore()

  const language = computed(() => userStore.language)
  const elSize = computed(() => (document.body.clientWidth >= 500 ? 'default' : 'small'))

  const locales = {
    zh: zh,
    en: en
  }

  onBeforeMount(() => {
    // setBodyClass(true)
  })

  onMounted(() => {
    // setBodyClass(false)
    // systemUpgrade() // 检查系统更新
    getUserInfo() // 获取用户信息
  })

  // 提升暗黑主题下页面刷新视觉体验
  const setBodyClass = (addClass: boolean) => {
    let el = document.getElementsByTagName('body')[0]

    if (addClass) {
      el.setAttribute('class', 'theme-change')
    } else {
      setTimeout(() => {
        el.removeAttribute('class')
      }, 300)
    }
  }

  // 获取不到token或用户信息跳转登陆页面
  const getUserInfo = async () => {
    const token = getTokenCookie()
    if (!token && !userStore.info) {
      router.push('/auth/login')
    }
  }
</script>
