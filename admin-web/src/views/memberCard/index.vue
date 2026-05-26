<template>
  <el-card>
    <template #header>
      <div style="display: flex; justify-content: space-between; align-items: center">
        <span>卡项管理</span>
        <el-button type="primary" @click="showDialog()">新增卡项</el-button>
      </div>
    </template>
    <el-form :inline="true" :model="query" style="margin-bottom: 16px">
      <el-form-item label="卡类型"><el-select v-model="query.cardType" clearable><el-option label="次卡" value="COUNT" /><el-option label="年卡" value="YEAR" /><el-option label="月卡" value="MONTH" /></el-select></el-form-item>
      <el-form-item label="状态"><el-select v-model="query.status" clearable><el-option label="生效中" value="ACTIVE" /><el-option label="已过期" value="EXPIRED" /><el-option label="已退款" value="REFUNDED" /></el-select></el-form-item>
      <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
    </el-form>
    <el-table :data="tableData" border stripe>
      <el-table-column prop="name" label="卡名称" width="150" />
      <el-table-column prop="cardType" label="类型" width="80">
        <template #default="{ row }">{{ { COUNT: '次卡', YEAR: '年卡', MONTH: '月卡' }[row.cardType] }}</template>
      </el-table-column>
      <el-table-column prop="price" label="价格" width="100" />
      <el-table-column prop="totalCount" label="总次数" width="80" />
      <el-table-column prop="remainingCount" label="剩余次数" width="80" />
      <el-table-column prop="startDate" label="开始日期" width="120" />
      <el-table-column prop="endDate" label="到期日期" width="120" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }"><el-tag :type="row.status === 'ACTIVE' ? 'success' : 'info'">{{ { ACTIVE: '生效中', EXPIRED: '已过期', REFUNDED: '已退款' }[row.status] }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button v-if="row.cardType === 'COUNT' && row.status === 'ACTIVE'" link type="primary" @click="handleUse(row.id)">核销</el-button>
          <el-button link type="primary" @click="showDialog(row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination style="margin-top: 16px; justify-content: flex-end" v-model:current-page="query.current" v-model:page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="loadData" />

    <el-dialog v-model="dialogVisible" :title="editId ? '编辑卡项' : '新增卡项'" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="客户ID" prop="customerId"><el-input v-model.number="form.customerId" /></el-form-item>
        <el-form-item label="门店ID" prop="storeId"><el-input v-model.number="form.storeId" /></el-form-item>
        <el-form-item label="卡类型" prop="cardType"><el-select v-model="form.cardType"><el-option label="次卡" value="COUNT" /><el-option label="年卡" value="YEAR" /><el-option label="月卡" value="MONTH" /></el-select></el-form-item>
        <el-form-item label="卡名称" prop="name"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="价格" prop="price"><el-input-number v-model="form.price" :min="0" /></el-form-item>
        <el-form-item v-if="form.cardType === 'COUNT'" label="总次数" prop="totalCount"><el-input-number v-model="form.totalCount" :min="1" /></el-form-item>
        <el-form-item label="开始日期"><el-date-picker v-model="form.startDate" type="date" /></el-form-item>
        <el-form-item label="到期日期"><el-date-picker v-model="form.endDate" type="date" /></el-form-item>
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
import { memberCardPage, memberCardAdd, memberCardUpdate, memberCardUse } from '../../api'

const query = reactive({ current: 1, size: 10, cardType: '', status: '' })
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const editId = ref(null)
const formRef = ref()
const form = reactive({ customerId: '', storeId: '', cardType: 'COUNT', name: '', price: 0, totalCount: 6, startDate: '', endDate: '' })
const rules = { customerId: [{ required: true, message: '必填', trigger: 'blur' }], storeId: [{ required: true, message: '必填', trigger: 'blur' }], cardType: [{ required: true, message: '必填', trigger: 'change' }], name: [{ required: true, message: '必填', trigger: 'blur' }] }

async function loadData() { const res = await memberCardPage(query); tableData.value = res.data.records; total.value = res.data.total }
function showDialog(row) { editId.value = row?.id || null; if (row) Object.assign(form, row); else Object.assign(form, { customerId: '', storeId: '', cardType: 'COUNT', name: '', price: 0, totalCount: 6, startDate: '', endDate: '' }); dialogVisible.value = true }
async function handleSubmit() { await formRef.value.validate(); if (editId.value) { await memberCardUpdate({ ...form, id: editId.value }) } else { await memberCardAdd(form) }; ElMessage.success('操作成功'); dialogVisible.value = false; loadData() }
async function handleUse(id) { await memberCardUse(id); ElMessage.success('核销成功'); loadData() }

onMounted(loadData)
</script>
