import request from '@/utils/http'

// 用户接口
export class commonService {
  static getPage(params: any, commonUrlName: string) {
    const queryParams = Object.entries(params)
      .map((item) => {
        return `${item[0]}=${item[1]}`
      })
      .join('&')

    return request.get({
      url: `/api/${commonUrlName}/list?${queryParams}`
    })
  }

  //   static getParamManage(params) {
  //     return request.get({
  //       url: `/api/paramManage/info/${params.id}`
  //     })
  //   }

  static save(params: any, commonUrlName: string) {
    return request.post({
      url: `/api/${commonUrlName}/save`,
      data: params
    })
  }

  static delete(params: any, commonUrlName: string) {
    return request.del({
      url: `/api/${commonUrlName}/delete`,
      data: params
    })
  }

  static update(params: any, commonUrlName: string) {
    return request.put({
      url: `/api/${commonUrlName}/update`,
      data: params
    })
  }
}
