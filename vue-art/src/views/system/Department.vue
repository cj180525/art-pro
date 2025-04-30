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
            <form-input label="部门名称" prop="depName" v-model="searchForm.depName" />
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
          <el-icon class="iconfont-sys">
            <CirclePlus />
          </el-icon>
          新增部门
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
        <el-table-column prop="depName" label="部门名称" min-width="120px" v-if="columns[0].show" />
        <el-table-column prop="status" label="状态" v-if="columns[1].show">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'primary' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" sortable v-if="columns[2].show" />
        <el-table-column prop="createTime" label="创建时间" min-width="100px" sortable v-if="columns[3].show" />

        <el-table-column label="操作" width="180px" align="center" fixed="right">
          <template #default="scope">
            <button-table type="add" @click="showDialog('add', scope.row)" />
            <button-table type="edit" @click="showDialog('edit', scope.row)" />
            <button-table type="delete" @click="deleteDepartment(scope.row)" />
          </template>
        </el-table-column>
      </template>
    </table-content>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '添加部门' : '编辑部门'"
      @closed="resetForm(dialogFormRef)"
      width="450px"
    >
      <el-form ref="dialogFormRef" :model="dialogForm" :rules="rules" label-width="95px">
        <el-col>
          <!-- <form-cascader
            label="上级部门"
            prop="parentId"
            v-model="dialogForm.parentId"
            :options="depOptions"
            :props="parentsProps"
            @visible-change="depVisibleChange"
          /> -->
          <form-cascader
            label="上级部门"
            prop="parentId"
            v-model="dialogForm.parentId"
            :options="depOptions"
            :props="parentProps"
            :disabled="parentDisable"
          />
        </el-col>
        <el-form-item label="部门名称" prop="depName">
          <el-input v-model="dialogForm.depName" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="dialogForm.sort" :min="1" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="dialogForm.status" :active-value="1" :inactive-value="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button @click="submitForm" type="primary">提交</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
  import type { FormInstance, FormRules } from 'element-plus'
  import { departmentService } from '@/api/system/departmentApi'
  import { ApiStatus } from '@/constant/apiStatusEnum'
  import { statusOptions } from '@/constant/formConst'
  // import { el, ta } from 'element-plus/es/locale'

  // const depVisibleChange = async (visible) => {
  //   if (visible && !hasLoaded.value) {
  //     try {
  //       const res = await departmentService.getDepartmentTree()

  //       if (res && res.code === ApiStatus.success && res.data) {
  //         depOptions.value = res.data
  //         hasLoaded.value = true
  //       }
  //     } catch (err) {
  //       console.error('加载部门失败', err)
  //     }
  //   }
  // }

  // 初始化
  const depOptions = ref([]) // 部门树数据
  onMounted(async () => {
    await getDepartmentTree()
    depOptions.value = tableData.data
  })
  const getDepartmentTree = async () => {
    try {
      const params = {
        ...searchForm,
        startTime: searchForm.createTime && searchForm.createTime[0] ? searchForm.createTime[0] : '',
        endTime: searchForm.createTime && searchForm.createTime[1] ? searchForm.createTime[1] : ''
      }

      const res = await departmentService.getDepartmentTree(params)

      if (res && res.code === ApiStatus.success && res.data) {
        tableData.data = res.data // 初始化表格数据
      }
    } catch (err) {
      console.log(err)
    }
  }

  // 表格
  const tableData = reactive({
    data: [],
    isExpandAll: true
  })

  // 搜索栏
  // 搜索栏 - 栏目处理
  const columns = reactive([
    { name: '部门名称', show: true },
    { name: '状态', show: true },
    { name: '排序', show: true },
    { name: '创建时间', show: true }
  ])

  const searchFormRef = ref<FormInstance>()
  const searchForm = reactive({
    depName: '',
    status: '',
    createTime: []
  })

  const parentProps = {
    value: 'id',
    label: 'depName',
    checkStrictly: true, // 选择时不联动父子节点
    emitPath: false // 只返回选中的值，而不是整个路径
  }

  const search = async () => {
    await getDepartmentTree()
  }
  const resetForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.resetFields()
  }

  // 弹窗
  const dialogType = ref()
  const dialogVisible = ref(false)

  const dialogFormRef = ref<FormInstance>()
  const defaultDialogForm = {
    parentId: '',
    depName: '',
    sort: 0,
    status: 1,
    createTime: '',
    id: ''
  }
  const dialogForm = reactive({ ...defaultDialogForm })

  const rules = reactive<FormRules>({
    depName: [
      { required: true, message: '请输入部门名称', trigger: 'blur' },
      { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
    ],
    sort: [{ required: true, message: '请输入排序号', trigger: 'blur' }]
  })

  const parentDisable = ref(false)

  const showDialog = (type: string, row?: any) => {
    dialogType.value = type
    dialogVisible.value = true

    if (type === 'edit') {
      parentDisable.value = true
      Object.assign(dialogForm, row)
    } else if (type === 'add' && row) {
      parentDisable.value = true
      dialogForm.parentId = row.id
      Object.assign(dialogForm, defaultDialogForm)
    } else {
      parentDisable.value = false
      Object.assign(dialogForm, defaultDialogForm)
    }
  }

  const submitForm = async () => {
    if (!dialogFormRef.value) return

    await dialogFormRef.value.validate(async (valid) => {
      if (valid) {
        const { ...params } = dialogForm

        console.log('提交数据:', params)

        if (dialogType.value === 'add') {
          const res = await departmentService.saveDepartment(params)

          if (res && res.code === ApiStatus.success) {
            ElMessage.success('添加成功')
            dialogVisible.value = false
            search()
          }
        } else if (dialogType.value === 'edit') {
          const res = await departmentService.updateDepartment(params)

          if (res && res.code === ApiStatus.success) {
            ElMessage.success('修改成功')
            dialogVisible.value = false
            search()
          }
        }
      }
    })
  }

  const deleteDepartment = (row?: any) => {
    let dialog
    const params = [] as number[]

    if (row.children && row.children.length > 0) {
      dialog = '确定要删除该部门及子部门吗？'
      filterTreeGetId(row, params)
    } else {
      dialog = '确定要删除该部门吗？'
      params.push(row.id)
    }

    ElMessageBox.confirm(dialog, '删除部门', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'error'
    })
      .then(async () => {
        const res = await departmentService.deleteDepartment(params)
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
  .svg-icon {
    width: 1.8em;
    height: 1.8em;
    overflow: hidden;
    vertical-align: -8px;
    fill: currentcolor;
  }

  .iconfont-sys {
    margin-right: 4px;
    margin-left: -4px;
  }
</style>
