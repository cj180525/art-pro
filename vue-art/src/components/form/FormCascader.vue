<!-- 响应式选择器 -->
<template>
  <el-form-item :label="`${label}：`" :prop="prop">
    <el-config-provider :value-on-clear="() => ''" :empty-values="[undefined, null, '']">
      <el-cascader
        ref="cascaderRef"
        v-model="modelValue"
        :show-all-levels="false"
        :props="props"
        :options="options"
        :placeholder="`请选择${label}`"
        @visible-change="handleVisibleChange"
        @change="handleChange"
        filterable
        clearable
        :disabled="disabled"
      />
    </el-config-provider>
  </el-form-item>
</template>

<script setup lang="ts">
  import type { CascaderProps } from 'element-plus'

  const cascaderRef = ref(null)

  const modelValue = defineModel<number | undefined>({ required: true })

  withDefaults(
    defineProps<{
      label: String
      prop: String
      props: CascaderProps
      disabled?: Boolean
      options: Array
    }>(),
    {
      disabled: false
    }
  )

  const emit = defineEmits(['visible-change'])

  const handleVisibleChange = (val: boolean) => {
    emit('visible-change', val)
  }

  const handleChange = (val: CascaderValue) => {
    cascaderRef.value?.togglePopperVisible(false)
  }
</script>
