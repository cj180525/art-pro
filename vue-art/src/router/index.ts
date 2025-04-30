import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

import { createRouter, createWebHistory, createWebHashHistory } from 'vue-router'
import type { Router, RouteRecordRaw } from 'vue-router'
import { useSettingStore } from '@/stores/setting'
import { useUserStore } from '@/stores/user'
import { useMenuStore } from '@/stores/menu'
import { logout } from '@/utils/storage'
import { processRoute, generateRoutes, saveIframeRoutes, saveCommonPageRoutes } from '@/utils/menu'
import { ApiStatus } from '@/constant/apiStatusEnum'
import { setWorkTab } from '@/utils/workTab'
import { getTokenCookie } from '@/utils/cookie'
import { menuService } from '@/api/system/menuApi'
import type { MenuType } from '@/types/model/menu'

export const HOME_PAGE = '/dashboard/console'

const staticRoutes = [
  {
    path: '/auth',
    name: 'auth',
    redirect: '/auth/login',
    component: () => import('@/views/auth/index.vue'),
    children: [
      {
        path: 'login',
        name: 'login',
        component: () => import('@/views/auth/LoginWrap.vue'),
        meta: { title: 'menus.login.title', noLogin: true, isHideTab: true }
      },
      {
        path: 'register',
        name: 'register',
        component: () => import('@/views/auth/RegisterWrap.vue'),
        meta: { title: 'menus.register.title', noLogin: true, isHideTab: true }
      },
      {
        path: '/forget-password',
        name: 'forgetPassword',
        component: () => import('@/views/auth/ForgetPasswordWrap.vue'),
        meta: { title: 'menus.forgetPassword.title', isHideTab: true, noLogin: true, setTheme: true }
      }
    ]
  },

  {
    path: '/',
    name: '/',
    redirect: HOME_PAGE
  },
  {
    path: '/index',
    name: 'index',
    redirect: HOME_PAGE,
    component: () => import('@/views/index.vue')
  },
  {
    path: '/:pathMatch(.*)*',
    // name: 'NotFound', // 防止控制台找不到路由警告 去掉name才有效
    meta: {
      isHide: true
    },
    component: () => import('@/views/page/exception/404.vue')
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  // history: createWebHistory(import.meta.env.BASE_URL),
  routes: staticRoutes
})

const isRouteRegistered = ref(false) // 标记是否已经注册动态路由

/**
 * 路由全局前置守卫
 * 处理进度条、获取菜单列表、动态路由注册、404 检查、工作标签页及页面标题设置
 */
router.beforeEach(async (to, from, next) => {
  const settingStore = useSettingStore()
  if (settingStore.showNprogress) NProgress.start()

  // 设置登录注册页面主题
  // setSystemTheme(to)

  console.log('router1====>', to.fullPath, to.name)

  // 检查登录状态，如果未登录则跳转到登录页
  const userStore = useUserStore()
  const token = getTokenCookie()

  if (!token && to.meta?.noLogin !== true) {
    console.log('12')

    logout()
    return next('/auth/login')
  }

  console.log('router2====>')

  // 如果用户已登录且动态路由未注册，则注册动态路由
  if (!isRouteRegistered.value && token) {
    await getMenuData()
    if (to.name === 'Exception404') {
      return next({ path: to.path, query: to.query, replace: true })
    } else {
      return next({ ...to, replace: true })
    }
  }

  // 检查路由是否存在，若不存在则跳转至404页面
  if (to.matched.length === 0) {
    return next('/page/exception/404')
  }

  // 设置工作标签页和页面标题
  setWorkTab(to)
  // setPageTitle(to)

  next()
})

/**
 * 根据接口返回的菜单列表注册动态路由
 * @throws 若菜单列表为空或获取失败则抛出错误
 */
async function getMenuData(): Promise<void> {
  try {
    const res = await menuService.getMenuTree()

    if (res && res.code === ApiStatus.success && res.data && res.data.length > 0) {
      // 处理菜单列表
      const menuList: MenuType[] = res.data.map((route) => processRoute(route))
      // const menuList: MenuType[] = res.data

      // 设置菜单列表
      useMenuStore().setMenuList(menuList)

      // // 关闭加载动画
      // closeLoading()

      const dynamicRoutes = generateRoutes(menuList)

      // 动态添加到 router
      dynamicRoutes.forEach((route) => {
        router.addRoute('index', route)
      })

      saveIframeRoutes()
      saveCommonPageRoutes()

      // 标记路由已注册
      isRouteRegistered.value = true

      // console.log('menuList', menuList)
      console.log('router', router.getRoutes())

      // 关闭加载动画
      // closeLoading()
    } else {
      // closeLoading()
      logout()
      throw new Error('获取菜单列表失败，请重新登录！')
    }
  } catch (error) {
    console.error('获取菜单列表失败:', error)
    throw error
  }
}

/**
 * 路由全局后置守卫
 */
router.afterEach(() => {
  if (useSettingStore().showNprogress) NProgress.done()
})

export default router
