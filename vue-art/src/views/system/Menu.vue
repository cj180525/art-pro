<template>
  <div class="page-content">
    <table-bar
      :showTop="true"
      @search="search"
      @reset="resetForm(searchFormRef)"
      @changeColumn="(list: any) => (columns.values = list)"
      :columns="columns"
    >
      <template #top>
        <el-form :model="searchForm" ref="searchFormRef" label-width="95px">
          <el-row :gutter="20">
            <form-input label="菜单名称" prop="name" v-model="searchForm.name" />
            <el-col :xs="24" :sm="12" :lg="8">
              <form-select label="菜单类型" prop="type" v-model="searchForm.type" :options="typeOptions" />
            </el-col>
            <form-input label="路由路径" prop="path" v-model="searchForm.path" />
            <form-input label="组件路径" prop="component" v-model="searchForm.component" />
            <el-col :xs="24" :sm="12" :lg="8">
              <form-select label="状态" prop="status" v-model="searchForm.status" :options="statusOptions" />
            </el-col>
            <el-col :xs="1" :sm="1" :lg="1">
              <div style="height: 50px"></div>
            </el-col>
          </el-row>
        </el-form>
      </template>
      <template #bottom>
        <el-button v-ripple type="success" @click="showDialog('add')">
          <el-icon class="iconfont-sys">
            <CirclePlus />
          </el-icon>
          新增菜单
        </el-button>
        <el-button v-ripple type="primary" @click="tableData.isExpandAll = !tableData.isExpandAll">
          <el-icon class="iconfont-sys">
            <Sort />
          </el-icon>
          展开/折叠
        </el-button>
      </template>
    </table-bar>

    <table-content :data="tableData.data" :isExpandAll="tableData.isExpandAll">
      <template #default>
        <el-table-column label="菜单名称" prop="name" min-width="120px" v-if="columns[0].show" />
        <el-table-column label="图标" prop="icon" v-if="columns[1].show" #default="scope">
          <i class="iconfont-sys" v-html="scope.row.icon"></i>
        </el-table-column>
        <el-table-column label="菜单类型" prop="type" v-if="columns[2].show">
          <template #default="scope">
            <el-tag :type="getMenuType(scope.row.type)">
              {{ buildMenuText(scope.row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="路由路径" prop="path" v-if="columns[3].show" />
        <el-table-column label="组件路径" prop="component" v-if="columns[4].show" />
        <el-table-column label="排序" prop="sort" sortable v-if="columns[5].show" />
        <el-table-column label="状态" prop="status" v-if="columns[6].show">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'primary' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" min-width="100px" sortable v-if="columns[7].show" />

        <el-table-column label="操作" width="180px" align="center" fixed="right">
          <template #default="scope">
            <button-table type="add" @click="showDialog('add', scope.row)" />
            <button-table type="edit" @click="showDialog('edit', scope.row)" />
            <button-table type="delete" @click="deleteMenu(scope.row)" />
          </template>
        </el-table-column>
      </template>
    </table-content>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '新增菜单' : '编辑菜单'"
      @closed="resetForm(dialogFormRef)"
      width="50%  "
      align-center
    >
      <el-form ref="dialogFormRef" :model="dialogForm" :rules="rules" label-width="95px">
        <el-row :gutter="20">
          <el-col :xs="24" :sm="24" :lg="12">
            <form-cascader
              label="上级菜单"
              prop="parentId"
              v-model="dialogForm.parentId"
              :options="menuOptions"
              :props="parentProps"
              :disabled="parentDisable"
            />
          </el-col>
        </el-row>
        <form-radio-button label="菜单类型" prop="type" v-model="dialogForm.type" :options="typeOptions" />
        <el-row :gutter="20">
          <el-col :xs="24" :sm="24" :lg="12">
            <el-form-item label="菜单名称" prop="name">
              <el-input v-model="dialogForm.name" placeholder="请输入名称" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :lg="12">
            <el-form-item label="图标" prop="name">
              <icon-selector :iconType="iconType" :defaultIcon="dialogForm.icon" width="229px" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :xs="24" :sm="24" :lg="12">
            <el-form-item label="路由路径" prop="path">
              <el-input v-model="dialogForm.path" placeholder="请输入路由路径" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :lg="12">
            <el-form-item label="组件路径" prop="component">
              <el-input v-model="dialogForm.component" placeholder="请输入组件路径" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :lg="12" v-if="dialogForm.type === 3">
            <el-form-item label="链接地址" prop="link">
              <el-input v-model="dialogForm.link" placeholder="请输入链接地址" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :lg="12">
            <el-form-item label="排序" prop="sort">
              <el-input-number v-model="dialogForm.sort" :min="1" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="状态" prop="status">
              <el-switch v-model="dialogForm.status"></el-switch>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="dialogForm.type === 2 || dialogForm.type === 3">
          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="是否缓存" prop="isCache">
              <el-checkbox v-model="dialogForm.isCache" :true-value="1" :false-value="2" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="展示标签" prop="isShowTab">
              <el-checkbox v-model="dialogForm.isShowTab" :true-value="1" :false-value="2" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="固定标签" prop="isFixTab">
              <el-checkbox v-model="dialogForm.isFixTab" :true-value="1" :false-value="2" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button @click="submitForm" type="primary">提交</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '新增菜单' : '编辑菜单'"
      @closed="resetForm(dialogFormRef)"
      width="50%  "
      align-center
    >
      <el-form ref="dialogFormRef" :model="dialogForm" :rules="rules" label-width="95px">
        <el-row :gutter="20">
          <el-col :xs="24" :sm="24" :lg="12">
            <el-form-item label="所在菜单" prop="name">
              <el-input v-model="dialogForm.name" disabled />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :lg="12">
            <el-form-item label="权限名称" prop="name">
              <el-input v-model="dialogForm.name" placeholder="请输入名称" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="权限标识" prop="name">
          <el-input v-model="dialogForm.name" placeholder="请输入标识" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button @click="submitForm" type="primary">提交</el-button>
        </div>
      </template>
    </el-dialog> -->
  </div>
</template>

<script setup lang="ts">
  import type { FormInstance, FormRules } from 'element-plus'
  import { menuService } from '@/api/system/menuApi'
  import { ApiStatus } from '@/constant/apiStatusEnum'
  import { statusOptions } from '@/constant/formConst'
  import { IconTypeEnum } from '@/constant/appEnum'

  const menuOptions = ref([])
  onMounted(() => {
    getMenuTreeOrList()
  })
  const getMenuTreeOrList = async () => {
    try {
      const params = {
        ...searchForm,
        startTime: searchForm.createTime && searchForm.createTime[0] ? searchForm.createTime[0] : '',
        endTime: searchForm.createTime && searchForm.createTime[1] ? searchForm.createTime[1] : ''
      }
      if (
        !params.name &&
        !params.type &&
        !params.path &&
        !params.component &&
        !params.status &&
        !params.startTime &&
        !params.endTime
      ) {
        const res = await menuService.getMenuTree()
        if (res && res.code === ApiStatus.success && res.data) {
          tableData.data = res.data // 初始化表格数据
        }
      } else {
        const res = await menuService.getMenuList(params)
        if (res && res.code === ApiStatus.success && res.data) {
          tableData.data = res.data.list // 初始化表格数据
        }
      }
    } catch (err) {
      console.log(err)
    }
  }

  // 表格
  const tableData = reactive({
    data: [],
    isExpandAll: false
  })

  const getMenuType = (type: number) =>
    ({
      1: 'info',
      2: 'primary',
      3: 'danger',
      4: 'warning',
      5: 'success'
    })[type] || 'success'
  const buildMenuText = (type: number) =>
    ({
      1: '目录',
      2: '页面',
      3: '公共页面',
      4: '内嵌',
      5: '外链'
    })[type] || ''

  // 搜索栏
  const columns = reactive([
    { name: '菜单名称', show: true },
    { name: '图标', show: true },
    { name: '菜单类型', show: true },
    { name: '路由路径', show: true },
    { name: '组件路径', show: true },
    { name: '排序', show: true },
    { name: '状态', show: true },
    { name: '创建时间', show: true }
  ])

  const searchFormRef = ref<FormInstance>()
  const searchForm = reactive({
    name: '',
    type: '',
    path: '',
    component: '',
    status: '',
    createTime: []
  })
  const typeOptions = [1, 2, 3, 4, 5].map((type) => ({
    value: type,
    label: buildMenuText(type)
  }))

  const search = async () => {
    await getMenuTreeOrList()
  }
  const resetForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.resetFields()
  }

  // 弹窗
  const dialogType = ref('add')
  const dialogVisible = ref(false)

  const dialogFormRef = ref<FormInstance>()
  const defaultDialogForm = {
    type: 1,
    id: '',
    name: '',
    path: '',
    component: '',
    sort: 0,
    status: 1
  }
  const dialogForm = reactive({ ...defaultDialogForm })

  const rules = reactive<FormRules>({
    name: [
      { required: true, message: '请输入菜单名称', trigger: 'blur' },
      { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
    ]
  })
  const parentProps = {
    value: 'id',
    label: 'depName',
    checkStrictly: true, // 选择时不联动父子节点
    emitPath: false // 只返回选中的值，而不是整个路径
  }

  const iconType = ref(IconTypeEnum.UNICODE)
  const cascaderRef = ref(null)
  const propsParent = {
    value: 'id',
    label: 'name',
    checkStrictly: true, // 选择时不联动父子节点
    emitPath: false // 只返回选中的值，而不是整个路径
  }
  const cascaderDisable = ref(false)

  const handleChange = () => {
    if (cascaderRef.value) {
      cascaderRef.value.togglePopperVisible(false)
    }
  }

  const parentDisable = ref(false)

  const showDialog = (type: string, row?: any) => {
    dialogVisible.value = true
    dialogType.value = type

    if (type === 'edit' && row) {
      Object.assign(dialogForm, row)
    } else {
      Object.assign(dialogForm, defaultDialogForm)
    }
  }

  const submitForm = async () => {
    if (!dialogFormRef) return
    ElMessage.warning('提交功能暂未实现')

    //   await dialogFormRef.validate(async (valid) => {
    //     if (valid) {
    //       let res
    //       if (dialogType.value === 'add') {
    //         const params = {
    //           ...dialogForm,
    //           status: dialogForm.status ? 1 : 0,
    //           createTime: dayjs().format('YYYY-MM-DD HH:mm:ss')
    //         }
    //         res = await roleService.saveRole(params)
    //       } else if (dialogType.value === 'edit') {
    //         const params = {
    //           ...dialogForm,
    //           status: dialogForm.status ? 1 : 0
    //         }
    //         res = await roleService.updateRole(params)
    //       }

    //       if (res && res.code === ApiStatus.success && res.data) {
    //         ElMessage.success(dialogType.value === 'add' ? '添加成功' : '修改成功')
    //         dialogVisible.value = false
    //         search()
    //       }
    //     }
    //   })
  }

  const deleteMenu = (row?: any) => {
    let dialog
    const params = [] as number[]

    if (row.children && row.children.length > 0) {
      dialog = '确定要删除该菜单及子菜单吗？'
      filterTreeGetId(row, params)
    } else {
      dialog = '确定要删除该菜单吗？'
      params.push(row.id)
    }

    ElMessageBox.confirm(dialog, '删除菜单', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'error'
    })
      .then(async () => {
        const res = await menuService.deleteMenu(params)
        if (res && res.code === ApiStatus.success) {
          ElMessage.success('删除成功')
          search()
        }
      })
      .catch(() => {})
  }

  const filterTreeGetId = (node, params) => {
    params.push(node.id)
    if (node.children && node.children.length > 0) {
      node.children.forEach((child) => filterTreeGetId(child, params))
    }
  }
</script>

<style lang="scss" scoped>
  .page-content {
    .svg-icon {
      width: 1.8em;
      height: 1.8em;
      overflow: hidden;
      vertical-align: -8px;
      fill: currentcolor;
    }
  }
</style>
