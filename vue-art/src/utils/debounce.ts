/**
 * 防抖（防止短时间刷新太多次）
 */
export function debounce<T extends (...args: any[]) => void>(fn: T, delay: number = 300): T {
  let timer: ReturnType<typeof setTimeout> | null = null
  return function (this: any, ...args: any[]) {
    if (timer) clearTimeout(timer)
    timer = setTimeout(() => {
      fn.apply(this, args)
    }, delay)
  } as T
}
