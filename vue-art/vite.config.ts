import { fileURLToPath, URL } from 'node:url'
import path from 'path'

import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import Components from 'unplugin-vue-components/vite'
import AutoImport from 'unplugin-auto-import/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

// https://vite.dev/config/
export default ({ mode }: { mode: string }) => {
  const { VITE_VERSION, VITE_API_URL, VITE_PORT, VITE_BASE_URL } = loadEnv(mode, process.cwd())

  return defineConfig({
    define: {
      __APP_VERSION__: JSON.stringify(VITE_VERSION)
    },
    base: VITE_BASE_URL,
    server: {
      port: parseInt(VITE_PORT),
      proxy: {
        '/api': {
          target: VITE_API_URL,
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/api/, '')
        }
      },
      host: true
    },
    plugins: [
      vue(),
      AutoImport({
        imports: ['vue', 'vue-router', '@vueuse/core', 'pinia'], // 需要自动导入的 API
        resolvers: [ElementPlusResolver()],
        dts: 'src/types/auto-imports.d.ts',
        eslintrc: {
          // 这里先设置成 true 然后pnpm dev 运行之后会生成 .auto-import.json 文件之后，在改为 false
          enabled: false,
          filepath: './.auto-import.json',
          globalsPropValue: true
        }
      }),
      Components({
        // deep: true, // 是否递归搜索子目录（默认 true）
        // extensions: ['vue'], // 组件文件扩展名（默认 ['vue']）
        dirs: ['src/components'], // 自动导入的组件目录
        resolvers: [ElementPlusResolver()],
        dts: 'src/types/components.d.ts' // 指定类型声明文件的路径
      })
    ],
    resolve: {
      extensions: ['.js', '.ts', '.json', '.vue'],
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url)),
        '@comps': resolvePath('src/components'),
        '@imgs': resolvePath('src/assets/img'),
        '@icons': resolvePath('src/assets/icons'),
        '@styles': resolvePath('src/assets/styles')
      }
    },
    // 预加载项目必需的组件
    // 只影响 开发环境 会增加第一次启动时的预构建时间，但之后启动更快
    optimizeDeps: {
      include: [
        'vue',
        'vue-router',
        'pinia',
        'axios',
        '@vueuse/core',
        'echarts',
        '@wangeditor/editor',
        '@wangeditor/editor-for-vue',
        'vue-i18n',
        'element-plus/es/components/form/style/css',
        'element-plus/es/components/form-item/style/css',
        'element-plus/es/components/button/style/css',
        'element-plus/es/components/input/style/css',
        'element-plus/es/components/input-number/style/css',
        'element-plus/es/components/switch/style/css',
        'element-plus/es/components/upload/style/css',
        'element-plus/es/components/menu/style/css',
        'element-plus/es/components/col/style/css',
        'element-plus/es/components/icon/style/css',
        'element-plus/es/components/row/style/css',
        'element-plus/es/components/tag/style/css',
        'element-plus/es/components/dialog/style/css',
        'element-plus/es/components/loading/style/css',
        'element-plus/es/components/radio/style/css',
        'element-plus/es/components/radio-group/style/css',
        'element-plus/es/components/popover/style/css',
        'element-plus/es/components/scrollbar/style/css',
        'element-plus/es/components/tooltip/style/css',
        'element-plus/es/components/dropdown/style/css',
        'element-plus/es/components/dropdown-menu/style/css',
        'element-plus/es/components/dropdown-item/style/css',
        'element-plus/es/components/sub-menu/style/css',
        'element-plus/es/components/menu-item/style/css',
        'element-plus/es/components/divider/style/css',
        'element-plus/es/components/card/style/css',
        'element-plus/es/components/link/style/css',
        'element-plus/es/components/breadcrumb/style/css',
        'element-plus/es/components/breadcrumb-item/style/css',
        'element-plus/es/components/table/style/css',
        'element-plus/es/components/tree-select/style/css',
        'element-plus/es/components/table-column/style/css',
        'element-plus/es/components/select/style/css',
        'element-plus/es/components/option/style/css',
        'element-plus/es/components/pagination/style/css',
        'element-plus/es/components/tree/style/css',
        'element-plus/es/components/alert/style/css',
        'element-plus/es/components/radio-button/style/css',
        'element-plus/es/components/checkbox-group/style/css',
        'element-plus/es/components/checkbox/style/css',
        'element-plus/es/components/tabs/style/css',
        'element-plus/es/components/tab-pane/style/css',
        'element-plus/es/components/rate/style/css',
        'element-plus/es/components/date-picker/style/css',
        'element-plus/es/components/notification/style/css',
        'element-plus/es/components/image/style/css',
        'element-plus/es/components/statistic/style/css',
        'element-plus/es/components/watermark/style/css',
        'element-plus/es/components/config-provider/style/css',
        'element-plus/es/components/text/style/css',
        'element-plus/es/components/drawer/style/css',
        'element-plus/es/components/color-picker/style/css',
        'element-plus/es/components/backtop/style/css',
        'element-plus/es/components/message-box/style/css',
        'element-plus/es/components/skeleton/style/css',
        'element-plus/es/components/skeleton/style/css',
        'element-plus/es/components/skeleton-item/style/css',
        'element-plus/es/components/badge/style/css',
        'element-plus/es/components/steps/style/css',
        'element-plus/es/components/step/style/css',
        'element-plus/es/components/avatar/style/css',
        'element-plus/es/components/descriptions/style/css',
        'element-plus/es/components/descriptions-item/style/css',
        'element-plus/es/components/checkbox-group/style/css',
        'element-plus/es/components/progress/style/css',
        'element-plus/es/components/image-viewer/style/css',
        'element-plus/es/components/empty/style/css',
        'element-plus/es/components/segmented/style/css'
      ]
    },
    css: {
      preprocessorOptions: {
        scss: {
          api: 'modern-compiler',
          additionalData: `@use "@styles/variables.scss" as *; @use "@styles/mixin.scss" as *;`
        }
      }
    },
    build: {
      rollupOptions: {
        external: ['element-plus/es/locale']
      },
      chunkSizeWarningLimit: 3000 // 设置为更高的阈值，例如 1000 KB
    }
  })
}

function resolvePath(paths: string): string {
  return path.resolve(__dirname, paths)
}
