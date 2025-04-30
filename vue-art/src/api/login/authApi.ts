import request from '@/utils/http'
import type { RegisterType } from '@/types/model/register'

// 菜单接口
export class authService {
  static login(params: RegisterType) {
    const { username, password } = params

    return request.post({
      url: `/api/auth/login?username=${username}&password=${password}`
    })
  }

  static askCode(params: RegisterType) {
    const { email, type } = params

    return request.get({
      url: `/api/auth/ask-code?email=${email}&type=${type}`
    })
  }

  static register(params: RegisterType) {
    return request.post({
      url: `/api/auth/register`,
      data: params
    })
  }

  static checkEmail(params: RegisterType) {
    return request.post({
      url: `/api/auth/checkEmail`,
      data: params
    })
  }

  static resetPassword(params) {
    return request.post({
      url: `/api/auth/resetPassword`,
      data: params
    })
  }
}
