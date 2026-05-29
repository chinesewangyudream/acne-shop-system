<template>
  <div class="page-container">
    <div class="page-header">
      <h3 class="page-title">产品管理</h3>
      <el-button type="primary" @click="showDialog()">
        <el-icon><Plus /></el-icon>新增产品
      </el-button>
    </div>

    <div class="search-bar">
      <el-form :inline="true" :model="query">
        <el-form-item label="名称">
          <el-input v-model="query.name" placeholder="搜索" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="query.name = ''; loadData()">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="tableData" stripe>
      <el-table-column prop="id" label="ID" width="70" align="center" />
      <el-table-column prop="name" label="产品名称" min-width="150" />
      <el-table-column prop="specification" label="规格" width="120" />
      <el-table-column prop="purchasePrice" label="进价" width="100" align="right">
        <template #default="{ row }">¥{{ row.purchasePrice }}</template>
      </el-table-column>
      <el-table-column prop="sellingPrice" label="售价" width="100" align="right">
        <template #default="{ row }">¥{{ row.sellingPrice }}</template>
      </el-table-column>
      <el-table-column prop="stock" label="库存" width="80" align="center">
        <template #default="{ row }">
          <span :style="{ color: row.stock <= row.warningStock ? '#e6a23c' : '' }">{{ row.stock }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="success" @click="showStockDialog(row, 'in')">入库</el-button>
          <el-button link type="warning" @click="showStockDialog(row, 'out')">出库</el-button>
          <el-button link type="primary" @click="showDialog(row)">编辑</el-button>
          <el-popconfirm title="确定删除？" @confirm="handleDelete(row.id)"><template #reference><el-button link type="danger">删除</el-button></template></el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-wrap">
      <el-pagination v-model:current-page="query.current" v-model:page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="loadData" />
    </div>

    <el-dialog v-model="dialogVisible" :title="editId ? '编辑产品' : '新增产品'" width="500px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="name"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="规格"><el-input v-model="form.specification" /></el-form-item>
        <el-form-item label="进价"><el-input-number v-model="form.purchasePrice" :min="0" :precision="2" style="width: 100%" /></el-form-item>
        <el-form-item label="售价" prop="sellingPrice"><el-input-number v-model="form.sellingPrice" :min="0" :precision="2" style="width: 100%" /></el-form-item>
        <el-form-item label="库存"><el-input-number v-model="form.stock" :min="0" style="width: 100%" /></el-form-item>
        <el-form-item label="预警值"><el-input-number v-model="form.warningStock" :min="0" style="width: 100%" /></el-form-item>
        <el-form-item v-if="isBoss" label="所属门店" prop="storeId">
          <el-select v-model="form.storeId" placeholder="请选择门店" style="width: 100%">
            <el-option v-for="s in storeListData" :key="s.id" :label="s.storeName" :value="s.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSubmit">确定</el-button></template>
    </el-dialog>

    <el-dialog v-model="stockDialogVisible" :title="stockType === 'in' ? '入库' : '出库'" width="400px" destroy-on-close>
      <el-form :model="stockForm" label-width="80px">
        <el-form-item label="产品">{{ stockProduct?.name }}</el-form-item>
        <el-form-item label="数量"><el-input-number v-model="stockForm.quantity" :min="1" style="width: 100%" /></el-form-item>
        <el-form-item label="原因"><el-input v-model="stockForm.reason" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="stockDialogVisible = false">取消</el-button><el-button type="primary" @click="handleStock">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { productPage, productAdd, productUpdate, productDelete, stockIn, stockOut, storePage } from '../../api'
import { useUserStore } from '../../store/user'

const userStore = useUserStore()
const isBoss = computed(() => userStore.userInfo.role === 1)
const storeListData = ref([])

async function loadStores() {
  if (!isBoss.value) return
  const res = await storePage({ current: 1, size: 100 })
  storeListData.value = res.data.records
}

const query = reactive({ current: 1, size: 10, name: '' })
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const editId = ref(null)
const formRef = ref()
const form = reactive({ name: '', specification: '', purchasePrice: 0, sellingPrice: 0, stock: 0, warningStock: 10, storeId: null })
const rules = { name: [{ required: true, message: '必填', trigger: 'blur' }], sellingPrice: [{ required: true, message: '必填', trigger: 'blur' }], storeId: [{ required: true, message: '请选择门店', trigger: 'change' }] }

const stockDialogVisible = ref(false)
const stockType = ref('in')
const stockProduct = ref(null)
const stockForm = reactive({ quantity: 1, reason: '' })

async function loadData() { const res = await productPage(query); tableData.value = res.data.records; total.value = res.data.total }
function showDialog(row) { editId.value = row?.id || null; if (row) Object.assign(form, row); else Object.assign(form, { name: '', specification: '', purchasePrice: 0, sellingPrice: 0, stock: 0, warningStock: 10, storeId: isBoss.value ? null : userStore.userInfo.storeId }); dialogVisible.value = true }
function showStockDialog(row, type) { stockProduct.value = row; stockType.value = type; Object.assign(stockForm, { quantity: 1, reason: '' }); stockDialogVisible.value = true }
async function handleSubmit() { await formRef.value.validate(); if (editId.value) { await productUpdate({ ...form, id: editId.value }) } else { await productAdd(form) }; ElMessage.success('操作成功'); dialogVisible.value = false; loadData() }
async function handleDelete(id) { await productDelete(id); ElMessage.success('删除成功'); loadData() }
async function handleStock() {
  const data = { productId: stockProduct.value.id, quantity: stockForm.quantity, reason: stockForm.reason }
  if (stockType.value === 'in') { await stockIn(data) } else { await stockOut(data) }
  ElMessage.success(stockType.value === 'in' ? '入库成功' : '出库成功'); stockDialogVisible.value = false; loadData()
}

onMounted(() => { loadData(); loadStores() })
</script>

<style scoped>
.page-container { background: #fff; border-radius: var(--radius-md); box-shadow: var(--shadow-sm); }
.page-header { display: flex; justify-content: space-between; align-items: center; padding: 18px 24px; border-bottom: 1px solid var(--c-border-light); }
.page-title { font-size: 16px; font-weight: 600; color: var(--c-text); margin: 0; }
.search-bar { padding: 16px 24px 0; }
.pagination-wrap { padding: 16px 24px; display: flex; justify-content: flex-end; }
</style>
