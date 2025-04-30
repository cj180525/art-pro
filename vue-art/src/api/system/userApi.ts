import request from '@/utils/http'
import type { UserType } from '@/types/model/user'

// 用户接口
export class userService {
  static getUserList(params: any) {
    const { page, size, username, email, mobile, sex, depId, status, startTime, endTime } = params

    return request.get<UserType[]>({
      url:
        `/api/user/list?page=${page}&size=${size}&username=${username}&email=${email}&mobile=${mobile}` +
        `&sex=${sex}&depId=${depId}&status=${status}&startTime=${startTime}&endTime=${endTime}`
    })
  }

  static getUser(params: UserType) {
    return request.get<UserType>({
      url: `/api/user/info/${params.id}`
    })
  }

  static saveUser(params: UserType) {
    return request.post<UserType>({
      url: `/api/user/save`,
      data: params
    })
  }

  static deleteUser(params) {
    return request.del({
      url: `/api/user/delete`,
      data: params
    })
  }

  static updateUser(params: UserType) {
    return request.put<UserType>({
      url: `/api/user/update`,
      data: params
    })
  }
}
