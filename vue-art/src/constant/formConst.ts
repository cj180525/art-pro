// 状态
export const statusOptions = [
  {
    value: 1,
    label: '启用'
  },
  {
    value: 2,
    label: '禁用'
  }
]
export const yesOrNoOptions = [
  {
    value: 1,
    label: '是'
  },
  {
    value: 2,
    label: '否'
  }
]
// 性别
export const sexOptions = [
  {
    value: 1,
    label: '男'
  },
  {
    value: 2,
    label: '女'
  }
]

export const selectOptions = (field: string) => {
  switch (field) {
    case 'status':
      return statusOptions
    case 'required':
      return yesOrNoOptions
    default:
      return []
  }
}
export const typeOptions = (field: string, value: number) => {
  switch (field) {
    case 'status':
      return value === 1 ? 'primary' : 'danger'
    case 'required':
      return value === 1 ? 'warning' : 'info'
    default:
      return ''
  }
}
export const textOptions = (field: string, value: number) => {
  switch (field) {
    case 'status':
      return value === 1 ? '启用' : '禁用'
    case 'required':
      return value === 1 ? '是' : '否'
    default:
      return ''
  }
}
