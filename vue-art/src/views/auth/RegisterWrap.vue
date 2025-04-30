<template>
  <div class="form-wrap">
    <el-form ref="formRef" :model="formData" :rules="rules" label-position="top">
      <h3 class="title">{{ $t('register.title') }}</h3>
      <p class="sub-title">{{ $t('register.subTitle') }}</p>

      <el-form-item prop="username">
        <el-input v-model.trim="formData.username" :placeholder="$t('register.placeholder[0]')" size="large">
          <template #prefix>
            <el-icon><User /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model.trim="formData.password"
          :placeholder="$t('register.placeholder[1]')"
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
      <el-form-item prop="confirmPassword">
        <el-input
          v-model.trim="formData.confirmPassword"
          :placeholder="$t('register.placeholder[2]')"
          size="large"
          type="password"
          show-password
          autocomplete="off"
          @keyup.enter="register"
        >
          <template #prefix>
            <el-icon><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="email">
        <el-input v-model.trim="formData.email" size="large" :placeholder="$t('forgetPassword.placeholder')">
          <template #prefix>
            <el-icon><Message /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="code">
        <el-row justify="space-between" style="width: 100%">
          <el-col :span="15">
            <el-input v-model.trim="formData.code" size="large" maxlength="6" type="text" placeholder="请输入验证码">
              <template #prefix>
                <el-icon><EditPen /></el-icon>
              </template>
            </el-input>
          </el-col>
          <el-col :span="8" :offset="1">
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
      <el-form-item prop="agreement">
        <el-checkbox v-model="formData.agreement">
          {{ $t('register.agreeText') }}
          <router-link style="color: var(--main-color); text-decoration: none" to="/privacy-policy">{{
            $t('register.privacyPolicy')
          }}</router-link>
        </el-checkbox>
      </el-form-item>

      <div style="margin-top: 15px">
        <el-button class="login-btn" size="large" type="primary" @click="register" :loading="loading" v-ripple>
          {{ $t('register.submitBtnText') }}
        </el-button>
      </div>

      <div class="footer">
        <p>
          {{ $t('register.hasAccount') }}
          <router-link to="/auth/login">{{ $t('register.toLogin') }}</router-link>
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
  const formRef = ref<FormInstance>()
  const loading = ref(false)

  const isCounting = ref(false)
  const coldTime = ref(0)
  let timer: number | undefined

  const formData = reactive({
    username: '',
    password: '',
    confirmPassword: '',
    email: '',
    code: '',
    agreement: false
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

  const rules = reactive<FormRules>({
    username: [
      { required: true, message: t('register.placeholder[0]'), trigger: 'blur' },
      { min: 3, max: 20, message: t('register.rule[2]'), trigger: 'blur' }
    ],
    password: [
      { required: true, validator: validatePass, trigger: 'blur' },
      { min: 6, message: t('register.rule[3]'), trigger: 'blur' }
    ],
    confirmPassword: [{ required: true, validator: validatePass2, trigger: 'blur' }],
    email: [
      { required: true, message: '请输入邮箱地址', trigger: 'blur' },
      {
        pattern: /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/,
        message: '邮箱格式不正确',
        trigger: 'blur'
      }
    ],
    code: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
    agreement: [
      {
        validator: (rule: any, value: boolean, callback: any) => {
          if (!value) {
            callback(new Error(t('register.rule[4]')))
          } else {
            callback()
          }
        },
        trigger: 'change'
      }
    ]
  })

  const isEmailValid = computed(() => /^[\w\.-]+@[\w\.-]+\.\w+$/.test(formData.email))

  const askCode = async () => {
    if (isEmailValid.value || !isCounting.value) {
      const params = {
        email: formData.email,
        type: 'register'
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

  const register = async () => {
    if (!formRef.value) return

    await formRef.value.validate(async (valid) => {
      if (valid) {
        loading.value = true

        console.log('formData', formData)

        try {
          const res = await authService.register(formData)

          if (res && res.code === ApiStatus.success) {
            ElMessage.success('注册成功')
            router.push('/auth/login')
          } else {
            ElMessage.error(res.msg)
          }
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
