import router from '@/router'

import { useUserStore } from '@/stores/user'
import { useWorkTabStore } from '@/stores/workTab'
import { clearTokenCookie } from '@/utils/cookie'

export async function logout() {
  clearTokenCookie()
  sessionStorage.removeItem('iframeRoutes')

  await useUserStore().$reset()
  await useWorkTabStore().$reset()

  await router.push('/auth/login')
}
