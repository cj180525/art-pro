export type DepartmentType = {
  id: number
  depName: string
  sort: number
  status: number
  createTime: string
  updateTime?: string
  children?: DepartmentType[]
}
