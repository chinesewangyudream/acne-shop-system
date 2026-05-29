<template>
  <div class="page-container">
    <div class="page-header">
      <h3 class="page-title">员工管理</h3>
      <el-button type="primary" @click="showDialog()">
        <el-icon><Plus /></el-icon>新增员工
      </el-button>
    </div>

    <div class="search-bar">
      <el-form :inline="true" :model="query">
        <el-form-item label="姓名">
          <el-input v-model="query.name" placeholder="搜索姓名" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="query.role" clearable placeholder="全部" style="width: 120px">
            <el-option label="店长" :value="2" /><el-option label="前台" :value="3" /><el-option label="美容师" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="query.name = ''; query.role = ''; loadData()">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="tableData" stripe>
      <el-table-column prop="id" label="ID" width="70" align="center" />
      <el-table-column prop="name" label="姓名" width="100" />
      <el-table-column prop="phone" label="手机号(账号)" width="130" />
      <el-table-column prop="role" label="角色" width="100" align="center">
        <template #default="{ row }">
          <el-tag size="small" effect="plain">{{ roleMap[row.role] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="skills" label="技能标签" min-width="150" />
      <el-table-column prop="status" label="状态" width="80" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small" effect="plain">{{ row.status === 1 ? '在职' : '离职' }}</el-tag>
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

    <el-dialog v-model="dialogVisible" :title="editId ? '编辑员工' : '新增员工'" width="500px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="手机号" prop="phone"><el-input v-model="form.phone" :disabled="!!editId" placeholder="手机号即为登录账号" /></el-form-item>
        <el-form-item label="密码" :prop="editId ? '' : 'passwordHash'"><el-input v-model="form.passwordHash" type="password" :placeholder="editId ? '不修改请留空' : '请输入密码'" /></el-form-item>
        <el-form-item label="姓名" prop="name"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="form.role" style="width: 100%">
            <el-option label="店长" :value="2" /><el-option label="前台" :value="3" /><el-option label="美容师" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="isBoss" label="所属门店" prop="storeId">
          <el-select v-model="form.storeId" placeholder="请选择门店" style="width: 100%">
            <el-option v-for="s in storeListData" :key="s.id" :label="s.storeName" :value="s.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="技能标签"><el-input v-model="form.skills" placeholder="逗号分隔" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSubmit">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { employeePage, employeeAdd, employeeUpdate, employeeDelete, storePage } from '../../api'
import { useUserStore } from '../../store/user'

const userStore = useUserStore()
const isBoss = computed(() => userStore.userInfo.role === 1)
const storeListData = ref([])

const roleMap = { 1: '老板', 2: '店长', 3: '前台', 4: '美容师' }

async function loadStores() {
  if (!isBoss.value) return
  const res = await storePage({ current: 1, size: 100 })
  storeListData.value = res.data.records
}

const query = reactive({ current: 1, size: 10, name: '', role: '' })
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const editId = ref(null)
const formRef = ref()
const form = reactive({ phone: '', passwordHash: '', name: '', role: 4, storeId: '', skills: '' })
const rules = { phone: [{ required: true, message: '必填', trigger: 'blur' }], passwordHash: [{ required: true, message: '必填', trigger: 'blur' }], name: [{ required: true, message: '必填', trigger: 'blur' }], role: [{ required: true, message: '必填', trigger: 'change' }] }

async function loadData() { const res = await employeePage(query); tableData.value = res.data.records; total.value = res.data.total }
function showDialog(row) { editId.value = row?.id || null; if (row) Object.assign(form, row); else Object.assign(form, { phone: '', passwordHash: '', name: '', role: 4, storeId: isBoss.value ? '' : userStore.userInfo.storeId, skills: '' }); dialogVisible.value = true }
async function handleSubmit() { await formRef.value.validate(); if (editId.value) { await employeeUpdate({ ...form, id: editId.value }) } else { await employeeAdd(form) }; ElMessage.success('操作成功'); dialogVisible.value = false; loadData() }
async function handleDelete(id) { await employeeDelete(id); ElMessage.success('删除成功'); loadData() }

onMounted(() => { loadData(); loadStores() })
</script>

<style scoped>
.page-container { background: #fff; border-radius: var(--radius-md); box-shadow: var(--shadow-sm); }
.page-header { display: flex; justify-content: space-between; align-items: center; padding: 18px 24px; border-bottom: 1px solid var(--c-border-light); }
.page-title { font-size: 16px; font-weight: 600; color: var(--c-text); margin: 0; }
.search-bar { padding: 16px 24px 0; }
.pagination-wrap { padding: 16px 24px; display: flex; justify-content: flex-end; }
</style>
