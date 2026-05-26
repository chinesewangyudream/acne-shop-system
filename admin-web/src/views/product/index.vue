<template>
  <el-card>
    <template #header>
      <div style="display: flex; justify-content: space-between; align-items: center">
        <span>产品管理</span>
        <el-button type="primary" @click="showDialog()">新增产品</el-button>
      </div>
    </template>
    <el-form :inline="true" :model="query" style="margin-bottom: 16px">
      <el-form-item label="名称"><el-input v-model="query.name" placeholder="搜索" clearable /></el-form-item>
      <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
    </el-form>
    <el-table :data="tableData" border stripe>
      <el-table-column prop="name" label="产品名称" />
      <el-table-column prop="spec" label="规格" width="120" />
      <el-table-column prop="purchasePrice" label="进价" width="100" />
      <el-table-column prop="sellPrice" label="售价" width="100" />
      <el-table-column prop="stock" label="库存" width="80" />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="showDialog(row)">编辑</el-button>
          <el-popconfirm title="确定删除？" @confirm="handleDelete(row.id)"><template #reference><el-button link type="danger">删除</el-button></template></el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination style="margin-top: 16px; justify-content: flex-end" v-model:current-page="query.current" v-model:page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="loadData" />

    <el-dialog v-model="dialogVisible" :title="editId ? '编辑产品' : '新增产品'" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="name"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="规格"><el-input v-model="form.spec" /></el-form-item>
        <el-form-item label="进价"><el-input-number v-model="form.purchasePrice" :min="0" :precision="2" /></el-form-item>
        <el-form-item label="售价" prop="sellPrice"><el-input-number v-model="form.sellPrice" :min="0" :precision="2" /></el-form-item>
        <el-form-item label="库存"><el-input-number v-model="form.stock" :min="0" /></el-form-item>
        <el-form-item label="预警值"><el-input-number v-model="form.warningStock" :min="0" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSubmit">确定</el-button></template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { productPage, productAdd, productUpdate, productDelete } from '../../api'

const query = reactive({ current: 1, size: 10, name: '' })
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const editId = ref(null)
const formRef = ref()
const form = reactive({ name: '', spec: '', purchasePrice: 0, sellPrice: 0, stock: 0, warningStock: 10 })
const rules = { name: [{ required: true, message: '必填', trigger: 'blur' }], sellPrice: [{ required: true, message: '必填', trigger: 'blur' }] }

async function loadData() { const res = await productPage(query); tableData.value = res.data.records; total.value = res.data.total }
function showDialog(row) { editId.value = row?.id || null; if (row) Object.assign(form, row); else Object.assign(form, { name: '', spec: '', purchasePrice: 0, sellPrice: 0, stock: 0, warningStock: 10 }); dialogVisible.value = true }
async function handleSubmit() { await formRef.value.validate(); if (editId.value) { await productUpdate({ ...form, id: editId.value }) } else { await productAdd(form) }; ElMessage.success('操作成功'); dialogVisible.value = false; loadData() }
async function handleDelete(id) { await productDelete(id); ElMessage.success('删除成功'); loadData() }

onMounted(loadData)
</script>
