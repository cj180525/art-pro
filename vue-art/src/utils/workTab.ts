import { useWorkTabStore } from '@/stores/workTab'
import type { RouteLocationNormalized } from 'vue-router'
import { getIframeRoutes } from './menu'
import { isIframe } from './utils'
import { useSettingStore } from '@/stores/setting'
import { HOME_PAGE } from '@/router'

/**
 * 根据当前路由信息设置工作标签页（workTab）
 * @param to 当前路由对象
 */
export const setWorkTab = (to: RouteLocationNormalized): void => {
  const workTabStore = useWorkTabStore()
  const { meta, path, name, params, query } = to

  if (!meta.isHideTab) {
    // 如果是 iframe 页面，则特殊处理工作标签页
    if (isIframe(path)) {
      const iframeRoute = getIframeRoutes().find((route: any) => route.path === to.path)

      if (iframeRoute?.meta) {
        workTabStore.openTab({
          title: iframeRoute.meta.title,
          path,
          name: name as string,
          keepAlive: meta.keepAlive as boolean,
          params,
          query
        })
      }
    } else if (useSettingStore().showWorkTab || path === HOME_PAGE) {
      workTabStore.openTab({
        title: meta.title as string,
        path,
        name: name as string,
        keepAlive: meta.keepAlive as boolean,
        params,
        query
      })
    }
  }
}
