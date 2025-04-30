<template>
  <div class="auth-content">
    <div class="left-wrap">
      <left-view></left-view>
    </div>

    <div class="right-wrap">
      <router-view></router-view>

      <div class="right-top-wrap">
        <div class="btn theme-btn">
          <i class="iconfont-sys">
            {{ isDark ? '&#xe6b5;' : '&#xe725;' }}
          </i>
        </div>
        <div class="btn theme-btn">
          <i class="iconfont-sys">
            {{ isDark ? '&#xe6b5;' : '&#xe725;' }}
          </i>
        </div>
        <div class="btn theme-btn" @click="toggleTheme">
          <i class="iconfont-sys">
            {{ isDark ? '&#xe6b5;' : '&#xe725;' }}
          </i>
        </div>
        <el-dropdown @command="changeLanguage" popper-class="langDropDownStyle">
          <div class="btn language-btn">
            <i class="iconfont-sys icon-language">&#xe611;</i>
            <!-- <i class="iconfont-sys icon-language">&#xe87a;</i> -->
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <div v-for="lang in languageOptions" :key="lang.value" class="lang-btn-item">
                <el-dropdown-item :command="lang.value" :class="{ 'is-selected': locale === lang.value }">
                  <span class="menu-txt">{{ lang.label }}</span>
                  <i v-if="locale === lang.value" class="iconfont-sys icon-check">&#xe621;</i>
                </el-dropdown-item>
              </div>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { useSettingStore } from '@/stores/setting'
  import { LanguageEnum, SystemThemeEnum } from '@/constant/appEnum'
  import { useI18n } from 'vue-i18n'

  const settingStore = useSettingStore()
  const { t, locale } = useI18n()

  const isDark = computed(() => settingStore.isDark)

  // 语言配置
  const languageOptions = [
    { value: LanguageEnum.ZH, label: '简体中文' },
    { value: LanguageEnum.EN, label: 'English' }
  ]

  // // 切换主题
  // // import { useTheme } from '@/composables/useTheme'

  const toggleTheme = () => {
    let { LIGHT, DARK } = SystemThemeEnum
    ElMessage({
      message: '切换主题成功',
      type: 'success'
    })
    // useTheme().switchTheme(useSettingStore().systemThemeType === LIGHT ? DARK : LIGHT)
  }

  // 切换语言
  const changeLanguage = (lang: LanguageEnum) => {
    if (locale.value === lang) return
    locale.value = lang
    userStore.setLanguage(lang)
  }
</script>

<style lang="scss" scoped>
  .auth-content {
    display: flex;
    width: 100%;
    height: 100%;

    .left-wrap {
      width: 65vw;
      overflow: hidden;
    }

    .right-wrap {
      flex: 1;
      overflow: auto;

      .right-top-wrap {
        position: fixed;
        top: 30px;
        right: 30px;
        background-color: grey;
        z-index: 100;
        display: flex;
        align-items: center;
        justify-content: flex-end;

        .btn {
          display: inline-block;
          padding: 8px;
          // margin-left: 15px;
          cursor: pointer;
          user-select: none;
          transition: all 0.3s;

          i {
            font-size: 18px;
          }

          //   &:hover {
          //     color: var(--main-color) !important;
          //   }
        }
      }
    }
  }

  @media only screen and (max-width: $device-pad) {
    .left-wrap {
      display: none;
    }
  }
</style>
