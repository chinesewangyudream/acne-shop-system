<template>
  <div class="page-container">
    <div class="page-header">
      <h3 class="page-title">服务项目</h3>
      <el-button type="primary" @click="showDialog()">
        <el-icon><Plus /></el-icon>新增项目
      </el-button>
    </div>

    <el-table :data="tableData" stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="70" align="center" />
      <el-table-column prop="name" label="项目名称" min-width="150" />
      <el-table-column prop="price" label="价格" width="100" align="right">
        <template #default="{ row }">¥{{ row.price }}</template>
      </el-table-column>
      <el-table-column prop="duration" label="时长" width="100" align="center">
        <template #default="{ row }">{{ row.duration }}分钟</template>
      </el-table-column>
      <el-table-column prop="description" label="描述" min-width="200" />
      <el-table-column prop="status" label="状态" width="80" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small" effect="plain">{{ row.status === 1 ? '启用' : '停用' }}</el-tag>
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

    <el-dialog v-model="dialogVisible" :title="editId ? '编辑项目' : '新增项目'" width="500px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="name"><el-input v-model="form.name" /></el-form-item>
        <el-form-item v-if="isBoss" label="所属门店" prop="storeId">
          <el-select v-model="form.storeId" placeholder="请选择门店" style="width: 100%">
            <el-option v-for="s in storeListData" :key="s.id" :label="s.storeName" :value="s.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格" prop="price"><el-input-number v-model="form.price" :min="0" :precision="2" style="width: 100%" /></el-form-item>
        <el-form-item label="时长(分)" prop="duration"><el-input-number v-model="form.duration" :min="1" style="width: 100%" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" /></el-form-item>
        <el-form-item label="状态"><el-switch v-model="form.status" :active-value="1" :inactive-value="0" active-text="启用" inactive-text="停用" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSubmit">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { serviceItemPage, serviceItemAdd, serviceItemUpdate, serviceItemDelete, storePage } from '../../api'
import { useUserStore } from '../../store/user'

const userStore = useUserStore()
const isBoss = computed(() => userStore.userInfo.role === 1)
const storeListData = ref([])

async function loadStores() {
  if (!isBoss.value) return
  const res = await storePage({ current: 1, size: 100 })
  storeListData.value = res.data.records
}

const loading = ref(false)
const query = reactive({ current: 1, size: 10, storeId: null })
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const editId = ref(null)
const formRef = ref()
const form = reactive({ name: '', price: 0, duration: 30, description: '', status: 1, storeId: null })
const rules = { name: [{ required: true, message: '必填', trigger: 'blur' }], storeId: [{ required: true, message: '请选择门店', trigger: 'change' }] }

async function loadData() { loading.value = true; try { const res = await serviceItemPage(query); tableData.value = res.data.records; total.value = res.data.total } finally { loading.value = false } }
function showDialog(row) { editId.value = row?.id || null; if (row) Object.assign(form, row); else Object.assign(form, { name: '', price: 0, duration: 30, description: '', status: 1, storeId: isBoss.value ? null : userStore.userInfo.storeId }); dialogVisible.value = true }
async function handleSubmit() { await formRef.value.validate(); if (editId.value) { await serviceItemUpdate({ ...form, id: editId.value }) } else { await serviceItemAdd(form) }; ElMessage.success('操作成功'); dialogVisible.value = false; loadData() }
async function handleDelete(id) { await serviceItemDelete(id); ElMessage.success('删除成功'); loadData() }

onMounted(() => { loadData(); loadStores() })
</script>

<style scoped>
.page-container { background: #fff; border-radius: var(--radius-md); box-shadow: var(--shadow-sm); }
.page-header { display: flex; justify-content: space-between; align-items: center; padding: 18px 24px; border-bottom: 1px solid var(--c-border-light); }
.page-title { font-size: 16px; font-weight: 600; color: var(--c-text); margin: 0; }
.pagination-wrap { padding: 16px 24px; display: flex; justify-content: flex-end; }
</style>
