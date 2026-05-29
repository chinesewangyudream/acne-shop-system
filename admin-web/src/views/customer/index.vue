<template>
  <div class="page-container">
    <div class="page-header">
      <h3 class="page-title">客户管理</h3>
      <el-button type="primary" @click="showDialog()">
        <el-icon><Plus /></el-icon>新增客户
      </el-button>
    </div>

    <div class="search-bar">
      <el-form :inline="true" :model="query">
        <el-form-item label="姓名">
          <el-input v-model="query.name" placeholder="搜索姓名" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="query.phone" placeholder="搜索手机号" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="query.name = ''; query.phone = ''; loadData()">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="tableData" stripe>
      <el-table-column prop="id" label="ID" width="70" align="center" />
      <el-table-column prop="name" label="姓名" width="100" />
      <el-table-column prop="gender" label="性别" width="70" align="center">
        <template #default="{ row }">{{ row.gender === 1 ? '男' : '女' }}</template>
      </el-table-column>
      <el-table-column prop="age" label="年龄" width="70" align="center" />
      <el-table-column prop="phone" label="手机号" width="130" />
      <el-table-column prop="acneType" label="痘痘类型" width="120" />
      <el-table-column prop="severity" label="严重程度" width="100" align="center">
        <template #default="{ row }">{{ { 1: '轻度', 2: '中度', 3: '重度' }[row.severity] || '-' }}</template>
      </el-table-column>
      <el-table-column prop="lastVisitAt" label="最后到店" width="170" />
      <el-table-column prop="isBlacklisted" label="黑名单" width="80" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.isBlacklisted === 1" type="danger" size="small" effect="plain">是</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="primary" @click="showDialog(row)">编辑</el-button>
          <el-button v-if="row.isBlacklisted !== 1" link type="warning" @click="handleBlacklist(row.id)">拉黑</el-button>
          <el-button v-else link type="success" @click="handleUnblacklist(row.id)">解除</el-button>
          <el-popconfirm title="确定删除该客户？" @confirm="handleDelete(row.id)">
            <template #reference><el-button link type="danger">删除</el-button></template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-wrap">
      <el-pagination v-model:current-page="query.current" v-model:page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="loadData" />
    </div>

    <el-dialog v-model="dialogVisible" :title="editId ? '编辑客户' : '新增客户'" width="500px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="姓名" prop="name"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="form.gender"><el-radio :value="0">女</el-radio><el-radio :value="1">男</el-radio></el-radio-group>
        </el-form-item>
        <el-form-item label="年龄"><el-input-number v-model="form.age" :min="0" style="width: 100%" /></el-form-item>
        <el-form-item label="手机号" prop="phone"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="过敏史"><el-input v-model="form.allergyHistory" type="textarea" /></el-form-item>
        <el-form-item label="痘痘类型"><el-input v-model="form.acneType" /></el-form-item>
        <el-form-item label="严重程度">
          <el-select v-model="form.severity" clearable style="width: 100%">
            <el-option label="轻度" :value="1" /><el-option label="中度" :value="2" /><el-option label="重度" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="来源渠道"><el-input v-model="form.sourceChannel" /></el-form-item>
        <el-form-item label="标签"><el-input v-model="form.tags" placeholder="多个标签逗号分隔" /></el-form-item>
        <el-form-item v-if="isBoss" label="所属门店" prop="storeId">
          <el-select v-model="form.storeId" placeholder="请选择门店" style="width: 100%">
            <el-option v-for="s in storeListData" :key="s.id" :label="s.storeName" :value="s.id" />
          </el-select>
        </el-form-item>
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
import { customerPage, customerAdd, customerUpdate, customerDelete, customerBlacklist, customerUnblacklist, storePage } from '../../api'
import { useUserStore } from '../../store/user'

const userStore = useUserStore()
const isBoss = computed(() => userStore.userInfo.role === 1)
const storeListData = ref([])

async function loadStores() {
  if (!isBoss.value) return
  const res = await storePage({ current: 1, size: 100 })
  storeListData.value = res.data.records
}

const query = reactive({ current: 1, size: 10, name: '', phone: '' })
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const editId = ref(null)
const formRef = ref()
const form = reactive({ name: '', gender: 0, age: null, phone: '', allergyHistory: '', acneType: '', severity: null, sourceChannel: '', tags: '', storeId: '' })
const rules = { name: [{ required: true, message: '请输入姓名', trigger: 'blur' }], phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }], storeId: [{ required: true, message: '请选择门店', trigger: 'change' }] }

async function loadData() { const res = await customerPage(query); tableData.value = res.data.records; total.value = res.data.total }
function showDialog(row) {
  editId.value = row?.id || null
  if (row) Object.assign(form, row)
  else Object.assign(form, { name: '', gender: 0, age: null, phone: '', allergyHistory: '', acneType: '', severity: null, sourceChannel: '', tags: '', storeId: isBoss.value ? '' : userStore.userInfo.storeId })
  dialogVisible.value = true
}
async function handleSubmit() {
  await formRef.value.validate()
  if (editId.value) { await customerUpdate({ ...form, id: editId.value }) }
  else { await customerAdd(form) }
  ElMessage.success('操作成功'); dialogVisible.value = false; loadData()
}
async function handleDelete(id) { await customerDelete(id); ElMessage.success('删除成功'); loadData() }
async function handleBlacklist(id) {
  const { value } = await ElMessageBox.prompt('请输入拉黑原因', '拉黑客户', { confirmButtonText: '确定', cancelButtonText: '取消' })
  await customerBlacklist(id, value); ElMessage.success('已加入黑名单'); loadData()
}
async function handleUnblacklist(id) { await customerUnblacklist(id); ElMessage.success('已移出黑名单'); loadData() }

onMounted(() => { loadData(); loadStores() })
</script>

<style scoped>
.page-container { background: #fff; border-radius: var(--radius-md); box-shadow: var(--shadow-sm); }
.page-header { display: flex; justify-content: space-between; align-items: center; padding: 18px 24px; border-bottom: 1px solid var(--c-border-light); }
.page-title { font-size: 16px; font-weight: 600; color: var(--c-text); margin: 0; }
.search-bar { padding: 16px 24px 0; }
.pagination-wrap { padding: 16px 24px; display: flex; justify-content: flex-end; }
</style>
