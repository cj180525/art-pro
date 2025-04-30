import { defineStore } from 'pinia'
import { LanguageEnum } from '@/constant/appEnum'
// import { router, setPageTitle } from '@/router'
import type { UserInfo } from '@/types/model/store'
// import type { MenuType } from '@/types/model/menu'

interface UserState {
  info: Partial<UserInfo> // 用户信息
  language: LanguageEnum // 语言
  // isLock: boolean // 是否锁屏
  // lockPassword: string // 锁屏密码
  // searchHistory: MenuType[] // 搜索历史
  // refreshToken: string // 添加刷新令牌
}

export const useUserStore = defineStore('userStore', {
  state: (): UserState => ({
    info: {},
    language: LanguageEnum.ZH
    // isLock: false,
    // lockPassword: '',
    // searchHistory: [],
    // refreshToken: ''
  }),
  getters: {},
  actions: {
    setUserInfo(info: UserInfo) {
      this.info = info
    },
    setLanguage(lang: LanguageEnum) {
      // setPageTitle(router.currentRoute.value)
      this.language = lang
    }
    // setSearchHistory(list: Array<MenuType>) {
    //   this.searchHistory = list
    // },
    // setLockStatus(isLock: boolean) {
    //   this.isLock = isLock
    // },
    // setLockPassword(password: string) {
    //   this.lockPassword = password
    // },
  },
  persist: {
    enabled: true, // 确保持久化启用
    strategies: [
      {
        key: 'userStore2', // 存储的键名
        storage: localStorage // 使用 localStorage
      }
    ]
  }
})
