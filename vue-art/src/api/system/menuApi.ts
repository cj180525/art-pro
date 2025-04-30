import request from '@/utils/http'
import type { MenuType } from '@/types/model/menu'

// 菜单接口
export class menuService {
  static getMenuList(params: any) {
    const { name = '', type = '', path = '', component = '', status = '', startTime = '', endTime = '' } = params || {}

    return request.get<MenuType[]>({
      url:
        `/api/menu/list?name=${name}&type=${type}&path=${path}&component=${component}&status=${status}` +
        `&startTime=${startTime}&endTime=${endTime}`
    })
  }
  static getMenuTree() {
    return request.get<MenuType[]>({
      url: '/api/menu/list/tree'
    })
  }

  static saveMenu(params: MenuType) {
    return request.post<MenuType>({
      url: `/api/menu/save`,
      data: params
    })
  }

  static deleteMenu(params) {
    return request.del({
      url: `/api/menu/delete`,
      data: params
    })
  }

  static updateMenu(params: MenuType) {
    return request.put<MenuType>({
      url: `/api/menu/update`,
      data: params
    })
  }
}
