import { $t } from '@/language'
import type { MenuListType } from '@/types/model/menu'
import { KeepAlive } from 'vue'

const modules: Record<string, () => Promise<any>> = import.meta.glob('../views/**/*.vue')
const commonPageRoutes: MenuListType[] = []
const iframeRoutes: MenuListType[] = []

// 创建递归函数处理嵌套路由
/**
 * 处理路由配置,转换为菜单数据结构
 * @param route 路由配置对象
 * @param parentPath 父级路径
 * @returns 处理后的菜单项
 */
export const processRoute = (route: MenuListType, parentPath = ''): MenuListType => {
  let currentPath = ''
  if (route.type === 3) {
    currentPath = `/common/commonPage/${route.path}`
  } else if (route.type === 4) {
    currentPath = `/outside/iframe/${route.path}`
  } else {
    if (parentPath) {
      currentPath = `/${parentPath}/${route.path}`.replace(/\/+/g, '/') // 规范化路径,避免多余的斜杠
    } else {
      currentPath = `/${route.path}`.replace(/\/+/g, '/')
    }
  }

  return {
    id: route.id, // 使用空值合并运算符
    name: route.name,
    path: currentPath,
    component: route.component,
    meta: {
      title: formatMenuTitle(route.title ?? ''),
      icon: route.icon,
      type: route.type,
      status: route.status,
      link: route.link,
      isHideTab: route.isHideTab,
      keepAlive: route.keepAlive
    }, // 使用空值合并运算符
    children: Array.isArray(route.children) ? route.children.map((child) => processRoute(child, currentPath)) : []
  }
}

/**
 * 递归注册路由
 */
export const generateRoutes = (routes: MenuType[]): RouteRecordRaw => {
  const newRoutes: RouteRecordRaw[] = []
  routes.forEach((route) => {
    const newRoute: RouteRecordRaw = {
      ...route,
      component: loadComponent(route.component, route.meta?.type),
      children: route.children ? generateRoutes(route.children) : []
    }
    if (route.meta?.type === 3) {
      commonPageRoutes.push(newRoute) // 保存 commonPage 路由到数组中
    } else if (route.meta?.type === 4) {
      console.log('333', route)
      iframeRoutes.push(newRoute) // 保存 iframe 路由到数组中
    }

    newRoutes.push(newRoute)
  })

  return newRoutes
}

/**
 * 根据组件路径动态加载组件
 * @param componentPath 组件路径（不包含 ../../views 前缀和 .vue 后缀）
 * @param type 菜单类型 (1:目录，2:页面，3:内嵌，4:外链)
 * @returns 组件加载函数
 */
function loadComponent(componentPath: string, type: number): () => Promise<any> {
  switch (type) {
    case 1:
      return () => import('@/views/common/Router.vue')
    case 2:
      return handleNormalRoute(componentPath)
    case 3:
      return () => import('@/views/common/commonPage.vue')
    case 4:
      return () => import('@/views/outside/Iframe.vue')
    default:
      return () => import('@/views/page/exception/500.vue')
  }
}

/**
 * 动态导入 views 目录下所有 .vue 组件
 */
function handleNormalRoute(componentPath: string): () => Promise<any> {
  const fullPath = `../views${componentPath}.vue`
  const module = modules[fullPath]
  if (!module && componentPath !== '') {
    console.error(`未找到组件：${fullPath}`)
  }
  return module as () => Promise<any>
}

/**
 * 保存 iframe 路由到 sessionStorage 中
 * @param list iframe 路由列表
 */
export const saveIframeRoutes = (): void => {
  if (iframeRoutes.length > 0) {
    sessionStorage.setItem('iframeRoutes', JSON.stringify(iframeRoutes))
  }
}
/**
 * 获取 iframe 路由
 */
export const getIframeRoutes = () => {
  return JSON.parse(sessionStorage.getItem('iframeRoutes') || '[]')
}

/**
 * 保存 commonPage 路由到 sessionStorage 中
 * @param list commonPage 路由列表
 */
export const saveCommonPageRoutes = (): void => {
  if (iframeRoutes.length > 0) {
    sessionStorage.setItem('commonPageRoutes', JSON.stringify(commonPageRoutes))
  }
}
/**
 * 获取 commonPage 路由
 */
export const getCommonPageRoutes = () => {
  return JSON.parse(sessionStorage.getItem('commonPageRoutes') || '[]')
}

/**
 * 格式化菜单标题
 * @param title 菜单标题，可以是 i18n 的 key，也可以是字符串，比如：'用户列表'
 * @returns 格式化后的菜单标题
 */
export const formatMenuTitle = (title: string) => {
  // console.log('$t', $t)
  return title.startsWith('menus.') ? $t(title) : title
}
