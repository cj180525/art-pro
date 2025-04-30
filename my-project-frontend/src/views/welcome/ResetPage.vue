<script setup>

import {reactive, ref} from "vue";
import {EditPen, Lock, Message} from "@element-plus/icons-vue";

const active = ref(0)
const formRef = ref()
const form = reactive({
  email: '',
  code: '',
  password: '',
  password_repeat: ''
})

const rule = {
}

</script>

<template>

  <div style="text-align: center" >
    <div style="margin-top: 30px">
      <el-steps align-center :active="active" finish-status="success">
        <el-step title="验证电子邮件"/>
        <el-step title="重新设定密码"/>
      </el-steps>
    </div>
    <div style="margin: 0 20px" v-if="active === 0">
      <div style="margin-top: 80px">
        <div style="margin-top: 25px;font-weight: bold">重置密码</div>
        <div style="margin-top: 14px;color: gray">请输入重置密码的邮件地址</div>
      </div>
      <div style="margin-top: 50px">
        <el-form :model="form" rules="rule" ref="formRef">
          <el-form-item prop="email">
            <el-input v-model="form.email" type="email" placeholder="邮箱">
              <template #prefix>
                <el-icon><Message/></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="code">
            <el-row :gutter="10" style="width: 100%">
              <el-col :span="17">
                <el-input v-model="form.code" maxlength="6" type="text" placeholder="请输入验证码">
                  <template #prefix>
                    <el-icon><EditPen/></el-icon>
                  </template>
                </el-input>
              </el-col>
              <el-col :span="5">
                <el-button  type="success">获取验证码</el-button>
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>
      </div>
      <div style="margin-top: 80px">
        <el-button @click="active++" type="warning" style="width: 270px" plain>开始重置密码</el-button>
      </div>
    </div>
    <div style="margin: 0 20px" v-if="active === 1">
      <div style="margin-top: 80px">
        <div style="margin-top: 25px;font-weight: bold">重置密码</div>
        <div style="margin-top: 14px;color: gray">请输入您的新密码</div>
      </div>
      <div style="margin-top: 50px">
        <el-form :model="form" rules="rule" ref="formRef">
          <el-form-item prop="password">
            <el-input v-model="form.password" maxlength="20" type="password" placeholder="密码">
              <template #prefix>
                <el-icon><Lock/></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password_repeat">
            <el-input v-model="form.password_repeat" maxlength="20" type="password" placeholder="重复密码">
              <template #prefix>
                <el-icon><Lock/></el-icon>
              </template>
            </el-input>
          </el-form-item>
        </el-form>
      </div>
      <div style="margin-top: 80px">
        <el-button  type="danger" style="width: 270px" plain>立即重置密码</el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>