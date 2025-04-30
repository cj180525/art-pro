import { defineStore } from 'pinia'
import { ThemeList, ElementPlusTheme, DarkMenuStyles, SystemSetting } from '@/config/setting'
import { SystemThemeEnum, MenuThemeEnum, MenuTypeEnum, ContainerWidthEnum } from '@/constant/appEnum'

export interface SettingState {
  systemThemeType: SystemThemeEnum // 全局主题类型 light dark
  systemThemeColor: string // 系统主题颜色
  refresh: boolean // 刷新当前页
  isCollapse: boolean // 菜单是否折叠
  showLanguage: boolean // 是否显示语言切换
  showWorkTab: boolean // 是否显示多标签
  showNprogress: boolean // 是否显示进度条
}

export const useSettingStore = defineStore('settingStore', {
  state: (): SettingState => ({
    systemThemeType: SystemThemeEnum.LIGHT,
    systemThemeColor: ElementPlusTheme.primary,
    refresh: true,
    isCollapse: false,
    showLanguage: true,
    showWorkTab: true,
    showNprogress: true
  }),
  getters: {},
  actions: {
    // 刷新当前页
    reload() {
      this.refresh = !this.refresh
    },
    // 设置系统主题
    setGlopTheme(theme: SystemThemeEnum, themeMode: SystemThemeEnum) {
      this.systemThemeType = theme
      this.systemThemeMode = themeMode
    },
    // 设置菜单是否展开
    setIsCollapse(bool: boolean) {
      this.isCollapse = bool
    },
    // 是否显示多标签
    setWorkTab(show: boolean) {
      this.showWorkTab = show
    },
    // 是否为暗黑模式
    isDark(): boolean {
      return this.systemThemeType === SystemThemeEnum.DARK
    }
  },
  persist: {
    enabled: true, // 确保持久化启用
    strategies: [
      {
        key: 'settingStore', // 存储的键名
        storage: localStorage // 使用 localStorage
      }
    ]
  }
})
