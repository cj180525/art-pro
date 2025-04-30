import request from '@/utils/http'
import type { RoleType } from '@/types/model/role'

// 部门接口
export class roleService {
  static getRoleList(params: any) {
    const { page, size, roleName, status, startTime, endTime } = params || {}

    return request.get<RoleType[]>({
      url: `/api/role/list?page=${page}&size=${size}&roleName=${roleName}&status=${status}&startTime=${startTime}&endTime=${endTime}`
    })
  }

  static saveRole(params: RoleType) {
    return request.post<RoleType>({
      url: `/api/role/save`,
      data: params
    })
  }

  static deleteRole(params) {
    return request.del({
      url: `/api/role/delete`,
      data: params
    })
  }

  static updateRole(params: RoleType) {
    return request.put<RoleType>({
      url: `/api/role/update`,
      data: params
    })
  }
}
