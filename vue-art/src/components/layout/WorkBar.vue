<template>
  <div class="work-tab">
    <div class="scroll-view" ref="scrollRef">
      <ul class="tabs" ref="tabsRef" :style="{ transform: `translateX(${translateX}px)`, transition: `${transition}` }">
        <li
          class="art-custom-card"
          v-for="(item, index) in list"
          :key="item.path"
          :ref="item.path"
          :class="{ 'activ-tab': item.path === activeTab }"
          :id="`scroll-li-${index}`"
          @click="clickTab(item)"
          @contextmenu.prevent="(e: MouseEvent) => showMenu(e, item.path)"
        >
          <!-- {{ formatMenuTitle(item.title) }} -->
          {{ item.name }}
          <el-icon v-if="index !== 0" @click.stop="closeWorkTab('current', item.path)">
            <Close />
          </el-icon>
        </li>
      </ul>
    </div>

    <div class="right">
      <el-dropdown @command="closeWorkTab">
        <el-icon class="btn console-box art-custom-card">
          <ArrowDown />
        </el-icon>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item :icon="ArrowLeft" command="left" :disabled="activeTabIndex === 0 || activeTabIndex === 1">
              <span>{{ $t('workTab.btn[0]') }}</span>
            </el-dropdown-item>
            <el-dropdown-item :icon="ArrowRight" command="right" :disabled="activeTabIndex === list.length - 1">
              <span>{{ $t('workTab.btn[1]') }}</span>
            </el-dropdown-item>
            <el-dropdown-item
              :icon="Close"
              command="other"
              :disabled="list.length === 1 || (list.length === 2 && activeTabIndex === 1)"
            >
              <span>{{ $t('workTab.btn[2]') }}</span>
            </el-dropdown-item>
            <el-dropdown-item :icon="CircleClose" command="all" :disabled="list.length === 1">
              <span>{{ $t('workTab.btn[3]') }}</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
    <MenuRight ref="menuRef" :menu-items="menuItems" @select="handleSelect" />
  </div>
</template>

<script setup lang="ts">
  import { ArrowDown, ArrowLeft, ArrowRight, Close, CircleClose } from '@element-plus/icons-vue'
  import { useRoute, useRouter } from 'vue-router'
  import { useI18n } from 'vue-i18n'

  import { useWorkTabStore } from '@/stores/workTab'
  import { useUserStore } from '@/stores/user'
  import { formatMenuTitle } from '@/utils/menu'
  import type { WorkTabType } from '@/stores/workTab'
  import type { MenuItemType } from '@/types/model/menu'

  const { t } = useI18n()
  const workTabStore = useWorkTabStore()
  const userStore = useUserStore()
  const route = useRoute()
  const router = useRouter()
  const { currentRoute } = router

  // 初始化状态和引用
  const scrollRef = ref<HTMLElement | null>(null) // 滚动容器引用
  const tabsRef = ref<HTMLElement | null>(null) // 标签列表容器引用
  const menuRef = ref() // 右键菜单引用

  // 滚动相关状态
  const translateX = ref(0) // 标签列表水平偏移量
  const transition = ref('') // 过渡动画样式
  const clickedPath = ref('') // 当前点击的标签路径
  let startX = 0 // 触摸开始位置
  let currentX = 0 // 当前触摸位置

  // 打开的标签页列表
  const list = computed(() => workTabStore.opened)

  console.log('list', list.value)

  // 当前激活的标签页
  const activeTab = computed(() => currentRoute.value.path)

  console.log('activeTab', activeTab.value)

  // 获取当前激活 tab 的 index
  const activeTabIndex = computed(() => list.value.findIndex((tab) => tab.path === activeTab.value))

  // 右键菜单选项
  const menuItems = computed(() => {
    const clickedIndex = list.value.findIndex((tab) => tab.path === clickedPath.value)
    const isLastTab = clickedIndex === list.value.length - 1
    const isFirstOrSecondTab = clickedIndex === 0 || clickedIndex === 1
    const isOneTab = list.value.length === 1
    const disableOther = list.value.length === 2 && clickedIndex === 1

    return [
      {
        key: 'left',
        label: t('workTab.btn[0]'),
        icon: 'ArrowLeft',
        disabled: isFirstOrSecondTab
      },
      {
        key: 'right',
        label: t('workTab.btn[1]'),
        icon: 'ArrowRight',
        disabled: isLastTab
      },
      {
        key: 'other',
        label: t('workTab.btn[2]'),
        icon: 'Close',
        disabled: isOneTab || disableOther
      },
      {
        key: 'all',
        label: t('workTab.btn[3]'),
        icon: 'CircleClose',
        disabled: isOneTab
      }
    ]
  })

  // 获取当前标签页索引和元素
  const getCurTabEl = () => document.getElementById(`scroll-li-${activeTabIndex.value}`) as HTMLElement

  // 设置过渡动画
  const setTransition = () => {
    transition.value = 'transform 0.5s ease-in-out'
    setTimeout(() => {
      transition.value = ''
    }, 300)
  }

  // 自动定位当前标签页到可视区域
  const workTabAutoPosition = () => {
    if (!scrollRef.value || !tabsRef.value) return

    const scrollWidth = scrollRef.value.offsetWidth
    const ulWidth = tabsRef.value.offsetWidth
    const curTabEl = getCurTabEl()

    if (!curTabEl) return

    const { offsetLeft, clientWidth } = curTabEl
    const curTabRight = offsetLeft + clientWidth
    const targetLeft = scrollWidth - curTabRight

    if (
      (offsetLeft > Math.abs(translateX.value) && curTabRight <= scrollWidth) ||
      (translateX.value < targetLeft && targetLeft < 0)
    )
      return

    requestAnimationFrame(() => {
      if (curTabRight > scrollWidth) {
        translateX.value = Math.max(targetLeft - 6, scrollWidth - ulWidth)
      } else if (offsetLeft < Math.abs(translateX.value)) {
        translateX.value = -offsetLeft
      }
    })
  }

  // 生命周期钩子
  onMounted(() => {
    listenerScroll() // 监听滚动事件
    addTouchListeners() // 添加触摸事件监听
    workTabAutoPosition() // 初始定位
  })

  // 监听路由变化，自动定位标签页
  watch(
    () => currentRoute.value,
    () => {
      setTransition()
      workTabAutoPosition()
    }
  )

  //   // 监听语言变化，重置标签页位置
  //   watch(
  //     () => userStore.language,
  //     () => {
  //       translateX.value = 0
  //       nextTick(() => {
  //         workTabAutoPosition()
  //       })
  //     }
  //   )

  // 标签页操作方法
  const clickTab = (item: WorkTabType) => {
    router.push({
      path: item.path,
      query: item.query
    })
  }

  // 关闭标签页的不同方式
  const closeWorkTab = (type: string, tabPath: string) => {
    let path = typeof tabPath === 'string' ? tabPath : route.path

    switch (type) {
      case 'current':
        workTabStore.removeTab(path)
        break
      case 'left':
        workTabStore.removeLeft(path)
        break
      case 'right':
        workTabStore.removeRight(path)
        break
      case 'other':
        workTabStore.removeOthers(path)
        break
      case 'all':
        workTabStore.removeAll(path)
        break
    }

    setTimeout(() => {
      workTabClosePosition()
    }, 100)
  }

  // 关闭标签页后的位置调整
  const workTabClosePosition = () => {
    if (!scrollRef.value || !tabsRef.value) return

    const curTabEl = getCurTabEl()
    if (!curTabEl) return

    const { offsetLeft, clientWidth } = curTabEl
    const scrollWidth = scrollRef.value.offsetWidth
    const ulWidth = tabsRef.value.offsetWidth
    const curTabLeft = offsetLeft + clientWidth

    requestAnimationFrame(() => {
      translateX.value = curTabLeft > scrollWidth ? scrollWidth - ulWidth : 0
    })
  }

  // 右键菜单相关方法
  const showMenu = (e: MouseEvent, path?: string) => {
    clickedPath.value = path || ''
    menuRef.value?.show(e)
    e.preventDefault()
    e.stopPropagation()
  }

  const handleSelect = (item: MenuItemType) => {
    const { key } = item
    const activeIndex = list.value.findIndex((tab) => tab.path === activeTab.value)
    const clickedIndex = list.value.findIndex((tab) => tab.path === clickedPath.value)

    // 处理标签跳转逻辑
    const shouldNavigate =
      (key === 'left' && activeIndex < clickedIndex) ||
      (key === 'right' && activeIndex > clickedIndex) ||
      key === 'other'

    if (shouldNavigate) {
      router.push(clickedPath.value)
    }

    // 关闭标签页
    closeWorkTab(key, clickedPath.value)
  }

  // 滚动事件处理
  const listenerScroll = () => {
    const xMax = 0

    if (tabsRef.value) {
      tabsRef.value.addEventListener(
        'wheel',
        (event: WheelEvent) => {
          if (scrollRef.value && tabsRef.value) {
            event.preventDefault()

            if (tabsRef.value.offsetWidth <= scrollRef.value.offsetWidth) return

            const xMin = scrollRef.value.offsetWidth - tabsRef.value.offsetWidth
            // 使用 deltaX 来处理水平滚动，使用 deltaY 来处理垂直滚动
            const delta = Math.abs(event.deltaX) > Math.abs(event.deltaY) ? event.deltaX : event.deltaY
            translateX.value = Math.min(Math.max(translateX.value - delta, xMin), xMax)
          }
        },
        { passive: false }
      )
    }
  }

  // 触摸事件处理
  const addTouchListeners = () => {
    if (tabsRef.value) {
      tabsRef.value.addEventListener('touchstart', handleTouchStart)
      tabsRef.value.addEventListener('touchmove', handleTouchMove)
      tabsRef.value.addEventListener('touchend', handleTouchEnd)
    }
  }

  const handleTouchStart = (event: TouchEvent) => {
    startX = event.touches[0].clientX
  }

  const handleTouchMove = (event: TouchEvent) => {
    if (!scrollRef.value || !tabsRef.value) return

    currentX = event.touches[0].clientX
    const deltaX = currentX - startX
    const xMin = scrollRef.value.offsetWidth - tabsRef.value.offsetWidth

    translateX.value = Math.min(Math.max(translateX.value + deltaX, xMin), 0)
    startX = currentX
  }

  const handleTouchEnd = () => {
    setTransition()
  }
</script>

<style lang="scss" scoped>
  .work-tab {
    box-sizing: border-box;
    display: flex;
    justify-content: space-between;
    width: 100%;
    padding: 0 20px;
    user-select: none;
    background: var(--art-bg-color);

    .scroll-view {
      width: 100%;
      overflow: hidden;

      .tabs {
        float: left;
        white-space: nowrap;
        background: transparent !important;

        li {
          display: inline-block;
          height: 28px;
          padding: 0 8px 0 12px;
          margin-right: 6px;
          font-size: 13px;
          line-height: 28px;
          color: var(--art-text-gray-600);
          text-align: center;
          cursor: pointer;
          background: var(--art-main-bg-color);
          border-radius: calc(var(--custom-radius) / 2.5 + 2px) !important;
          transition: color 0.1s;

          &:hover {
            color: var(--main-color) !important;
            transition: color 0.2s;
          }

          i {
            position: relative;
            top: 2px;
            padding: 2px;
            margin-left: 5px;
            border-radius: 50%;
            transition: all 0.2s;

            &:hover {
              background: rgb(238 238 238 / 100%);
            }
          }
        }

        .activ-tab {
          color: var(--main-color) !important;
        }
      }
    }

    .right {
      display: flex;

      .btn {
        position: relative;
        top: 0;
        box-sizing: border-box;
        width: 28px;
        height: 28px;
        font-size: 16px;
        line-height: 28px;
        text-align: center;
        cursor: pointer;
        background: var(--art-main-bg-color);
        // border: 1px solid var(--art-border-dashed-color) !important;
        border-radius: 3px;

        &:hover ul {
          display: inline;
        }

        &.history {
          color: #666;
        }
      }
    }
  }

  .dark {
    .tabs {
      li {
        i {
          &:hover {
            background: rgb(238 238 238 / 10%) !important;
          }
        }
      }
    }
  }

  @media only screen and (max-width: $device-pad) {
    .workTab {
      padding: 6px 10px;
    }
  }

  @media only screen and (max-width: $device-phone) {
    .workTab {
      padding: 0 15px;
    }
  }
</style>
