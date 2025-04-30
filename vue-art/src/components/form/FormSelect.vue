<!-- 响应式选择器 -->
<template>
  <el-form-item :label="`${label}：`" :prop="prop">
    <el-config-provider :value-on-clear="() => ''" :empty-values="[undefined, null, '']">
      <el-select v-model="modelValue" :placeholder="`请选择${label}`" :clearable="clearable" style="width: 100%">
        <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"> </el-option>
      </el-select>
    </el-config-provider>
  </el-form-item>
</template>

<script setup lang="ts">
  const modelValue = defineModel<number | string>({ required: true })

  interface Props {
    label: string
    prop: string
    clearable?: boolean
    options: any[]
  }

  withDefaults(defineProps<Props>(), {
    clearable: true
  })
</script>

<style lang="scss" scoped></style>
