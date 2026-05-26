<template>
  <el-card>
    <template #header>
      <div style="display: flex; justify-content: space-between; align-items: center">
        <span>员工管理</span>
        <el-button type="primary" @click="showDialog()">新增员工</el-button>
      </div>
    </template>
    <el-form :inline="true" :model="query" style="margin-bottom: 16px">
      <el-form-item label="角色"><el-select v-model="query.role" clearable><el-option label="店长" value="STORE_MANAGER" /><el-option label="前台" value="RECEPTIONIST" /><el-option label="美容师" value="BEAUTICIAN" /></el-select></el-form-item>
      <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
    </el-form>
    <el-table :data="tableData" border stripe>
      <el-table-column prop="username" label="账号" width="120" />
      <el-table-column prop="realName" label="姓名" width="100" />
      <el-table-column prop="phone" label="手机号" width="130" />
      <el-table-column prop="role" label="角色" width="100">
        <template #default="{ row }">{{ { ADMIN: '管理员', STORE_MANAGER: '店长', RECEPTIONIST: '前台', BEAUTICIAN: '美容师' }[row.role] }}</template>
      </el-table-column>
      <el-table-column prop="skillTags" label="技能标签" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }"><el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '启用' : '停用' }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="showDialog(row)">编辑</el-button>
          <el-popconfirm title="确定删除？" @confirm="handleDelete(row.id)"><template #reference><el-button link type="danger">删除</el-button></template></el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination style="margin-top: 16px; justify-content: flex-end" v-model:current-page="query.current" v-model:page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="loadData" />

    <el-dialog v-model="dialogVisible" :title="editId ? '编辑员工' : '新增员工'" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="账号" prop="username"><el-input v-model="form.username" :disabled="!!editId" /></el-form-item>
        <el-form-item label="密码" :prop="editId ? '' : 'password'"><el-input v-model="form.password" type="password" :placeholder="editId ? '不修改请留空' : '请输入密码'" /></el-form-item>
        <el-form-item label="姓名" prop="realName"><el-input v-model="form.realName" /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="角色" prop="role"><el-select v-model="form.role"><el-option label="店长" value="STORE_MANAGER" /><el-option label="前台" value="RECEPTIONIST" /><el-option label="美容师" value="BEAUTICIAN" /></el-select></el-form-item>
        <el-form-item label="门店ID"><el-input v-model.number="form.storeId" /></el-form-item>
        <el-form-item label="技能标签"><el-input v-model="form.skillTags" placeholder="逗号分隔" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSubmit">确定</el-button></template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { userPage, userAdd, userUpdate, userDelete } from '../../api'

const query = reactive({ current: 1, size: 10, role: '' })
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const editId = ref(null)
const formRef = ref()
const form = reactive({ username: '', password: '', realName: '', phone: '', role: 'BEAUTICIAN', storeId: '', skillTags: '' })
const rules = { username: [{ required: true, message: '必填', trigger: 'blur' }], password: [{ required: true, message: '必填', trigger: 'blur' }], realName: [{ required: true, message: '必填', trigger: 'blur' }], role: [{ required: true, message: '必填', trigger: 'change' }] }

async function loadData() { const res = await userPage(query); tableData.value = res.data.records; total.value = res.data.total }
function showDialog(row) { editId.value = row?.id || null; if (row) Object.assign(form, row); else Object.assign(form, { username: '', password: '', realName: '', phone: '', role: 'BEAUTICIAN', storeId: '', skillTags: '' }); dialogVisible.value = true }
async function handleSubmit() { await formRef.value.validate(); if (editId.value) { await userUpdate({ ...form, id: editId.value }) } else { await userAdd(form) }; ElMessage.success('操作成功'); dialogVisible.value = false; loadData() }
async function handleDelete(id) { await userDelete(id); ElMessage.success('删除成功'); loadData() }

onMounted(loadData)
</script>
