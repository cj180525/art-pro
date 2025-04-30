<template>
  <div class="form-wrap">
    <el-form ref="formRef" :model="formData" :rules="rules" @keyup.enter="handleSubmit">
      <h3 class="title">{{ $t('login.title') }}</h3>
      <p class="sub-title">{{ $t('login.subTitle') }}</p>

      <el-form-item prop="username">
        <el-input v-model.trim="formData.username" :placeholder="$t('login.placeholder[0]')" size="large">
          <template #prefix>
            <el-icon><User /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          type="password"
          v-model.trim="formData.password"
          size="large"
          show-password
          :placeholder="$t('login.placeholder[1]')"
        >
          <template #prefix>
            <el-icon><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>

      <div class="drag-verify">
        <div class="drag-verify-content" :class="{ error: !isPassing && isClickPass }">
          <DragVerify
            ref="dragVerify"
            v-model:value="isPassing"
            :width="width < 500 ? 280 : 320"
            :text="$t('login.sliderText')"
            textColor="var(--art-gray-800)"
            :successText="$t('login.sliderSuccessText')"
            :progressBarBg="getCssVariable('--el-color-primary')"
            background="var(--art-gray-200)"
            handlerBg="var(--art-main-bg-color)"
            @pass="onPass"
          />
        </div>
        <p class="error-text" :class="{ 'show-error-text': !isPassing && isClickPass }">{{
          $t('login.placeholder[2]')
        }}</p>
      </div>

      <div class="forget-password">
        <el-checkbox v-model="formData.rememberPassword">{{ $t('login.rememberPwd') }}</el-checkbox>
        <router-link to="/forget-password">{{ $t('login.forgetPwd') }}</router-link>
      </div>

      <div style="margin-top: 20px">
        <el-button class="login-btn" size="large" type="primary" @click="handleSubmit" :loading="loading" v-ripple>
          {{ $t('login.btnText') }}
        </el-button>
      </div>

      <div class="footer">
        <p>
          {{ $t('login.noAccount') }}
          <router-link to="/auth/register">{{ $t('login.registerNow') }}</router-link>
        </p>
      </div>

      <Copyright></Copyright>
    </el-form>
  </div>
</template>

<script setup lang="ts">
  import { useUserStore } from '@/stores/user'
  import { authService } from '@/api/login/authApi'
  import { ApiStatus } from '@/constant/apiStatusEnum'
  import { saveTokenCookie } from '@/utils/cookie'
  import { useI18n } from 'vue-i18n'
  import { HOME_PAGE } from '@/router'
  import { getCssVariable } from '@/utils/utils'

  import { SystemInfo } from '@/config/setting'

  const userStore = useUserStore()
  const { t } = useI18n()
  const router = useRouter()
  const { width } = useWindowSize()

  const isPassing = ref(false)
  const isClickPass = ref(false)
  const loading = ref(false)
  const formRef = ref<FormInstance>()
  const formData = reactive({
    username: SystemInfo.login.username,
    password: SystemInfo.login.password,
    rememberPassword: true
  })

  const rules = computed<FormRules>(() => ({
    username: [{ required: true, message: t('login.placeholder[0]'), trigger: 'blur' }],
    password: [{ required: true, message: t('login.placeholder[1]'), trigger: 'blur' }]
  }))

  const onPass = () => {}

  const handleSubmit = async () => {
    if (!formRef.value) return

    await formRef.value.validate(async (valid) => {
      if (valid) {
        // 验证滑块是否通过
        if (!isPassing.value) {
          isClickPass.value = true
          return
        }
        loading.value = true

        try {
          const params = {
            username: formData.username,
            password: formData.password,
            rememberPassword: formData.rememberPassword
          }
          const res = await authService.login(params)

          if (res && res.code === ApiStatus.success && res.data) {
            const { token, expire, user } = res.data
            // 保存token
            saveTokenCookie(token, expire)
            // 保存用户信息
            userStore.setUserInfo(user)
            // 登录成功提示
            showLoginSuccessNotice()
            // 跳转首页
            router.push(HOME_PAGE)
          } else {
            ElMessage.error(res.msg)
          }
        } finally {
          loading.value = false
        }
      }
    })
  }

  // 登录成功提示
  const showLoginSuccessNotice = () => {
    setTimeout(() => {
      ElNotification({
        title: t('login.success.title'),
        type: 'success',
        showClose: true,
        duration: 2500,
        zIndex: 10000,
        message: `${t('login.success.message')}, ${SystemInfo.name}!`
      })
    }, 300)
  }
</script>

<style lang="scss" scoped>
  @use './wrap';
</style>
