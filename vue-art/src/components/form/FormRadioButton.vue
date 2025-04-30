<!-- 响应式选择器 -->
<template>
  <el-form-item :label="`${label}：`" :prop="prop">
    <el-config-provider :value-on-clear="() => ''" :empty-values="[undefined, null, '']">
      <el-radio-group v-model="modelValue">
        <el-radio-button v-for="item in options" :label="item.value" :value="item.value">{{
          item.label
        }}</el-radio-button>
      </el-radio-group>
    </el-config-provider>
  </el-form-item>
</template>

<script setup lang="ts">
  const modelValue = defineModel<number | string>({ required: true })

  defineProps<{
    label: string
    prop: string
    options: any[]
  }>()
</script>

<style lang="scss" scoped></style>
