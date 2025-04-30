import Cookies from 'js-cookie'

/**
 * 保存 token Cookie，并根据后端返回的 expires 设置有效期
 * @param token token字符串
 * @param expires 后端返回的过期天数
 */
export function saveTokenCookie(token: string, expires: number): void {
  Cookies.set('token', 'Bearer ' + token, { expires: expires })
}

/**
 * 获取 token Cookie
 */
export function getTokenCookie(): string | undefined {
  return Cookies.get('token')
}

/**
 * 清除 token Cookie
 */
export function clearTokenCookie(): void {
  Cookies.remove('token')
}

/**
 * 刷新 token Cookie 的有效期
 */
export function refreshTokenCookie(): void {
  const token: string | undefined = Cookies.get('token')
  if (token) {
    Cookies.set('token', token, { expires: 7 }) // 7天有效期
  }
}
