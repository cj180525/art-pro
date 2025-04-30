<template>
  <div class="form-wrap">
    <el-steps align-center :active="active" finish-status="success">
      <el-step title="验证电子邮箱" />
      <el-step title="重新设定密码" />
    </el-steps>
    <el-form ref="formRef" :model="formData" :rules="rules" label-position="top" v-if="active === 0">
      <h3 class="title">{{ $t('校验邮箱') }}</h3>
      <p class="sub-title">{{ $t('获取邮箱验证码') }}</p>

      <el-form-item prop="email">
        <el-input v-model.trim="formData.email" size="large" :placeholder="$t('forgetPassword.placeholder')">
          <template #prefix>
            <el-icon><Message /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="code">
        <el-row justify="space-between" style="width: 100%">
          <el-col :span="16">
            <el-input v-model.trim="formData.code" size="large" maxlength="6" type="text" placeholder="请输入验证码">
              <template #prefix>
                <el-icon><EditPen /></el-icon>
              </template>
            </el-input>
          </el-col>
          <el-col :span="7" :offset="1">
            <el-button
              @click="askCode"
              :disabled="!isEmailValid || isCounting"
              size="large"
              type="primary"
              style="width: 100%"
            >
              {{ coldTime > 0 ? `请稍后 ${coldTime} 秒` : '获取验证码' }}
            </el-button>
          </el-col>
        </el-row>
      </el-form-item>

      <div style="margin-top: 15px">
        <el-button class="login-btn" size="large" type="primary" @click="checkEmail" :loading="loading" v-ripple>
          {{ $t('校验') }}
        </el-button>
      </div>

      <div class="footer">
        <p>
          {{ $t('forgetPassword.remenberPassword') }}
          <router-link to="/auth/login">{{ $t('立即登录') }}</router-link>
        </p>
      </div>

      <Copyright></Copyright>
    </el-form>

    <el-form ref="formRef2" :model="formData" :rules="rules" label-position="top" v-if="active === 1">
      <h3 class="title">{{ '设置新密码' }}</h3>
      <p class="sub-title">{{ '请输入您的新密码' }}</p>
      <el-form-item prop="password">
        <el-input
          v-model.trim="formData.password"
          :placeholder="$t('register.placeholder[1]')"
          size="large"
          type="password"
          show-password
        >
          <template #prefix>
            <el-icon><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="confirmPassword">
        <el-input
          v-model.trim="formData.confirmPassword"
          :placeholder="$t('register.placeholder[2]')"
          size="large"
          type="password"
          show-password
          autocomplete="off"
        >
          <template #prefix>
            <el-icon><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>

      <div style="margin-top: 15px">
        <el-button class="login-btn" size="large" type="primary" @click="resetPassword" :loading="loading" v-ripple>
          {{ '提交' }}
        </el-button>
      </div>

      <div class="footer">
        <p>
          {{ $t('forgetPassword.remenberPassword') }}
          <router-link to="/auth/login">{{ $t('立即登录') }}</router-link>
        </p>
      </div>

      <Copyright></Copyright>
    </el-form>
  </div>
</template>

<script setup lang="ts">
  import { authService } from '@/api/login/authApi'
  import { ApiStatus } from '@/constant/apiStatusEnum'
  import { useI18n } from 'vue-i18n'

  const { t } = useI18n()
  const router = useRouter()
  const showInputLabel = ref(false)
  const active = ref(0)

  const username = ref('')
  const loading = ref(false)

  const isCounting = ref(false)
  const coldTime = ref(0)
  let timer: number | undefined

  const formRef = ref<FormInstance>()
  const formRef2 = ref<FormInstance>()

  const formData = reactive({
    email: '',
    code: '',
    password: '',
    confirmPassword: ''
  })

  const validatePass = (rule: any, value: string, callback: any) => {
    if (value === '') {
      callback(new Error(t('register.placeholder[1]')))
    } else {
      if (formData.confirmPassword !== '') {
        formRef.value?.validateField('confirmPassword')
      }
      callback()
    }
  }

  const validatePass2 = (rule: any, value: string, callback: any) => {
    if (value === '') {
      callback(new Error(t('register.rule[0]')))
    } else if (value !== formData.password) {
      callback(new Error(t('register.rule[1]')))
    } else {
      callback()
    }
  }

  // const rules = []
  const rules = reactive<FormRules>({
    email: [
      { required: true, message: '请输入邮箱地址', trigger: 'blur' },
      {
        pattern: /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/,
        message: '邮箱格式不正确',
        trigger: 'blur'
      }
    ],
    code: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
    password: [
      { required: true, validator: validatePass, trigger: 'blur' },
      { min: 6, message: t('register.rule[3]'), trigger: 'blur' }
    ],
    confirmPassword: [{ required: true, validator: validatePass2, trigger: 'blur' }]
  })

  const isEmailValid = computed(() => /^[\w\.-]+@[\w\.-]+\.\w+$/.test(formData.email))

  const askCode = async () => {
    if (isEmailValid.value || !isCounting.value) {
      const params = {
        email: formData.email,
        type: 'reset'
      }
      const res = await authService.askCode(params)
      if (res && res.code === ApiStatus.success) {
        ElMessage.success(`验证码已发送到:${formData.email}，请注意查收`)
      }
      startCountdown()
    }
  }

  // 开始倒计时
  const startCountdown = () => {
    isCounting.value = true
    coldTime.value = 60

    timer = window.setInterval(() => {
      coldTime.value--
      if (coldTime.value <= 0) {
        clearInterval(timer)
        isCounting.value = false
      }
    }, 1000)
  }

  const checkEmail = async () => {
    if (!formRef.value) return

    await formRef.value.validate(async (valid) => {
      if (valid) {
        loading.value = true

        const params = {
          email: formData.email,
          code: formData.code
        }
        const res = await authService.checkEmail(params)

        if (res && res.code === ApiStatus.success) {
          ElMessage.success('校验成功')
          active.value = 1
        } else {
          ElMessage.error('校验失败', res.message)
        }

        try {
        } catch (error) {
          console.log('验证失败', error)
        } finally {
          loading.value = false
        }
      }
    })
  }

  const resetPassword = async () => {
    if (!formRef2.value) return

    await formRef2.value.validate(async (valid) => {
      if (valid) {
        loading.value = true

        const params = {
          email: formData.email,
          password: formData.password
        }
        console.log('resetPassword', params)
        const res = await authService.resetPassword(params)

        if (res && res.code === ApiStatus.success) {
          ElMessage.success('密码设置成功')
          router.push({
            path: '/auth/login',
            query: {
              // username: yourUsername,
              // password: yourNewPassword
            }
          })
        } else {
          ElMessage.error('密码设置失败', res.message)
        }

        try {
        } catch (error) {
          console.log('验证失败', error)
        } finally {
          loading.value = false
        }
      }
    })
  }
</script>

<style lang="scss" scoped>
  @use './wrap';
</style>
