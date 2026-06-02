<template>
  <div class="page-container">
    <div class="page-header">
      <h3 class="page-title">预约管理</h3>
      <el-button type="primary" @click="showDialog()">
        <el-icon><Plus /></el-icon>新增预约
      </el-button>
    </div>

    <div class="search-bar">
      <el-form :inline="true" :model="query">
        <el-form-item label="状态">
          <el-select v-model="query.status" clearable placeholder="全部" style="width: 120px">
            <el-option label="待确认" :value="1" /><el-option label="已确认" :value="2" /><el-option label="已完成" :value="3" /><el-option label="已取消" :value="4" /><el-option label="已改期" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="日期">
          <el-date-picker v-model="query.date" type="date" style="width: 150px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="query.status = ''; query.date = ''; loadData()">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="tableData" stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="70" align="center" />
      <el-table-column prop="appointmentDate" label="日期" width="120" />
      <el-table-column prop="startTime" label="开始" width="80" />
      <el-table-column prop="endTime" label="结束" width="80" />
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="statusMap[row.status]" size="small" effect="plain">{{ statusLabel[row.status] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="source" label="来源" width="80" align="center">
        <template #default="{ row }">{{ row.source === 1 ? '自助' : '前台' }}</template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" min-width="120" />
      <el-table-column label="操作" width="200" fixed="right" align="center">
        <template #default="{ row }">
          <el-button v-if="row.status === 1" link type="primary" @click="handleConfirm(row.id)">确认</el-button>
          <el-button v-if="row.status === 2" link type="success" @click="handleComplete(row.id)">完成</el-button>
          <el-button v-if="row.status === 1 || row.status === 2" link type="danger" @click="handleCancel(row.id)">取消</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-wrap">
      <el-pagination v-model:current-page="query.current" v-model:page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="loadData" />
    </div>

    <el-dialog v-model="dialogVisible" title="新增预约" width="500px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="客户" prop="customerId">
          <CustomerSelect v-model="form.customerId" />
        </el-form-item>
        <el-form-item v-if="isBoss" label="所属门店" prop="storeId">
          <el-select v-model="form.storeId" placeholder="请选择门店" style="width: 100%">
            <el-option v-for="s in storeListData" :key="s.id" :label="s.storeName" :value="s.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="服务项目" prop="serviceItemId">
          <el-select v-model="form.serviceItemId" placeholder="请选择服务项目" style="width: 100%">
            <el-option v-for="item in serviceItemList" :key="item.id" :label="`${item.name} - ¥${item.price}`" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="美容师">
          <el-select v-model="form.employeeId" placeholder="请选择美容师" clearable style="width: 100%">
            <el-option v-for="e in employeeList" :key="e.id" :label="e.name" :value="e.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="预约日期" prop="appointmentDate"><el-date-picker v-model="form.appointmentDate" type="date" style="width: 100%" /></el-form-item>
        <el-form-item label="开始时间" prop="startTime"><el-time-picker v-model="form.startTime" format="HH:mm" style="width: 100%" /></el-form-item>
        <el-form-item label="结束时间" prop="endTime"><el-time-picker v-model="form.endTime" format="HH:mm" style="width: 100%" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" /></el-form-item>
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
import { ElMessage } from 'element-plus'
import { appointmentPage, appointmentAdd, appointmentConfirm, appointmentCancel, appointmentComplete, storePage, serviceItemList as fetchServiceItemList, employeeList as fetchEmployeeList } from '../../api'
import { useUserStore } from '../../store/user'
import CustomerSelect from '../../components/CustomerSelect.vue'

const userStore = useUserStore()
const isBoss = computed(() => userStore.userInfo.role === 1)
const storeListData = ref([])
const serviceItemList = ref([])
const employeeList = ref([])

async function loadStores() {
  if (!isBoss.value) return
  const res = await storePage({ current: 1, size: 100 })
  storeListData.value = res.data.records
}

async function loadServiceItems() {
  const storeId = isBoss.value ? undefined : userStore.userInfo.storeId
  const res = await fetchServiceItemList({ storeId })
  serviceItemList.value = res.data
}

async function loadEmployees() {
  const storeId = isBoss.value ? undefined : userStore.userInfo.storeId
  const res = await fetchEmployeeList({ storeId })
  employeeList.value = res.data.filter(e => e.role === 4)
}

const statusLabel = { 1: '待确认', 2: '已确认', 3: '已完成', 4: '已取消', 5: '已改期' }
const statusMap = { 1: 'warning', 2: '', 3: 'success', 4: 'danger', 5: 'info' }

const loading = ref(false)
const query = reactive({ current: 1, size: 10, status: '', date: '' })
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const formRef = ref()
const form = reactive({ customerId: '', storeId: '', serviceItemId: '', employeeId: '', appointmentDate: '', startTime: '', endTime: '', remark: '' })
const rules = { customerId: [{ required: true, message: '请选择客户', trigger: 'change' }], storeId: [{ required: true, message: '请选择门店', trigger: 'change' }], serviceItemId: [{ required: true, message: '请选择服务项目', trigger: 'change' }] }

async function loadData() { loading.value = true; try { const res = await appointmentPage(query); tableData.value = res.data.records; total.value = res.data.total } finally { loading.value = false } }
function showDialog() {
  Object.assign(form, { customerId: '', storeId: isBoss.value ? '' : userStore.userInfo.storeId, serviceItemId: '', employeeId: '', appointmentDate: '', startTime: '', endTime: '', remark: '' })
  dialogVisible.value = true
  loadServiceItems()
  loadEmployees()
}
async function handleSubmit() { await formRef.value.validate(); if (!form.customerId) return ElMessage.warning('请先搜索并选择客户'); await appointmentAdd(form); ElMessage.success('预约成功'); dialogVisible.value = false; loadData() }
async function handleConfirm(id) { await appointmentConfirm(id); ElMessage.success('已确认'); loadData() }
async function handleCancel(id) { await appointmentCancel(id); ElMessage.success('已取消'); loadData() }
async function handleComplete(id) { await appointmentComplete(id); ElMessage.success('已完成'); loadData() }

onMounted(() => { loadData(); loadStores() })
</script>

<style scoped>
.page-container { background: #fff; border-radius: var(--radius-md); box-shadow: var(--shadow-sm); }
.page-header { display: flex; justify-content: space-between; align-items: center; padding: 18px 24px; border-bottom: 1px solid var(--c-border-light); }
.page-title { font-size: 16px; font-weight: 600; color: var(--c-text); margin: 0; }
.search-bar { padding: 16px 24px 0; }
.pagination-wrap { padding: 16px 24px; display: flex; justify-content: flex-end; }
</style>
