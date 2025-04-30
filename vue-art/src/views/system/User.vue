<template>
  <div class="page-content">
    <table-bar
      :showTop="false"
      @search="search"
      @reset="resetForm(searchFormRef)"
      @changeColumn="(list: any) => (columns.values = list)"
      :columns="columns"
    >
      <template #top>
        <el-form :model="searchForm" ref="searchFormRef" label-width="95px">
          <el-row :gutter="20">
            <form-input label="用户名" prop="username" v-model="searchForm.username" />
            <form-input label="邮箱" prop="email" v-model="searchForm.email" />
            <form-input label="手机号" prop="mobile" v-model="searchForm.mobile" />
            <el-col :xs="24" :sm="12" :lg="8">
              <form-select label="性别" prop="sex" v-model="searchForm.sex" :options="sexOptions" />
            </el-col>
            <el-col :xs="24" :sm="12" :lg="8">
              <form-cascader
                label="所属部门"
                prop="depId"
                v-model="searchForm.depId"
                :options="depOptions"
                :props="depProps"
              />
            </el-col>
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
          新增用户
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
    >
      <template #default>
        <el-table-column label="用户名" prop="avatar" #default="scope" min-width="240px" v-if="columns[0].show">
          <div class="user" style="display: flex; align-items: center">
            <img class="avatar" :src="scope.row.avatar" />
            <div>
              <p class="user-name">{{ scope.row.username }}</p>
              <p class="email">{{ scope.row.email }}</p>
            </div>
          </div>
        </el-table-column>
        <el-table-column label="手机号" prop="mobile" v-if="columns[1].show" />
        <el-table-column label="性别" prop="sex" #default="scope" v-if="columns[2].show">
          {{ scope.row.sex === 1 ? '男' : '女' }}
        </el-table-column>
        <el-table-column label="所属部门" prop="depName" v-if="columns[3].show" />
        <el-table-column label="状态" prop="status" v-if="columns[4].show">
          <template #default="scope">
            <el-tag :type="getTagType(scope.row.status)">
              {{ buildTagText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" min-width="100px" sortable v-if="columns[5].show" />
        <el-table-column label="操作" align="center" width="180px" fixed="right">
          <template #default="scope">
            <el-row>
              <button-table type="edit" @click="showDialog('edit', scope.row)" />
              <button-table type="delete" @click="deleteDialog(scope.row)" />
              <button-more
                :list="[
                  { key: 'uploadAvatar', label: '上传头像' },
                  { key: 'assignedRole', label: '分配角色' }
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
      :title="dialogType === 'add' ? '添加用户' : '编辑用户'"
      @closed="resetForm(dialogFormRef)"
      width="450px"
    >
      <el-form ref="dialogFormRef" :model="dialogForm" :rules="rules" label-width="95px">
        <el-form-item label="用户名：" prop="username">
          <el-input v-model="dialogForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码：" prop="password">
          <el-input v-model="dialogForm.password" type="password" show-password placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="重复密码：" prop="confirmPassword" v-if="dialogType === 'add'">
          <el-input v-model="dialogForm.confirmPassword" type="password" show-password placeholder="请再次输入密码" />
        </el-form-item>
        <el-form-item label="手机号：" prop="mobile">
          <el-input v-model="dialogForm.mobile" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱：" prop="email">
          <el-input v-model="dialogForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <form-select label="性别" prop="sex" v-model="dialogForm.sex" :options="sexOptions" :clearable="false" />
        <form-select
          label="状态"
          prop="status"
          v-model="dialogForm.status"
          :options="statusOptions"
          :clearable="false"
        />
        <form-cascader
          label="所属部门"
          prop="depId"
          v-model="dialogForm.depId"
          :options="depOptions"
          :props="depProps"
        />
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
  import type { CascaderProps } from 'element-plus'
  import { userService } from '@/api/system/userApi'
  import { departmentService } from '@/api/system/departmentApi'
  import { ApiStatus } from '@/constant/apiStatusEnum'
  import { sexOptions } from '@/constant/formConst'

  import { useI18n } from 'vue-i18n'
  const { t } = useI18n()

  // 初始化
  const depOptions = ref([])
  onMounted(() => {
    getUserList()
    getDepOptions()
  })
  const getUserList = async () => {
    try {
      const params = {
        ...searchForm,
        startTime: searchForm.createTime && searchForm.createTime[0] ? searchForm.createTime[0] : '',
        endTime: searchForm.createTime && searchForm.createTime[1] ? searchForm.createTime[1] : '',
        page: tableData.page,
        size: tableData.size
      }
      const res = await userService.getUserList(params)

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
  const getDepOptions = async () => {
    try {
      const res = await departmentService.getDepartmentTree()

      if (res && res.code === ApiStatus.success && res.data) {
        depOptions.value = res.data
      }
    } catch (err) {
      console.error('加载部门失败', err)
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
  // 表格 - 状态处理
  const getTagType = (status: number) =>
    ({
      1: 'success',
      2: 'warning',
      3: 'danger',
      4: 'info'
    })[status] || 'primary'
  const buildTagText = (status: number) =>
    ({
      1: '在线',
      2: '离线',
      3: '异常',
      4: '注销'
    })[status] || ''

  const handleCurrentChange = (val: number) => {
    tableData.page = val
    getUserList()
  }
  const handleSizeChange = (val: number) => {
    tableData.size = val
    getUserList()
  }

  // 搜索栏
  // 搜索栏 - 栏目处理
  const columns = reactive([
    { name: '用户名', show: true },
    { name: '手机号', show: true },
    { name: '性别', show: true },
    { name: '所属部门', show: true },
    { name: '状态', show: true },
    { name: '创建时间', show: true }
  ])

  const searchFormRef = ref<FormInstance>()
  const searchForm = reactive({
    username: '',
    email: '',
    mobile: '',
    sex: '',
    depId: '',
    status: '',
    createTime: []
  })

  const statusOptions = [1, 2, 3, 4].map((status) => ({
    value: status,
    label: buildTagText(status)
  }))

  // 搜索栏 - 部门多级关联
  const depProps: CascaderProps = {
    value: 'id',
    label: 'depName',
    checkStrictly: true, // 选择时不联动父子节点
    emitPath: false // 只返回选中的值，而不是整个路径
  }

  const search = () => {
    getUserList()
  }
  const resetForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.resetFields()
  }

  // 弹窗
  const dialogType = ref('add')
  const dialogVisible = ref(false)

  const defaultDialogForm = {
    username: '',
    password: '',
    confirmPassword: '',
    mobile: '',
    email: '',
    sex: '',
    status: 1,
    depId: ''
  }
  const dialogForm = reactive({ ...defaultDialogForm })
  const dialogFormRef = ref<FormInstance>()

  // 自定义校验规则
  // const validatePass = (rule: any, value: string, callback: any) => {
  //   if (value === '') {
  //     callback(new Error(t('register.placeholder[1]')))
  //   } else {
  //     if (formData.confirmPassword !== '') {
  //       formRef.value?.validateField('confirmPassword')
  //     }
  //     callback()
  //   }
  // }
  const validatePass2 = (rule: any, value: string, callback: any) => {
    if (value === '') {
      callback(new Error(t('register.rule[0]')))
    } else if (value !== formData.password) {
      callback(new Error(t('register.rule[1]')))
    } else {
      callback()
    }
  }

  const rules = reactive<FormRules>({
    username: [
      { required: true, message: '请输入用户名', trigger: 'blur' },
      { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' },
      {
        pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d@$!%*?&]{8,16}$/,
        message: '密码需8-16位，含大小写字母和数字',
        trigger: 'blur'
      }
    ],
    confirmPassword: [{ required: true, validator: validatePass2, trigger: 'blur' }],
    mobile: [
      { required: true, message: '请输入手机号', trigger: 'blur' },
      { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
    ],
    email: [
      { required: true, message: '请输入邮箱地址', trigger: 'blur' },
      {
        pattern: /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/,
        message: '邮箱格式不正确',
        trigger: 'blur'
      }
    ],
    sex: [{ required: true, message: '请选择性别', trigger: 'change' }],
    depId: [{ required: true, message: '请选择部门', trigger: 'change' }]
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
        const { confirmPassword, ...params } = dialogForm

        if (dialogType.value === 'add') {
          const res = await userService.saveUser(params)

          if (res && res.code === ApiStatus.success) {
            ElMessage.success('添加成功')
            dialogVisible.value = false
            search()
          }
        } else if (dialogType.value === 'edit') {
          const res = await userService.updateUser(params)

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
    ElMessageBox.confirm('确定要删除该用户吗？', '删除用户', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'error'
    }).then(async () => {
      const params = [row.id]
      const res = await userService.deleteUser(params)

      if (res && res.code === ApiStatus.success) {
        ElMessage.success('删除成功')
        search()
      }
    })
  }

  const buttonMoreClick = (item: ButtonMoreItem, row: any) => {
    if (item.key === 'uploadAvatar') {
      // showPermissionDialog()
      ElMessage.warning('上传头像功能暂未实现')
    } else if (item.key === 'assignedRole') {
      // showDialog('edit', row)
      ElMessage.warning('分配角色功能暂未实现')
    }
  }
</script>

<style lang="scss" scoped>
  .user {
    .avatar {
      width: 40px;
      height: 40px;
      border-radius: 6px;
    }

    > div {
      margin-left: 10px;

      .user-name {
        font-weight: 500;
        color: var(--art-text-gray-800);
      }
    }
  }
</style>
