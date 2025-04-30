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
            <template v-for="col in columns" :key="col.paField">
              <form-input
                :prop="col.paField"
                :label="col.fieldName"
                v-model="searchForm[col.paField]"
                v-if="col.fieldType === 1 || col.fieldType === 4"
              />
              <el-col :xs="24" :sm="12" :lg="8" v-if="col.fieldType === 2">
                <form-select
                  :prop="col.paField"
                  :label="col.fieldName"
                  v-model="searchForm[col.paField]"
                  :options="selectOptions(col.paField)"
                />
              </el-col>
              <el-col :xs="24" :sm="24" :lg="8" v-if="col.fieldType === 3">
                <form-date
                  :prop="col.paField"
                  :label="col.fieldName"
                  v-model="searchForm[col.paField]"
                  type="datetimerange"
                  format="YYYY-MM-DD HH:mm:ss"
                />
              </el-col>
            </template>

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
        <template v-for="col in columns" :key="col.paField">
          <el-table-column
            :prop="col.paField"
            :label="col.fieldName"
            v-if="col.show && (col.fieldType === 1 || col.fieldType === 4)"
          />
          <el-table-column :prop="col.paField" :label="col.fieldName" v-if="col.show && col.fieldType === 2">
            <template #default="scope">
              <el-tag :type="typeOptions(col.paField, scope.row[col.paField])">
                {{ textOptions(col.paField, scope.row[col.paField]) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            :prop="col.paField"
            :label="col.fieldName"
            min-width="100px"
            sortable
            v-if="col.show && col.fieldType === 3"
          />
        </template>

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
        <template v-for="col in columns" :key="col.paField">
          <el-form-item :prop="col.paField" :label="col.fieldName" v-if="col.fieldType === 1">
            <el-input v-model="dialogForm[col.paField]" :placeholder="'请输入' + col.fieldName" />
          </el-form-item>
          <el-form-item :prop="col.paField" :label="col.fieldName" v-if="col.fieldType === 2">
            <el-switch v-model="dialogForm[col.paField]" :active-value="1" :inactive-value="2" />
          </el-form-item>
          <el-form-item :prop="col.paField" :label="col.fieldName" v-if="col.fieldType === 4">
            <el-input
              v-model="dialogForm[col.paField]"
              :placeholder="'请输入' + col.fieldName"
              type="textarea"
              :rows="3"
            />
          </el-form-item>
          <el-form-item :prop="col.paField" :label="col.fieldName" v-if="col.fieldType === 5">
            <el-input-number v-model="dialogForm[col.paField]" :min="1" />
          </el-form-item>
        </template>
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
  import { paramManageService } from '@/api/common/paramManageApi'
  import { commonService } from '@/api/common/commonApi'
  import { ApiStatus } from '@/constant/apiStatusEnum'
  import { selectOptions, typeOptions, textOptions } from '@/constant/formConst'
  import { toLine } from '@/utils/utils'
  import { getCommonPageRoutes } from '@/utils/menu'
  import { useSettingStore } from '@/stores/setting'

  const route = useRoute()

  watch(
    () => route.fullPath, // 监听完整路径变化
    () => {
      useSettingStore().reload()
    }
  )

  const commonPageRoutes = getCommonPageRoutes().find((item: any) => item.path === route.path)
  const commonUrlName = commonPageRoutes?.path.replace('/common/commonPage/', '')
  const commonTableName = 'pa_' + toLine(commonUrlName)

  // const commonTableName = 'pa_param_manage'
  // const commonUrlName = 'paramManage'

  // 表格
  const tableData = reactive({
    data: [],
    page: 1,
    size: 10,
    total: 0,
    hideOnSinglePage: true
  })
  const selectedRows = ref([])

  // 搜索栏
  const columns = reactive([])
  const rules = reactive({})

  const searchFormRef = ref<FormInstance>()
  const defaultSearchForm = {}
  const searchForm = reactive({})

  // 弹窗
  const dialogType = ref('add')
  const dialogVisible = ref(false)

  const dialogFormRef = ref<FormInstance>()
  const defaultDialogForm = {}
  const dialogForm = reactive({})

  const fetchColumns = async () => {
    const res = await paramManageService.getParamManageByPaTable(commonTableName)

    if (res && res.code === ApiStatus.success && res.data) {
      const newCols = res.data.map((item) => ({
        ...item,
        show: true,
        name: item.fieldName
      }))

      // 初始化columns
      columns.splice(0, columns.length, ...newCols)
      // 初始化搜索字段
      columns.forEach((col) => {
        defaultSearchForm[col.paField] = ''
      })
      Object.assign(searchForm, defaultSearchForm)
      // 初始化表单字段
      columns.forEach((col) => {
        if (col.paField !== 'createTime') {
          defaultDialogForm[col.paField] = ''
          if (col.fieldType === 2) {
            defaultDialogForm[col.paField] = 2
          }
          if (col.fieldType === 5 || col.paField === 'status') {
            defaultDialogForm[col.paField] = 1
          }
        }
      })
      Object.assign(dialogForm, defaultDialogForm)

      console.log('----', dialogForm)

      // 初始化校验规则
      columns.forEach((col) => {
        if (col.required === 1) {
          rules[col.paField] = [{ required: true, message: `请输入${col.fieldName}`, trigger: 'blur' }]
        }
      })
    }
  }

  fetchColumns()

  // 初始化
  onMounted(() => {
    getPage()
  })
  const getPage = async () => {
    try {
      const params = {
        ...searchForm,
        startTime: searchForm.createTime && searchForm.createTime[0] ? searchForm.createTime[0] : '',
        endTime: searchForm.createTime && searchForm.createTime[1] ? searchForm.createTime[1] : '',
        page: tableData.page,
        size: tableData.size
      }
      const res = await commonService.getPage(params, commonUrlName)

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

  const search = () => {
    getPage()
  }
  const resetForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.resetFields()
  }

  const handleCurrentChange = (val: number) => {
    tableData.page = val
    getPage()
  }
  const handleSizeChange = (val: number) => {
    tableData.size = val
    getPage()
  }
  const handleSelectionChange = (selection: any[]) => {
    selectedRows.value = selection
  }

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
        if (dialogType.value === 'add') {
          const res = await commonService.save(dialogForm, commonUrlName)

          if (res && res.code === ApiStatus.success) {
            ElMessage.success('添加成功')
            dialogVisible.value = false
            search()
          }
        } else if (dialogType.value === 'edit') {
          const res = await commonService.update(dialogForm, commonUrlName)

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
      const res = await commonService.delete(params, commonUrlName)
      if (res && res.code === ApiStatus.success) {
        ElMessage.success('删除成功')
        search()
      }
    })
  }
</script>
