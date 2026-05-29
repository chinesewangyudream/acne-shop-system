<template>
  <div class="page-container">
    <div class="page-header">
      <h3 class="page-title">次卡管理</h3>
      <el-button type="primary" @click="showDialog()">
        <el-icon><Plus /></el-icon>办次卡
      </el-button>
    </div>

    <div class="search-bar">
      <el-form :inline="true" :model="query">
        <el-form-item label="状态">
          <el-select v-model="query.status" clearable placeholder="全部" style="width: 110px">
            <el-option label="正常" :value="1" /><el-option label="已退" :value="0" /><el-option label="已用完" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="query.status = ''; loadData()">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="tableData" stripe>
      <el-table-column prop="id" label="ID" width="70" align="center" />
      <el-table-column prop="customerId" label="客户" width="90" align="center" />
      <el-table-column prop="treatmentPlanId" label="疗程" width="90" align="center" />
      <el-table-column prop="totalCount" label="总次数" width="80" align="center" />
      <el-table-column prop="remainingCount" label="剩余" width="80" align="center" />
      <el-table-column prop="purchasePrice" label="购买价格" width="100" align="right">
        <template #default="{ row }">¥{{ row.purchasePrice }}</template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="90" align="center">
        <template #default="{ row }">
          <el-tag :type="{ 1: 'success', 0: 'danger', 2: 'info' }[row.status]" size="small" effect="plain">
            {{ { 1: '正常', 0: '已退', 2: '已用完' }[row.status] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160" fixed="right" align="center">
        <template #default="{ row }">
          <el-button v-if="row.status === 1" link type="success" @click="handleUse(row.id)">核销</el-button>
          <el-button v-if="row.status === 1" link type="warning" @click="handleRefund(row.id)">退卡</el-button>
          <el-button link type="primary" @click="showDialog(row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-wrap">
      <el-pagination v-model:current-page="query.current" v-model:page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="loadData" />
    </div>

    <el-dialog v-model="dialogVisible" :title="editId ? '编辑次卡' : '办次卡'" width="500px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="客户" prop="customerId">
          <CustomerSelect v-model="form.customerId" :disabled="!!editId" :initial-customer="editCustomer" />
        </el-form-item>
        <el-form-item v-if="isBoss" label="所属门店" prop="storeId">
          <el-select v-model="form.storeId" placeholder="请选择门店" style="width: 100%">
            <el-option v-for="s in storeListData" :key="s.id" :label="s.storeName" :value="s.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="疗程方案">
          <el-select v-model="form.treatmentPlanId" placeholder="请选择疗程方案" clearable style="width: 100%">
            <el-option v-for="p in planList" :key="p.id" :label="`${p.name} - ¥${p.price}`" :value="p.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="总次数" prop="totalCount"><el-input-number v-model="form.totalCount" :min="1" style="width: 100%" /></el-form-item>
        <el-form-item label="价格" prop="purchasePrice"><el-input-number v-model="form.purchasePrice" :min="0" :precision="2" style="width: 100%" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { countCardPage, countCardAdd, countCardUpdate, countCardUse, countCardRefund, storePage, treatmentPlanPage } from '../../api'
import { useUserStore } from '../../store/user'
import CustomerSelect from '../../components/CustomerSelect.vue'

const userStore = useUserStore()
const isBoss = computed(() => userStore.userInfo.role === 1)
const storeListData = ref([])
const planList = ref([])
const editCustomer = ref(null)

async function loadStores() {
  if (!isBoss.value) return
  const res = await storePage({ current: 1, size: 100 })
  storeListData.value = res.data.records
}

async function loadPlans() {
  const storeId = isBoss.value ? undefined : userStore.userInfo.storeId
  const res = await treatmentPlanPage({ storeId, current: 1, size: 100 })
  planList.value = res.data.records
}

const query = reactive({ current: 1, size: 10, status: '' })
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const editId = ref(null)
const formRef = ref()
const form = reactive({ customerId: '', storeId: '', treatmentPlanId: '', totalCount: 6, purchasePrice: 0 })
const rules = { customerId: [{ required: true, message: '请选择客户', trigger: 'change' }], storeId: [{ required: true, message: '请选择门店', trigger: 'change' }], totalCount: [{ required: true, message: '必填', trigger: 'blur' }] }

async function loadData() { const res = await countCardPage(query); tableData.value = res.data.records; total.value = res.data.total }
function showDialog(row) {
  editId.value = row?.id || null
  editCustomer.value = null
  if (row) {
    Object.assign(form, row)
    if (row.customerName) editCustomer.value = { name: row.customerName, phone: row.customerPhone }
  } else {
    Object.assign(form, { customerId: '', storeId: isBoss.value ? '' : userStore.userInfo.storeId, treatmentPlanId: '', totalCount: 6, purchasePrice: 0 })
  }
  dialogVisible.value = true
  loadPlans()
}
async function handleSubmit() { await formRef.value.validate(); if (!form.customerId) return ElMessage.warning('请先搜索并选择客户'); if (editId.value) { await countCardUpdate({ ...form, id: editId.value }) } else { await countCardAdd(form) }; ElMessage.success('操作成功'); dialogVisible.value = false; loadData() }
async function handleUse(id) { await ElMessageBox.confirm('确定核销一次？', '核销'); await countCardUse(id); ElMessage.success('核销成功'); loadData() }
async function handleRefund(id) { await ElMessageBox.confirm('确定退卡？', '退卡'); await countCardRefund(id); ElMessage.success('退卡成功'); loadData() }

onMounted(() => { loadData(); loadStores() })
</script>

<style scoped>
.page-container { background: #fff; border-radius: var(--radius-md); box-shadow: var(--shadow-sm); }
.page-header { display: flex; justify-content: space-between; align-items: center; padding: 18px 24px; border-bottom: 1px solid var(--c-border-light); }
.page-title { font-size: 16px; font-weight: 600; color: var(--c-text); margin: 0; }
.search-bar { padding: 16px 24px 0; }
.pagination-wrap { padding: 16px 24px; display: flex; justify-content: flex-end; }
</style>
