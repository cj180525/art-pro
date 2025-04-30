<!-- 响应式选择器 -->
<template>
  <el-form-item :label="`${props.label}：`" :prop="props.prop">
    <el-config-provider :value-on-clear="() => null" :empty-values="[undefined, null]">
      <el-date-picker
        v-model="modelValue"
        :type="props.type"
        range-separator="-"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        :format="props.format"
        :value-format="props.format"
        :shortcuts="shortcuts"
        clearable
        style="width: 100%"
      />
    </el-config-provider>
  </el-form-item>
</template>

<script setup lang="ts">
  const modelValue = defineModel<Array | string | null>({ required: true })

  const props = defineProps({
    label: String,
    prop: String,
    type: String, // date, datetime, daterange, datetimerange
    format: String
  })

  const shortcuts = [
    {
      text: '前一天',
      value: () => {
        const end = new Date()
        const start = new Date()
        start.setDate(start.getDate() - 1)
        return [start, end]
      }
    },
    {
      text: '前一周',
      value: () => {
        const end = new Date()
        const start = new Date()
        start.setDate(start.getDate() - 7)
        return [start, end]
      }
    },
    {
      text: '前一月',
      value: () => {
        const end = new Date()
        const start = new Date()
        start.setMonth(start.getMonth() - 1)
        return [start, end]
      }
    },
    {
      text: '前三月',
      value: () => {
        const end = new Date()
        const start = new Date()
        start.setMonth(start.getMonth() - 3)
        return [start, end]
      }
    },
    {
      text: '前一年',
      value: () => {
        const end = new Date()
        const start = new Date()
        start.setMonth(start.getMonth() - 12)
        return [start, end]
      }
    }
  ]
</script>

<style lang="scss" scoped></style>
