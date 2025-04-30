import { defineStore } from 'pinia'
import type { MenuType } from '@/types/model/menu'

interface MenuState {
  menuList: MenuType[]
  menuWidth: string
}

export const useMenuStore = defineStore('menuStore', {
  state: (): MenuState => ({
    menuList: [],
    menuWidth: ''
  }),
  getters: {
    getMenuList(): MenuType[] {
      return this.menuList
    }
  },
  actions: {
    setMenuList(list: []) {
      this.menuList = list
    },
    setMenuWidth(width: string) {
      this.menuWidth = width
    }
  }
})
