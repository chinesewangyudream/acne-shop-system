<template>
  <div class="page-container">
    <div class="page-header">
      <h3 class="page-title">核销记录</h3>
    </div>

    <div class="search-bar">
      <el-form :inline="true" :model="query">
        <el-form-item label="卡类型">
          <el-select v-model="query.cardType" clearable placeholder="全部" style="width: 110px">
            <el-option label="次卡" :value="1" />
            <el-option label="年月卡" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="客户ID">
          <el-input v-model="query.customerId" placeholder="客户ID" clearable style="width: 100px" />
        </el-form-item>
        <el-form-item v-if="isBoss" label="门店">
          <el-select v-model="query.storeId" clearable placeholder="全部" style="width: 140px">
            <el-option v-for="s in storeListData" :key="s.id" :label="s.storeName" :value="s.id" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="tableData" stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="70" align="center" />
      <el-table-column prop="customerId" label="客户" width="90" align="center" />
      <el-table-column prop="cardType" label="卡类型" width="90" align="center">
        <template #default="{ row }">
          <el-tag :type="row.cardType === 1 ? 'success' : 'warning'" size="small" effect="plain">
            {{ row.cardType === 1 ? '次卡' : '年月卡' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="cardId" label="卡项ID" width="90" align="center" />
      <el-table-column prop="serviceItemId" label="服务项目" width="100" align="center">
        <template #default="{ row }">{{ row.serviceItemId || '-' }}</template>
      </el-table-column>
      <el-table-column prop="operatorId" label="操作人" width="90" align="center">
        <template #default="{ row }">{{ row.operatorId || '-' }}</template>
      </el-table-column>
      <el-table-column prop="remainingCount" label="剩余次数" width="90" align="center">
        <template #default="{ row }">{{ row.remainingCount != null ? row.remainingCount : '-' }}</template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" min-width="120" show-overflow-tooltip>
        <template #default="{ row }">{{ row.remark || '-' }}</template>
      </el-table-column>
      <el-table-column prop="createdAt" label="核销时间" width="170" align="center" />
    </el-table>

    <div class="pagination-wrap">
      <el-pagination v-model:current-page="query.current" v-model:page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="loadData" />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { writeoffRecordPage, storePage } from '../../api'
import { useUserStore } from '../../store/user'

const userStore = useUserStore()
const isBoss = computed(() => userStore.userInfo.role === 1)
const storeListData = ref([])

async function loadStores() {
  if (!isBoss.value) return
  const res = await storePage({ current: 1, size: 100 })
  storeListData.value = res.data.records
}

const loading = ref(false)
const query = reactive({ current: 1, size: 10, cardType: '', customerId: '', storeId: '' })
const tableData = ref([])
const total = ref(0)

async function loadData() {
  loading.value = true
  try {
    const params = { current: query.current, size: query.size }
    if (query.cardType !== '') params.cardType = query.cardType
    if (query.customerId) params.customerId = query.customerId
    if (query.storeId) params.storeId = query.storeId
    const res = await writeoffRecordPage(params)
    tableData.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

function resetQuery() {
  query.cardType = ''
  query.customerId = ''
  query.storeId = ''
  loadData()
}

onMounted(() => { loadData(); loadStores() })
</script>

<style scoped>
.page-container { background: #fff; border-radius: var(--radius-md); box-shadow: var(--shadow-sm); }
.page-header { display: flex; justify-content: space-between; align-items: center; padding: 18px 24px; border-bottom: 1px solid var(--c-border-light); }
.page-title { font-size: 16px; font-weight: 600; color: var(--c-text); margin: 0; }
.search-bar { padding: 16px 24px 0; }
.pagination-wrap { padding: 16px 24px; display: flex; justify-content: flex-end; }
</style>
