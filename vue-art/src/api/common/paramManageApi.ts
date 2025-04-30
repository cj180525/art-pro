import request from '@/utils/http'
import type { ParamManageType } from '@/types/model/paramManage'

// 用户接口
export class paramManageService {
  static getParamManageList(params: any) {
    const { page, size, paTable, paName, paKey, paValue, remark, status, startTime, endTime } = params

    return request.get<ParamManageType[]>({
      url:
        `/api/paramManage/list?page=${page}&size=${size}&paTable=${paTable}&paName=${paName}&paKey=${paKey}&paValue=${paValue}` +
        `&remark=${remark}&status=${status}&startTime=${startTime}&endTime=${endTime}`
    })
  }

  static getParamManageByPaTable(paTable: string) {
    return request.get({
      url: `/api/paramManage/queryByPaTable?paTable=${paTable}`
    })
  }

  static getParamManage(params: ParamManageType) {
    return request.get<ParamManageType>({
      url: `/api/paramManage/info/${params.id}`
    })
  }

  static saveParamManage(params: ParamManageType) {
    return request.post<ParamManageType>({
      url: `/api/paramManage/save`,
      data: params
    })
  }

  static deleteParamManage(params) {
    return request.del({
      url: `/api/paramManage/delete`,
      data: params
    })
  }

  static updateParamManage(params: ParamManageType) {
    return request.put<ParamManageType>({
      url: `/api/paramManage/update`,
      data: params
    })
  }
}
