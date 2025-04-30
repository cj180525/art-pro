import request from '@/utils/http'
import type { CommonParamType } from '@/types/model/commonParam'

// 用户接口
export class commonParamService {
  static getCommonParamList(params: any) {
    const { page, size, paTable, paName, paKey, paValue, remark, status, startTime, endTime } = params

    return request.get<CommonParamType[]>({
      url:
        `/api/commonParam/list?page=${page}&size=${size}&paTable=${paTable}&paName=${paName}&paKey=${paKey}&paValue=${paValue}` +
        `&remark=${remark}&status=${status}&startTime=${startTime}&endTime=${endTime}`
    })
  }

  static getCommonParam(params: CommonParamType) {
    return request.get<CommonParamType>({
      url: `/api/commonParam/info/${params.id}`
    })
  }

  static saveCommonParam(params: CommonParamType) {
    return request.post<CommonParamType>({
      url: `/api/commonParam/save`,
      data: params
    })
  }

  static deleteCommonParam(params) {
    return request.del({
      url: `/api/commonParam/delete`,
      data: params
    })
  }

  static updateCommonParam(params: CommonParamType) {
    return request.put<CommonParamType>({
      url: `/api/commonParam/update`,
      data: params
    })
  }
}
