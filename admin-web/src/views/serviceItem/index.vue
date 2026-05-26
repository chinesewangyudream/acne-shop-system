<template>
  <el-card>
    <template #header>
      <div style="display: flex; justify-content: space-between; align-items: center">
        <span>服务项目</span>
        <el-button type="primary" @click="showDialog()">新增项目</el-button>
      </div>
    </template>
    <el-table :data="tableData" border stripe>
      <el-table-column prop="name" label="项目名称" />
      <el-table-column prop="price" label="价格" width="100" />
      <el-table-column prop="duration" label="时长(分钟)" width="100" />
      <el-table-column prop="description" label="描述" />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="showDialog(row)">编辑</el-button>
          <el-popconfirm title="确定删除？" @confirm="handleDelete(row.id)"><template #reference><el-button link type="danger">删除</el-button></template></el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination style="margin-top: 16px; justify-content: flex-end" v-model:current-page="query.current" v-model:page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="loadData" />

    <el-dialog v-model="dialogVisible" :title="editId ? '编辑项目' : '新增项目'" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="name"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="价格" prop="price"><el-input-number v-model="form.price" :min="0" /></el-form-item>
        <el-form-item label="时长(分)" prop="duration"><el-input-number v-model="form.duration" :min="1" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSubmit">确定</el-button></template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { serviceItemPage, serviceItemAdd, serviceItemUpdate, serviceItemDelete } from '../../api'

const query = reactive({ current: 1, size: 10, storeId: null })
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const editId = ref(null)
const formRef = ref()
const form = reactive({ name: '', price: 0, duration: 30, description: '' })
const rules = { name: [{ required: true, message: '必填', trigger: 'blur' }] }

async function loadData() { const res = await serviceItemPage(query); tableData.value = res.data.records; total.value = res.data.total }
function showDialog(row) { editId.value = row?.id || null; if (row) Object.assign(form, row); else Object.assign(form, { name: '', price: 0, duration: 30, description: '' }); dialogVisible.value = true }
async function handleSubmit() { await formRef.value.validate(); if (editId.value) { await serviceItemUpdate({ ...form, id: editId.value }) } else { await serviceItemAdd(form) }; ElMessage.success('操作成功'); dialogVisible.value = false; loadData() }
async function handleDelete(id) { await serviceItemDelete(id); ElMessage.success('删除成功'); loadData() }

onMounted(loadData)
</script>
