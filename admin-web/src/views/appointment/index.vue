<template>
  <el-card>
    <template #header>
      <div style="display: flex; justify-content: space-between; align-items: center">
        <span>预约管理</span>
        <el-button type="primary" @click="showDialog()">新增预约</el-button>
      </div>
    </template>
    <el-form :inline="true" :model="query" style="margin-bottom: 16px">
      <el-form-item label="状态"><el-select v-model="query.status" clearable placeholder="全部"><el-option label="待确认" value="PENDING" /><el-option label="已确认" value="CONFIRMED" /><el-option label="已取消" value="CANCELLED" /><el-option label="已完成" value="COMPLETED" /></el-select></el-form-item>
      <el-form-item label="日期"><el-date-picker v-model="query.date" type="date" /></el-form-item>
      <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
    </el-form>
    <el-table :data="tableData" border stripe>
      <el-table-column prop="appointmentDate" label="日期" width="120" />
      <el-table-column prop="startTime" label="开始时间" width="100" />
      <el-table-column prop="endTime" label="结束时间" width="100" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusMap[row.status]">{{ statusLabel[row.status] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="source" label="来源" width="80">
        <template #default="{ row }">{{ row.source === 'SELF' ? '自助' : '前台' }}</template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" />
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button v-if="row.status !== 'CANCELLED' && row.status !== 'COMPLETED'" link type="success" @click="handleComplete(row.id)">完成</el-button>
          <el-button v-if="row.status !== 'CANCELLED' && row.status !== 'COMPLETED'" link type="danger" @click="handleCancel(row.id)">取消</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination style="margin-top: 16px; justify-content: flex-end" v-model:current-page="query.current" v-model:page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="loadData" />

    <el-dialog v-model="dialogVisible" title="新增预约" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="客户ID" prop="customerId"><el-input v-model.number="form.customerId" /></el-form-item>
        <el-form-item label="门店ID" prop="storeId"><el-input v-model.number="form.storeId" /></el-form-item>
        <el-form-item label="服务项目ID" prop="serviceItemId"><el-input v-model.number="form.serviceItemId" /></el-form-item>
        <el-form-item label="美容师ID"><el-input v-model.number="form.beauticianId" /></el-form-item>
        <el-form-item label="预约日期" prop="appointmentDate"><el-date-picker v-model="form.appointmentDate" type="date" /></el-form-item>
        <el-form-item label="开始时间" prop="startTime"><el-time-picker v-model="form.startTime" format="HH:mm" /></el-form-item>
        <el-form-item label="结束时间" prop="endTime"><el-time-picker v-model="form.endTime" format="HH:mm" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { appointmentPage, appointmentAdd, appointmentCancel, appointmentComplete } from '../../api'

const statusLabel = { PENDING: '待确认', CONFIRMED: '已确认', CANCELLED: '已取消', COMPLETED: '已完成' }
const statusMap = { PENDING: 'warning', CONFIRMED: '', CANCELLED: 'danger', COMPLETED: 'success' }

const query = reactive({ current: 1, size: 10, status: '', date: '' })
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const formRef = ref()
const form = reactive({ customerId: '', storeId: '', serviceItemId: '', beauticianId: '', appointmentDate: '', startTime: '', endTime: '', remark: '' })
const rules = { customerId: [{ required: true, message: '必填', trigger: 'blur' }], storeId: [{ required: true, message: '必填', trigger: 'blur' }], serviceItemId: [{ required: true, message: '必填', trigger: 'blur' }] }

async function loadData() { const res = await appointmentPage(query); tableData.value = res.data.records; total.value = res.data.total }
function showDialog() { Object.assign(form, { customerId: '', storeId: '', serviceItemId: '', beauticianId: '', appointmentDate: '', startTime: '', endTime: '', remark: '' }); dialogVisible.value = true }
async function handleSubmit() { await formRef.value.validate(); await appointmentAdd(form); ElMessage.success('预约成功'); dialogVisible.value = false; loadData() }
async function handleCancel(id) { await appointmentCancel(id); ElMessage.success('已取消'); loadData() }
async function handleComplete(id) { await appointmentComplete(id); ElMessage.success('已完成'); loadData() }

onMounted(loadData)
</script>
