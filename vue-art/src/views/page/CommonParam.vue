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
            <form-input label="参数表" prop="paTable" v-model="searchForm.paTable" />
            <form-input label="参数名" prop="paName" v-model="searchForm.paName" />
            <form-input label="键" prop="paKey" v-model="searchForm.paKey" />
            <form-input label="值" prop="paValue" v-model="searchForm.paValue" />
            <form-input label="备注" prop="remark" v-model="searchForm.remark" />
            <el-col :xs="24" :sm="12" :lg="8">
              <form-select label="状态" prop="status" v-model="searchForm.status" :options="statusOptions" />
            </el-col>
            <el-col :xs="24" :sm="24" :lg="8">
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
          新增参数
        </el-button>
        <el-button v-ripple type="danger" @click="deleteDialog">
          <el-icon class="iconfont-sys">
            <Delete />
          </el-icon>
          删除参数
        </el-button>
      </template>
    </table-bar>

    <table-content
      :data="tableData.data"
      :current-page="tableData.page"
      :page-size="tableData.size"
      :total="tableData.total"
      :hide-on-single-page="tableData.hideOnSinglePage"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
      @selection-change="handleSelectionChange"
      selection
    >
      <template #default>
        <el-table-column label="参数表" prop="paTable" min-width="150px" v-if="columns[0].show" />
        <el-table-column label="参数名" prop="paName" min-width="120px" v-if="columns[1].show" />
        <el-table-column label="键" prop="paKey" v-if="columns[2].show" />
        <el-table-column label="值" prop="paValue" v-if="columns[3].show" />
        <el-table-column label="备注" prop="remark" v-if="columns[4].show" />
        <el-table-column label="状态" prop="status" v-if="columns[5].show">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'primary' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" min-width="100px" sortable v-if="columns[6].show" />
        <el-table-column label="操作" align="center" width="120px" fixed="right">
          <template #default="scope">
            <el-row>
              <button-table type="edit" @click="showDialog('edit', scope.row)" />
              <button-table type="delete" @click="deleteDialog(scope.row)" />
            </el-row>
          </template>
        </el-table-column>
      </template>
    </table-content>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '添加参数' : '编辑参数'"
      @closed="resetForm(dialogFormRef)"
      width="450px"
    >
      <el-form ref="dialogFormRef" :model="dialogForm" :rules="rules" label-width="95px">
        <el-form-item label="参数表：" prop="paTable">
          <el-input v-model="dialogForm.paTable" placeholder="请输入参数表" />
        </el-form-item>
        <el-form-item label="参数名：" prop="paName">
          <el-input v-model="dialogForm.paName" placeholder="请输入参数名" />
        </el-form-item>
        <el-form-item label="键：" prop="paKey">
          <el-input v-model="dialogForm.paKey" placeholder="请输入键" />
        </el-form-item>
        <el-form-item label="值：" prop="paValue">
          <el-input v-model="dialogForm.paValue" placeholder="请输入值" />
        </el-form-item>
        <el-form-item label="状态：" prop="status">
          <el-switch v-model="dialogForm.status" :active-value="1" :inactive-value="2" />
        </el-form-item>
        <el-form-item label="备注：" prop="remark">
          <el-input v-model="dialogForm.remark" type="textarea" :rows="3" placeholder="请输入备注" />
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
  import { commonParamService } from '@/api/common/commonParamApi'
  import { ApiStatus } from '@/constant/apiStatusEnum'
  import { statusOptions } from '@/constant/formConst'
  import { el } from 'element-plus/es/locale'

  // 初始化
  onMounted(() => {
    getCommonParamList()
  })
  const getCommonParamList = async () => {
    try {
      const params = {
        ...searchForm,
        startTime: searchForm.createTime && searchForm.createTime[0] ? searchForm.createTime[0] : '',
        endTime: searchForm.createTime && searchForm.createTime[1] ? searchForm.createTime[1] : '',
        page: tableData.page,
        size: tableData.size
      }
      const res = await commonParamService.getCommonParamList(params)

      if (res && res.code === ApiStatus.success && res.data) {
        tableData.data = res.data.list
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
  const selectedRows = ref([])

  const handleCurrentChange = (val: number) => {
    tableData.page = val
    getUserList()
  }
  const handleSizeChange = (val: number) => {
    tableData.size = val
    getUserList()
  }
  const handleSelectionChange = (selection: any[]) => {
    selectedRows.value = selection
  }

  // 搜索栏
  // 搜索栏 - 栏目处理
  const columns = reactive([
    { name: '参数表', show: true },
    { name: '参数名', show: true },
    { name: '键', show: true },
    { name: '值', show: true },
    { name: '备注', show: true },
    { name: '状态', show: true },
    { name: '创建时间', show: true }
  ])

  const searchFormRef = ref<FormInstance>()
  const searchForm = reactive({
    paTable: '',
    paName: '',
    paKey: '',
    paValue: '',
    remark: '',
    status: '',
    createTime: []
  })

  const search = () => {
    getCommonParamList()
  }
  const resetForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.resetFields()
  }

  // 弹窗
  const dialogType = ref('add')
  const dialogVisible = ref(false)

  const defaultDialogForm = {
    paTable: '',
    paName: '',
    paKey: '',
    paValue: '',
    remark: '',
    status: 1
  }
  const dialogForm = reactive({ ...defaultDialogForm })
  const dialogFormRef = ref<FormInstance>()

  // 自定义校验规则
  const rules = reactive<FormRules>({
    paTable: [{ required: true, message: '请输入参数表', trigger: 'blur' }],
    paName: [{ required: true, message: '请输入参数名', trigger: 'blur' }],
    paKey: [{ required: true, message: '请输入键', trigger: 'blur' }],
    paValue: [{ required: true, message: '请输入值', trigger: 'blur' }]
  })

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
    if (!dialogFormRef.value) return

    await dialogFormRef.value.validate(async (valid) => {
      if (valid) {
        const { repeatPassword, ...params } = dialogForm

        if (dialogType.value === 'add') {
          const res = await commonParamService.saveCommonParam(params)

          if (res && res.code === ApiStatus.success) {
            ElMessage.success('添加成功')
            dialogVisible.value = false
            search()
          }
        } else if (dialogType.value === 'edit') {
          const res = await commonParamService.updateCommonParam(params)

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
    if (!row.id && selectedRows.value.length === 0) {
      ElMessage.warning('请先勾选')
      return
    }
    ElMessageBox.confirm('确定要删除该参数吗？', '删除参数', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'error'
    }).then(async () => {
      const params = []
      if (row.id) {
        params.push(row.id)
      } else {
        params.push(...selectedRows.value.map((item) => item.id))
      }
      const res = await commonParamService.deleteCommonParam(params)

      if (res && res.code === ApiStatus.success) {
        ElMessage.success('删除成功')
        search()
      }
    })
  }
</script>
