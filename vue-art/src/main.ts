import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

import App from './App.vue'
import router from './router'
import language from './language'
import { setupGlobDirectives } from './directives'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

// import 'default-passive-events'                 // 优化滚动性能

import '@styles/reset.scss'                     // 重置HTML样式
import '@styles/app.scss'                       // 全局样式
// import '@styles/pages.scss'                  // 公共页面样式
import '@styles/el-ui.scss'                     // 优化element样式
// import '@styles/mobile.scss'                 // 移动端样式优化
// import '@styles/change.scss'                 // 主题切换过渡优化
import '@icons/iconfont.js'                     // 系统彩色图标
import '@icons/iconfont.css'                    // 系统图标
// import '@styles/el-light.scss'               // Element 自定义主题（亮色）
// import '@styles/el-dark.scss'                // Element 自定义主题（暗色）
// import '@styles/dark.scss'                   // 系统主题
// import '@utils/console.ts'                   // 控制台输出内容


const app = createApp(App)
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate) // Pinia自动持久化插件

app.use(pinia)
app.use(router)
app.use(language)

setupGlobDirectives(app) // 注册全局指令


// 注册全局 ElementPlus 图标组件
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}


document.documentElement.style.setProperty('--custom-radius', `0.5rem`) // 设置全局圆角
document.documentElement.setAttribute('data-box-mode', 'border-mode') // 设置全局盒子模型

// import ElementPlus from 'element-plus'
// app.use(ElementPlus, { size: 'default' }) 

app.mount('#app')
