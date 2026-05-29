<template>
  <div class="page-container">
    <div class="page-header">
      <h3 class="page-title">门店管理</h3>
      <el-button type="primary" @click="showDialog()">
        <el-icon><Plus /></el-icon>新增门店
      </el-button>
    </div>

    <el-table :data="tableData" stripe>
      <el-table-column prop="id" label="ID" width="70" align="center" />
      <el-table-column prop="storeName" label="门店名称" min-width="150" />
      <el-table-column prop="address" label="地址" min-width="200" />
      <el-table-column prop="phone" label="电话" width="150" />
      <el-table-column prop="businessHours" label="营业时间" width="140" />
      <el-table-column prop="status" label="状态" width="80" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small" effect="plain">{{ row.status === 1 ? '营业' : '停业' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="140" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="primary" @click="showDialog(row)">编辑</el-button>
          <el-popconfirm title="确定删除？" @confirm="handleDelete(row.id)"><template #reference><el-button link type="danger">删除</el-button></template></el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-wrap">
      <el-pagination v-model:current-page="query.current" v-model:page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="loadData" />
    </div>

    <el-dialog v-model="dialogVisible" :title="editId ? '编辑门店' : '新增门店'" width="500px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="storeName"><el-input v-model="form.storeName" /></el-form-item>
        <el-form-item label="地址"><el-input v-model="form.address" /></el-form-item>
        <el-form-item label="电话"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="营业时间"><el-input v-model="form.businessHours" placeholder="如 09:00-21:00" /></el-form-item>
        <el-form-item label="状态"><el-switch v-model="form.status" :active-value="1" :inactive-value="0" active-text="营业" inactive-text="停业" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSubmit">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { storePage, storeAdd, storeUpdate, storeDelete } from '../../api'

const query = reactive({ current: 1, size: 10 })
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const editId = ref(null)
const formRef = ref()
const form = reactive({ storeName: '', address: '', phone: '', businessHours: '', status: 1 })
const rules = { storeName: [{ required: true, message: '必填', trigger: 'blur' }] }

async function loadData() { const res = await storePage(query); tableData.value = res.data.records; total.value = res.data.total }
function showDialog(row) { editId.value = row?.id || null; if (row) Object.assign(form, row); else Object.assign(form, { storeName: '', address: '', phone: '', businessHours: '', status: 1 }); dialogVisible.value = true }
async function handleSubmit() { await formRef.value.validate(); if (editId.value) { await storeUpdate({ ...form, id: editId.value }) } else { await storeAdd(form) }; ElMessage.success('操作成功'); dialogVisible.value = false; loadData() }
async function handleDelete(id) { await storeDelete(id); ElMessage.success('删除成功'); loadData() }

onMounted(loadData)
</script>

<style scoped>
.page-container { background: #fff; border-radius: var(--radius-md); box-shadow: var(--shadow-sm); }
.page-header { display: flex; justify-content: space-between; align-items: center; padding: 18px 24px; border-bottom: 1px solid var(--c-border-light); }
.page-title { font-size: 16px; font-weight: 600; color: var(--c-text); margin: 0; }
.pagination-wrap { padding: 16px 24px; display: flex; justify-content: flex-end; }
</style>
