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
            <form-input label="角色名称" prop="roleName" v-model="searchForm.roleName" />
            <el-col :xs="24" :sm="12" :lg="8">
              <form-select label="状态" prop="status" v-model="searchForm.status" :options="statusOptions" />
            </el-col>
            <el-col :xs="24" :sm="12" :lg="8">
              <form-date
                label="创建时间"
                prop="createTime"
                v-model="searchForm.createTime"
                type="datetimerange"
                format="YYYY-MM-DD HH:mm:ss"
              />
            </el-col>
            <el-col :xs="1" :sm="1" :lg="1">
              <div style="height: 50px"></div>
            </el-col>
          </el-row>
        </el-form>
      </template>
      <template #bottom>
        <el-button v-ripple type="success" @click="showDialog('add')">
          <el-icon class="iconfont-sys"><CirclePlus /></el-icon>
          新增角色
        </el-button>
      </template>
    </table-bar>

    <table-content
      :data="tableData.data"
      :current-page="tableData.page"
      :page-size="tableData.size"
      :total="tableData.total"
      :hide-on-single-page="tableData.hideOnSinglePage"
      selection
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    >
      <template #default>
        <el-table-column label="角色名称" prop="roleName" v-if="columns[0].show" />
        <el-table-column label="描述" prop="roleDesc" v-if="columns[1].show" />
        <el-table-column label="状态" prop="status" v-if="columns[2].show">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'primary' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" min-width="100px" sortable v-if="columns[3].show" />

        <el-table-column label="操作" width="180px" align="center" fixed="right">
          <template #default="scope">
            <el-row>
              <button-table type="edit" @click="showDialog('edit', scope.row)" />
              <button-table type="delete" @click="deleteDialog(scope.row)" />
              <button-more
                :list="[
                  { key: 'menuPermission', label: '菜单权限' },
                  { key: 'assignedUser', label: '分配用户' }
                ]"
                @click="buttonMoreClick($event, scope.row)"
              />
            </el-row>
          </template>
        </el-table-column>
      </template>
    </table-content>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '新增角色' : '编辑角色'"
      width="450px"
      @closed="resetForm(dialogFormRef)"
    >
      <el-form ref="dialogFormRef" :model="dialogForm" :rules="rules" label-width="95px">
        <el-form-item label="角色名称：" prop="roleName">
          <el-input v-model="dialogForm.roleName" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="dialogForm.status" :active-value="1" :inactive-value="2" />
        </el-form-item>
        <el-form-item label="描述：" prop="roleDesc">
          <el-input v-model="dialogForm.roleDesc" type="textarea" :rows="3" placeholder="请输入描述信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button @click="submitForm" type="primary">提交</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- <el-dialog v-model="permissionDialog" title="菜单权限" width="30%">
      <div :style="{ maxHeight: '500px', overflowY: 'scroll' }">
        <el-tree
          :data="menuList"
          show-checkbox
          node-key="id"
          :default-expanded-keys="[1, 2, 3, 4, 5, 6, 7, 8]"
          :default-checked-keys="[1, 2, 3]"
          :props="defaultProps"
        />
      </div>
    </el-dialog> -->
  </div>
</template>

<script setup lang="ts">
  import type { FormInstance, FormRules } from 'element-plus'
  import { roleService } from '@/api/system/roleApi'
  import { ApiStatus } from '@/constant/apiStatusEnum'
  import { statusOptions } from '@/constant/formConst'

  // 初始化
  onMounted(() => {
    getRoleList()
  })
  const getRoleList = async () => {
    try {
      const params = {
        ...searchForm,
        startTime: searchForm.createTime && searchForm.createTime[0] ? searchForm.createTime[0] : '',
        endTime: searchForm.createTime && searchForm.createTime[1] ? searchForm.createTime[1] : '',
        page: tableData.page,
        size: tableData.size
      }
      const res = await roleService.getRoleList(params)

      if (res && res.code === ApiStatus.success && res.data) {
        tableData.data = res.data.list // 初始化表格数据
        tableData.page = res.data.currPage
        tableData.size = res.data.pageSize
        tableData.total = res.data.totalCount
        tableData.hideOnSinglePage = res.data.totalCount <= 10
      }
    } catch (err) {
      console.log(err)
    }
  }

  // 表格
  const tableData = reactive({
    data: [],
    page: 1,
    size: 10,
    total: 0,
    hideOnSinglePage: true
  })

  const handleCurrentChange = (val: number) => {
    tableData.page = val
    getRoleList()
  }
  const handleSizeChange = (val: number) => {
    tableData.size = val
    getRoleList()
  }

  // 搜索栏
  // 搜索栏 - 栏目处理
  const columns = reactive([
    { name: '角色名称', show: true },
    { name: '描述', show: true },
    { name: '状态', show: true },
    { name: '创建时间', show: true }
  ])

  const searchFormRef = ref<FormInstance>()
  const searchForm = reactive({
    roleName: '',
    status: '',
    createTime: []
  })

  const search = async () => {
    await getRoleList()
  }
  const resetForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.resetFields()
  }

  // 弹窗
  const dialogFormRef = ref<FormInstance>()
  const defaultDialogForm = {
    id: '',
    roleName: '',
    roleDesc: '',
    status: 1
  }
  const dialogForm = reactive({ ...defaultDialogForm })
  const rules = reactive<FormRules>({
    roleName: [
      { required: true, message: '请输入角色名称', trigger: 'blur' },
      { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
    ]
  })

  const dialogVisible = ref(false)
  const dialogType = ref('add')

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

    await dialogFormRef.value.validate(async (valid) => {
      if (valid) {
        const { ...params } = dialogForm

        if (dialogType.value === 'add') {
          const res = await roleService.saveRole(params)

          if (res && res.code === ApiStatus.success) {
            ElMessage.success('添加成功')
            dialogVisible.value = false
            search()
          }
        } else if (dialogType.value === 'edit') {
          const res = await roleService.updateRole(params)

          if (res && res.code === ApiStatus.success) {
            ElMessage.success('修改成功')
            dialogVisible.value = false
            search()
          }
        }
      }
    })
  }
  const deleteDialog = (row?: any) => {
    ElMessageBox.confirm('确定删除该角色吗？', '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'error'
    })
      .then(async () => {
        const params = [row.id]
        const res = await roleService.deleteRole(params)

        if (res && res.code === ApiStatus.success) {
          ElMessage.success('删除成功')
          search()
        }
      })
      .catch(() => {})
  }

  // // 更多
  // const permissionDialog = ref(false)
  // // const menuList = computed(() => useMenuStore().getMenuList)
  // const menuList = ref([])
  // const defaultProps = {
  //   children: 'children',
  //   label: (data: any) => formatMenuTitle(data.title) || ''
  // }
  // const showPermissionDialog = () => {
  //   permissionDialog.value = true
  // }

  const buttonMoreClick = (item: ButtonMoreItem, row: any) => {
    if (item.key === 'menuPermission') {
      // showPermissionDialog()
      ElMessage.warning('菜单权限功能暂未实现')
    } else if (item.key === 'assignedUser') {
      // showDialog('edit', row)
      ElMessage.warning('分配用户功能暂未实现')
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

  .iconfont-sys {
    margin-right: 4px;
    margin-left: -4px;
  }
</style>
