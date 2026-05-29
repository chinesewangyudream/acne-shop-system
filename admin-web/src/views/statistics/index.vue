<template>
  <div class="page-container">
    <div class="page-header">
      <h3 class="page-title">数据统计</h3>
    </div>

    <div class="tabs-wrap">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="营业概览" name="overview">
          <div class="stat-grid">
            <div class="stat-card" v-for="item in overviewStats" :key="item.title">
              <span class="stat-title">{{ item.title }}</span>
              <span class="stat-value">{{ item.prefix }}{{ item.value }}</span>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane v-if="isBoss" label="门店对比" name="compare">
          <el-empty description="门店对比数据（待对接）" />
        </el-tab-pane>
        <el-tab-pane label="员工绩效" name="performance">
          <el-empty description="员工绩效数据（待对接）" />
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useUserStore } from '../../store/user'

const userStore = useUserStore()
const isBoss = computed(() => userStore.userInfo.role === 1)

const activeTab = ref('overview')

const overviewStats = [
  { title: '本月营收', value: 0, prefix: '¥' },
  { title: '本月到店人次', value: 0, prefix: '' },
  { title: '平均客单价', value: 0, prefix: '¥' },
  { title: '新增客户', value: 0, prefix: '' },
]
</script>

<style scoped>
.page-container { background: #fff; border-radius: var(--radius-md); box-shadow: var(--shadow-sm); }
.page-header { display: flex; justify-content: space-between; align-items: center; padding: 18px 24px; border-bottom: 1px solid var(--c-border-light); }
.page-title { font-size: 16px; font-weight: 600; color: var(--c-text); margin: 0; }
.tabs-wrap { padding: 0 24px 24px; }
.stat-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; padding-top: 8px; }
.stat-card { background: var(--c-border-light); border-radius: var(--radius-sm); padding: 22px; display: flex; flex-direction: column; gap: 10px; }
.stat-card .stat-title { font-size: 13px; color: var(--c-text-secondary); font-weight: 500; }
.stat-card .stat-value { font-size: 30px; font-weight: 700; color: var(--c-text); }
</style>
