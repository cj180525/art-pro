<template>
  <div class="top-bar">
    <div class="left">
      <!-- 菜单按钮 -->
      <div class="btn-box">
        <div class="btn menu-btn">
          <i class="iconfont-sys" @click="changeCollapse">&#xe6ba;</i>
        </div>
      </div>
      <!-- 刷新按钮 -->
      <div class="btn-box">
        <div class="btn refresh-btn">
          <i class="iconfont-sys" @click="reload()"> &#xe6b3; </i>
        </div>
      </div>
      <!-- 面包屑 -->
      <el-breadcrumb>
        <el-breadcrumb-item v-for="(item, index) in breadcrumbList" :key="index">
          <!-- <span>{{ item.meta?.title }}</span> -->
          <span>{{ item.name }}</span>
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="right">
      <!-- 搜索 -->
      <!-- <div class="search-wrap">
        <div class="search-input" @click="openSearchDialog">
          <div class="left">
            <i class="iconfont-sys">&#xe710;</i>
            <span>{{ $t('topBar.search.title') }}</span>
          </div>
          <div class="search-keydown">
            <i class="iconfont-sys" v-if="isWindows">&#xeeac;</i>
            <i class="iconfont-sys" v-else>&#xe9ab;</i>
            <span>k</span>
          </div>
        </div>
      </div> -->

      <!-- 全屏按钮 -->
      <div class="btn-box screen-box" @click="toggleFullScreen">
        <div class="btn" :class="{ 'full-screen-btn': !isFullscreen, 'exit-full-screen-btn': isFullscreen }">
          <i class="iconfont-sys">{{ isFullscreen ? '&#xe62d;' : '&#xe8ce;' }}</i>
        </div>
      </div>
      <!-- 通知 -->
      <div class="btn-box notice-btn" @click="visibleNotice">
        <div class="btn notice-button">
          <i class="iconfont-sys notice-btn">&#xe6c2;</i>
          <span class="count notice-btn"></span>
        </div>
      </div>
      <!-- 语言 -->
      <div class="btn-box" v-if="showLanguage">
        <el-dropdown @command="changeLanguage" popper-class="langDropDownStyle">
          <div class="btn language-btn">
            <i class="iconfont-sys">&#xe611;</i>
            <!-- <i class="iconfont-sys">&#xe87a;</i> -->
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <div v-for="item in languageOptions" :key="item.value" class="lang-btn-item">
                <el-dropdown-item :command="item.value" :class="{ 'is-selected': locale === item.value }">
                  <span class="menu-txt">{{ item.label }}</span>
                  <i v-if="locale === item.value" class="iconfont-sys">&#xe621;</i>
                </el-dropdown-item>
              </div>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
      <!-- 设置 -->
      <!-- <div class="btn-box" @click="openSetting">
        <el-popover :visible="showSettingGuide" placement="bottom-start" :width="190" :offset="0">
          <template #reference>
            <div class="btn setting-btn">
              <i class="iconfont-sys">&#xe6d0;</i>
            </div>
          </template>
          <template #default>
            <p
              >点击这里查看<span :style="{ color: systemThemeColor }"> 主题风格 </span>、
              <span :style="{ color: systemThemeColor }"> 开启顶栏菜单 </span>等更多配置
            </p>
          </template>
        </el-popover>
      </div> -->
      <!-- 切换主题 -->
      <div class="btn-box" @click="toggleTheme">
        <div class="btn theme-btn">
          <i class="iconfont-sys">{{ isDark ? '&#xe6b5;' : '&#xe725;' }}</i>
        </div>
      </div>

      <!-- 用户头像、菜单 -->
      <div class="user">
        <el-popover
          placement="bottom-end"
          :width="240"
          :hide-after="0"
          :offset="10"
          trigger="hover"
          :show-arrow="false"
          popper-style="border: 1px solid var(--art-border-dashed-color); border-radius: calc(var(--custom-radius) / 2 + 4px); padding: 5px 16px; 5px 16px;"
        >
          <template #reference>
            <img class="cover" :src="userInfo.avatar" />
          </template>
          <template #default>
            <div class="user-menu-box">
              <div class="user-head">
                <img class="cover" :src="userInfo.avatar" style="float: left" />
                <div class="user-wrap">
                  <span class="name">{{ userInfo.username }}</span>
                  <span class="email" v-if="userInfo.email">{{ userInfo.email }}</span>
                </div>
              </div>
              <ul class="user-menu">
                <li @click="goPage('/')">
                  <i class="menu-icon iconfont-sys">&#xe734;</i>
                  <span class="menu-txt">{{ $t('topBar.user.userCenter') }}</span>
                </li>
                <li @click="toDocs()">
                  <i class="menu-icon iconfont-sys" style="font-size: 15px">&#xe828;</i>
                  <span class="menu-txt">{{ $t('topBar.user.docs') }}</span>
                </li>
                <li @click="toGithub()">
                  <i class="menu-icon iconfont-sys">&#xe8d6;</i>
                  <span class="menu-txt">{{ $t('topBar.user.github') }}</span>
                </li>
                <li @click="lockScreen()">
                  <i class="menu-icon iconfont-sys">&#xe817;</i>
                  <span class="menu-txt">{{ $t('topBar.user.lockScreen') }}</span>
                </li>
                <div class="line"></div>
                <div class="logout-btn" @click="loginOut">
                  {{ $t('topBar.user.logout') }}
                </div>
              </ul>
            </div>
          </template>
        </el-popover>
      </div>
    </div>
    <slot></slot>
    <Notice v-model:value="showNotice" ref="notice" />
  </div>
</template>

<script setup lang="ts">
  // import Breadcrumb from '../Breadcrumb/index.vue'
  import Notice from './Notice/index.vue'
  // import MixedMenu from '../MixedMenu/index.vue'
  import { useSettingStore } from '@/stores/setting'
  import { useUserStore } from '@/stores/user'
  import { useMenuStore } from '@/stores/menu'
  import { useTheme } from '@/composables/useTheme'
  import { logout } from '@/utils/storage'
  import { LanguageEnum, MenuTypeEnum, MenuWidth, SystemThemeEnum } from '@/constant/appEnum'
  import { useI18n } from 'vue-i18n'
  import { useFullscreen } from '@vueuse/core'
  import { HOME_PAGE } from '@/router'
  import { useRoute } from 'vue-router'
  // import mittBus from '@/utils/mittBus'
  // import { SystemInfo } from '@/config/setting'

  const route = useRoute()
  const isWindows = navigator.userAgent.includes('Windows')
  const { locale } = useI18n()

  const settingStore = useSettingStore()
  const userStore = useUserStore()
  const router = useRouter()

  // const showMenuButton = computed(() => settingStore.showMenuButton)
  // const showRefreshButton = computed(() => settingStore.showRefreshButton)
  const showLanguage = computed(() => settingStore.showLanguage)
  const isCollapse = computed(() => settingStore.isCollapse)
  // const showCrumbs = computed(() => settingStore.showCrumbs)
  const isDark = computed(() => settingStore.isDark())
  const userInfo = computed(() => userStore.info)
  const language = computed(() => userStore.language)

  // const systemThemeColor = computed(() => settingStore.systemThemeColor)
  // const showSettingGuide = computed(() => settingStore.showSettingGuide)
  const menuList = computed(() => useMenuStore().getMenuList)
  // const menuType = computed(() => settingStore.menuType)
  // const isLeftMenu = computed(() => menuType.value === MenuTypeEnum.LEFT)
  // const isDualMenu = computed(() => menuType.value === MenuTypeEnum.DUAL_MENU)
  // const isTopMenu = computed(() => menuType.value === MenuTypeEnum.TOP)
  // const isTopLeftMenu = computed(() => menuType.value === MenuTypeEnum.TOP_LEFT)
  const { t } = useI18n()
  const { isFullscreen, toggle: toggleFullscreen } = useFullscreen()

  const notice = ref(null)
  const showNotice = ref(false)

  // 根据当前路由分支生成面包屑
  const breadcrumbList = computed(() => {
    return route.matched.filter((item) => item.meta?.title)
  })

  onMounted(() => {
    // initLanguage()
    document.addEventListener('click', closeNotice)
  })

  onUnmounted(() => {
    document.removeEventListener('click', closeNotice)
  })

  const visibleNotice = () => {
    console.log('visibleNotice', showNotice.value)
    showNotice.value = !showNotice.value
  }

  const closeNotice = (e: any) => {
    let { className } = e.target

    if (showNotice.value) {
      if (typeof className === 'object') {
        showNotice.value = false
        return
      }
      if (className.indexOf('notice-btn') === -1) {
        showNotice.value = false
      }
    }
  }

  const changeCollapse = () => {
    settingStore.setIsCollapse(!isCollapse.value)
  }

  const reload = (time: number = 0) => {
    setTimeout(() => {
      settingStore.reload()
    }, time)
  }

  const toggleTheme = () => {
    let { LIGHT, DARK } = SystemThemeEnum
    // useTheme().switchTheme(settingStore.systemThemeType === LIGHT ? DARK : LIGHT)

    settingStore.setGlopTheme(settingStore.isDark() ? LIGHT : DARK, settingStore.isDark() ? LIGHT : DARK)
    ElMessage.success('theme changed')
  }

  const toggleFullScreen = () => {
    toggleFullscreen()
  }

  const initLanguage = () => {
    locale.value = language.value
  }

  const changeLanguage = (lang: LanguageEnum) => {
    ElMessage.success('theme 1')

    if (locale.value === lang) return
    locale.value = lang
    userStore.setLanguage(lang)

    ElMessage.success('theme 2')

    reload(150)
  }

  interface LanguageOption {
    label: string
    value: LanguageEnum
  }

  const languageOptions: LanguageOption[] = [
    { label: '简体中文', value: LanguageEnum.ZH },
    { label: 'English', value: LanguageEnum.EN }
  ]

  const goPage = (path: string) => {
    router.push(path)
  }

  const toDocs = () => {
    window.open('https://example.com/')
  }

  const toGithub = () => {
    window.open('https://github.com/cj180525')
  }

  const lockScreen = () => {
    // mittBus.emit('openLockScreen')
    ElMessage.success('锁屏成功')
  }

  const toHome = () => {
    router.push(HOME_PAGE)
  }

  const loginOut = () => {
    setTimeout(() => {
      ElMessageBox.confirm(t('common.logOutTips'), t('common.tips'), {
        confirmButtonText: t('common.confirm'),
        cancelButtonText: t('common.cancel'),
        customClass: 'login-out-dialog'
      })
        .then(() => {
          ElMessage.success('登出成功')
          logout()
        })
        .catch(() => {
          ElMessage.info('已取消登出')
        })
    }, 200)
  }
</script>

<style lang="scss" scoped>
  // @use './style';
  // @use './mobile';

  .user-menu-box {
    padding-top: 10px;

    .user-head {
      height: 50px;

      .cover {
        width: 50px;
        height: 50px;
        margin: 0 10px 0 0;
        overflow: hidden;
        background: #eee;
        border-radius: 50%;
      }

      .user-wrap {
        span {
          display: block;
        }

        .name {
          font-size: 14px;
          font-weight: 500;
          color: var(--art-gray-800);

          // @include ellipsis();
        }

        .email {
          margin-top: 3px;
          font-size: 12px;
          color: var(--art-gray-500);

          // @include ellipsis();
        }
      }
    }

    .user-menu {
      padding: 16px 0;
      margin-top: 10px;
      border-top: 1px solid var(--art-border-color);

      li {
        display: flex;
        // align-items: center;
        padding: 8px;
        margin-bottom: 10px;
        cursor: pointer;
        // user-select: none;
        border-radius: 6px;

        &:last-of-type {
          margin-bottom: 0;
        }

        i {
          display: block;
          width: 25px;
          font-size: 16px;
          color: var(--art-text-gray-800);
        }

        span {
          font-size: 14px;
          color: var(--art-text-gray-800);
        }

        &:hover {
          background-color: rgb(var(--art-gray-200-rgb), 0.7);
        }
      }

      .line {
        width: 100%;
        height: 1px;
        margin: 10px 0;
        background-color: var(--art-border-color);
      }

      .logout-btn {
        box-sizing: border-box;
        width: 100%;
        padding: 7px 0;
        margin-top: 16px;
        font-size: 13px;
        color: var(--art-text-gray-800);
        text-align: center;
        cursor: pointer;
        border: 1px solid var(--art-border-dashed-color);
        border-radius: 7px;
        transition: all 0.2s;

        &:hover {
          box-shadow: 0 0 10px rgb(var(--art-gray-300-rgb), 0.7);
        }
      }
    }
  }

  .top-bar {
    position: fixed;
    top: 0;
    right: 0;
    z-index: 100;
    position: relative;
    display: flex;
    justify-content: space-between;
    height: 50px;

    .left {
      display: flex;
    }
    .right {
      display: flex;

      .user {
        display: flex;
        align-items: center;
        padding: 0 10px;
        transition: background-color 0.3s;

        &:hover ul {
          height: 80px;
        }

        .cover {
          width: 34px;
          height: 34px;
          margin: 0 10px 0 0;
          overflow: hidden;
          cursor: pointer;
          background: #eee;
          border-radius: 50%;
        }
      }
    }

    .btn-box {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 46px;

      .btn {
        display: block;
        width: 38px;
        height: 38px;
        line-height: 38px;
        text-align: center;
        cursor: pointer;
        border-radius: 6px;
        transition: all 0.2s;

        i {
          display: block;
          font-size: 19px;
          color: var(--art-gray-600);
        }
      }
    }

    .el-breadcrumb {
      display: flex;
      margin-left: 10px;

      ul {
        display: flex;

        li {
          font-size: 13px;
          color: var(--art-text-gray-700) !important;

          span {
            font-size: 13px;
          }

          i {
            margin: 0 7px;
            font-size: 13px;
          }
        }
      }
    }
  }
</style>
