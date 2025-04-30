import { defineStore } from 'pinia'
import router from '@/router'
import { HOME_PAGE } from '@/router'

export type WorkTabType = {
  title: string
  path: string
  name: string
  // keepAlive: boolean
  // params?: object
  // query?: object
}

interface WorkTabState {
  opened: WorkTabType[]
  current: Partial<WorkTabType>
  // keepAliveExclude: string[]
}

export const useWorkTabStore = defineStore('workTabStore', {
  state: (): WorkTabState => ({
    current: {},
    opened: []
    // keepAliveExclude: []
  }),
  getters: {},
  actions: {
    /**
     * 打开或激活一个选项卡
     * @param tab 目标选项卡信息
     */
    openTab(tab: WorkTabType): void {
      this.removeKeepAliveExclude(tab.name as string)

      const index = this.opened.findIndex((item) => item.path === tab.path)

      if (index === -1) {
        this.opened.push({ ...tab })
      } else {
        const existingTab = this.opened[index]
        if (!this.areQueriesEqual(existingTab.query, tab.query)) {
          this.opened[index] = {
            ...existingTab,
            query: tab.query,
            // 可选：支持更多属性更新
            title: tab.title || existingTab.title
          }
        }
      }

      this.current = this.opened[index === -1 ? this.opened.length - 1 : index]
    },

    // 辅助函数
    areQueriesEqual(query1: any, query2: any): boolean {
      return JSON.stringify(query1) === JSON.stringify(query2)
    },

    /**
     * 关闭指定的选项卡，并处理激活状态和路由跳转
     * @param path 要关闭的路由路径
     */
    removeTab(path: string) {
      const noCurrentTab = this.opened.find((tab) => tab.path === path)
      const index = this.opened.findIndex((tab) => tab.path === path)
      if (index === -1) return

      this.opened.splice(index, 1)

      // 若关闭后无选项卡，且关闭的不是首页，则跳转首页
      if (!this.opened.length && path !== HOME_PAGE) {
        router.push(HOME_PAGE)
        return
      }

      // 若关闭的是当前激活标签，则标记其为缓存排除，并激活相邻标签
      if (this.current.path === path) {
        if (this.current.name) {
          this.addKeepAliveExclude(this.current as WorkTabType)
        }
        const newIndex = index >= this.opened.length ? this.opened.length - 1 : index
        this.current = this.opened[newIndex]
        router.push(this.current.path as string)
      } else {
        if (noCurrentTab?.name) {
          this.addKeepAliveExclude(noCurrentTab)
        }
      }
    },

    /**
     * 关闭当前标签左侧（不包括首页）的所有选项卡
     * @param path 当前选项卡的路由路径
     */
    removeLeft(path: string) {
      const index = this.opened.findIndex((tab) => tab.path === path)
      if (index > 1) {
        // 保留首页和当前标签
        const tabsToRemove = this.opened.slice(1, index)
        this.markTabsToRemove(tabsToRemove)
        this.opened.splice(1, index - 1)
      }
    },

    /**
     * 关闭当前标签右侧的所有选项卡
     * @param path 当前选项卡的路由路径
     */
    removeRight(path: string) {
      const index = this.opened.findIndex((tab) => tab.path === path)
      if (index !== -1 && index < this.opened.length - 1) {
        const tabsToRemove = this.opened.slice(index + 1)
        this.markTabsToRemove(tabsToRemove)
        this.opened.splice(index + 1)
      }
    },

    /**
     * 关闭除当前标签和首页外的所有选项卡
     * @param path 当前选项卡的路由路径
     */
    removeOthers(path: string) {
      const tabsToRemove = this.opened.filter((tab) => tab.path !== path && tab.path !== HOME_PAGE)
      this.markTabsToRemove(tabsToRemove)
      this.opened = this.opened.filter((tab) => tab.path === path || tab.path === HOME_PAGE)
    },

    /**
     * 关闭所有选项卡（当传入的 path 不是首页时关闭全部；首页时只保留首页）
     * @param path 当前选项卡的路由路径
     */
    removeAll(path: string) {
      if (path !== HOME_PAGE) {
        this.markTabsToRemove(this.opened)
        this.current = {}
        this.opened = []
        router.push(HOME_PAGE)
      } else {
        const tabsToRemove = this.opened.filter((tab) => tab.path !== HOME_PAGE)
        this.markTabsToRemove(tabsToRemove)
        this.opened = this.opened.filter((tab) => tab.path === HOME_PAGE)
        if (this.opened.length === 0) router.push(HOME_PAGE)
      }
    },

    /**
     * 检查第一个选项卡是否为首页，否则清空所有标签并跳转首页
     */
    checkFirstHomePage() {
      if (this.opened.length && this.opened[0].path !== HOME_PAGE) {
        this.removeAll(HOME_PAGE)
      }
    },

    /**
     * 将指定选项卡添加到 keepAlive 排除列表中，只有当该选项卡的 keepAlive 为 true 时才进行添加
     * @param tab 选项卡对象
     */
    addKeepAliveExclude(tab: WorkTabType) {
      // if (tab.keepAlive && tab.name && !this.keepAliveExclude.includes(tab.name)) {
      //   this.keepAliveExclude.push(tab.name)
      // }
    },

    /**
     * 从 keepAlive 排除列表中移除指定组件名称
     * @param name 路由组件名称
     */
    removeKeepAliveExclude(name: string) {
      // this.keepAliveExclude = this.keepAliveExclude.filter((item) => item !== name)
    },

    /**
     * 将传入的一组选项卡的组件名称标记为排除缓存
     * @param tabs 需要标记的选项卡数组
     */
    markTabsToRemove(tabs: WorkTabType[]) {
      tabs.forEach((tab) => {
        if (tab.name) {
          this.addKeepAliveExclude(tab)
        }
      })
    }
  },
  persist: {
    storage: sessionStorage // 👈 这里指定用 sessionStorage
  }
})
