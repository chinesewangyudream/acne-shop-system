<template>
  <el-card>
    <template #header>
      <div style="display: flex; justify-content: space-between; align-items: center">
        <span>客户管理</span>
        <el-button type="primary" @click="showDialog()">新增客户</el-button>
      </div>
    </template>
    <el-form :inline="true" :model="query" style="margin-bottom: 16px">
      <el-form-item label="姓名"><el-input v-model="query.name" placeholder="搜索姓名" clearable /></el-form-item>
      <el-form-item label="手机号"><el-input v-model="query.phone" placeholder="搜索手机号" clearable /></el-form-item>
      <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
    </el-form>
    <el-table :data="tableData" border stripe>
      <el-table-column prop="name" label="姓名" width="100" />
      <el-table-column prop="gender" label="性别" width="80" />
      <el-table-column prop="phone" label="手机号" width="130" />
      <el-table-column prop="acneType" label="痘痘类型" width="120" />
      <el-table-column prop="skinType" label="肤质" width="100" />
      <el-table-column prop="tags" label="标签" />
      <el-table-column prop="lastVisitTime" label="最后到店" width="170" />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="showDialog(row)">编辑</el-button>
          <el-popconfirm title="确定删除？" @confirm="handleDelete(row.id)">
            <template #reference><el-button link type="danger">删除</el-button></template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination style="margin-top: 16px; justify-content: flex-end" v-model:current-page="query.current" v-model:page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="loadData" />

    <el-dialog v-model="dialogVisible" :title="editId ? '编辑客户' : '新增客户'" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="姓名" prop="name"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="性别"><el-radio-group v-model="form.gender"><el-radio value="男">男</el-radio><el-radio value="女">女</el-radio></el-radio-group></el-form-item>
        <el-form-item label="手机号" prop="phone"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="生日"><el-date-picker v-model="form.birthday" type="date" /></el-form-item>
        <el-form-item label="过敏史"><el-input v-model="form.allergyHistory" type="textarea" /></el-form-item>
        <el-form-item label="痘痘类型"><el-input v-model="form.acneType" /></el-form-item>
        <el-form-item label="肤质"><el-input v-model="form.skinType" /></el-form-item>
        <el-form-item label="标签"><el-input v-model="form.tags" placeholder="多个标签逗号分隔" /></el-form-item>
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
import { customerPage, customerAdd, customerUpdate, customerDelete } from '../../api'

const query = reactive({ current: 1, size: 10, name: '', phone: '' })
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const editId = ref(null)
const formRef = ref()
const form = reactive({ name: '', gender: '女', phone: '', birthday: '', allergyHistory: '', acneType: '', skinType: '', tags: '' })
const rules = { name: [{ required: true, message: '请输入姓名', trigger: 'blur' }], phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }] }

async function loadData() {
  const res = await customerPage(query)
  tableData.value = res.data.records
  total.value = res.data.total
}

function showDialog(row) {
  editId.value = row?.id || null
  if (row) Object.assign(form, row)
  else Object.assign(form, { name: '', gender: '女', phone: '', birthday: '', allergyHistory: '', acneType: '', skinType: '', tags: '' })
  dialogVisible.value = true
}

async function handleSubmit() {
  await formRef.value.validate()
  if (editId.value) { await customerUpdate({ ...form, id: editId.value }) }
  else { await customerAdd(form) }
  ElMessage.success('操作成功')
  dialogVisible.value = false
  loadData()
}

async function handleDelete(id) {
  await customerDelete(id)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(loadData)
</script>
