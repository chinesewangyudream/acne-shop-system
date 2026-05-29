<template>
  <div style="display: flex; gap: 8px; width: 100%; align-items: center">
    <el-input
      v-model="searchName"
      placeholder="输入客户姓名或手机号搜索"
      clearable
      :disabled="disabled"
      style="flex: 1"
      @input="onInput"
      @clear="onClear"
    />
    <el-button type="primary" @click="doSearch" :disabled="disabled">搜索</el-button>
  </div>
  <!-- 已选中展示 -->
  <div v-if="modelValue && selectedCustomer" class="customer-selected">
    <el-tag closable @close="onClear" type="" effect="plain">
      {{ selectedCustomer.name }} | {{ selectedCustomer.phone }}
      <span v-if="selectedCustomer.gender !== null && selectedCustomer.gender !== undefined">
        | {{ selectedCustomer.gender === 1 ? '男' : '女' }}
      </span>
      <span v-if="selectedCustomer.age"> | {{ selectedCustomer.age }}岁</span>
      <span v-if="selectedCustomer.acneType"> | {{ selectedCustomer.acneType }}</span>
    </el-tag>
  </div>

  <!-- 搜索结果弹窗 -->
  <el-dialog v-model="dialogVisible" title="选择客户" width="650px" append-to-body destroy-on-close>
    <el-table :data="customerList" highlight-current-row @row-click="selectCustomer" style="cursor: pointer" max-height="400">
      <el-table-column prop="name" label="姓名" width="80" />
      <el-table-column prop="phone" label="手机号" width="120" />
      <el-table-column prop="gender" label="性别" width="60" align="center">
        <template #default="{ row }">{{ row.gender === 1 ? '男' : '女' }}</template>
      </el-table-column>
      <el-table-column prop="age" label="年龄" width="60" align="center" />
      <el-table-column prop="acneType" label="痘痘类型" width="100" />
      <el-table-column prop="severity" label="严重程度" width="80" align="center">
        <template #default="{ row }">{{ { 1: '轻度', 2: '中度', 3: '重度' }[row.severity] || '-' }}</template>
      </el-table-column>
      <el-table-column prop="lastVisitAt" label="最后到店" min-width="100" />
    </el-table>
    <div v-if="customerList.length === 0" style="text-align: center; color: #999; padding: 30px 0">未找到匹配的客户</div>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { customerPage } from '../api'

const props = defineProps({
  modelValue: { type: [Number, String], default: '' },
  disabled: { type: Boolean, default: false },
  /** 编辑模式传入已选客户信息，避免初始空白 */
  initialCustomer: { type: Object, default: null }
})

const emit = defineEmits(['update:modelValue'])

const searchName = ref('')
const dialogVisible = ref(false)
const customerList = ref([])
const selectedCustomer = ref(null)

// 外部传入初始客户
watch(() => props.initialCustomer, (val) => {
  if (val) {
    selectedCustomer.value = val
    searchName.value = val.name || ''
  }
}, { immediate: true })

// modelValue 变化时同步
watch(() => props.modelValue, (val) => {
  if (!val) {
    selectedCustomer.value = null
    searchName.value = ''
  }
})

function onInput() {
  // 输入时清空已选
  if (selectedCustomer.value && searchName.value !== selectedCustomer.value.name) {
    selectedCustomer.value = null
    emit('update:modelValue', '')
  }
}

function onClear() {
  selectedCustomer.value = null
  searchName.value = ''
  emit('update:modelValue', '')
}

async function doSearch() {
  if (!searchName.value) {
    ElMessage.warning('请输入客户姓名或手机号')
    return
  }
  const res = await customerPage({ name: searchName.value, current: 1, size: 50 })
  const phoneRes = await customerPage({ phone: searchName.value, current: 1, size: 50 })
  // 合并去重
  const map = new Map()
  ;[...res.data.records, ...phoneRes.data.records].forEach(c => map.set(c.id, c))
  customerList.value = [...map.values()]

  if (customerList.value.length === 0) {
    ElMessage.warning('未找到匹配的客户')
    return
  }
  if (customerList.value.length === 1) {
    selectCustomer(customerList.value[0])
    return
  }
  dialogVisible.value = true
}

function selectCustomer(row) {
  selectedCustomer.value = row
  searchName.value = row.name
  emit('update:modelValue', row.id)
  dialogVisible.value = false
}
</script>

<style scoped>
.customer-selected {
  margin-top: 6px;
  line-height: 1;
}
</style>
