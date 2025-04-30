import request from '@/utils/http'
import type { DepartmentType } from '@/types/model/department'

// 部门接口
export class departmentService {
  static getDepartmentTree(params: any) {
    const { depName = '', status = '', startTime = '', endTime = '' } = params || {}

    return request.get<DepartmentType[]>({
      url: `/api/department/list/tree?depName=${depName}&status=${status}&startTime=${startTime}&endTime=${endTime}`
    })
  }

  static saveDepartment(params: DepartmentType) {
    return request.post<DepartmentType>({
      url: `/api/department/save`,
      data: params
    })
  }

  static deleteDepartment(params) {
    return request.del({
      url: `/api/department/delete`,
      data: params
    })
  }

  static updateDepartment(params: DepartmentType) {
    return request.put<DepartmentType>({
      url: `/api/department/update`,
      data: params
    })
  }

  static getDepartmentList(params: any) {
    const { page, size } = params

    return request.get<DepartmentType[]>({
      url: `/api/department/list?page=${page}&size=${size}`
    })
  }
}
