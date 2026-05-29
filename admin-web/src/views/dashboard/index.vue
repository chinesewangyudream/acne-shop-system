<template>
  <div class="dashboard">
    <div class="greeting">
      <h2 class="greeting-title">{{ greeting }}，{{ userStore.userInfo.name || '管理员' }}</h2>
      <p class="greeting-sub">{{ todayStr }} | {{ roleLabel }}</p>
    </div>

    <div class="stat-grid">
      <div
        class="stat-card"
        v-for="(item, i) in statCards"
        :key="item.title"
        :style="{ animationDelay: `${i * 0.08}s` }"
      >
        <div class="stat-top">
          <span class="stat-title">{{ item.title }}</span>
          <div class="stat-icon-wrap" :style="{ background: item.bgColor }">
            <el-icon :size="18" :color="item.color"><component :is="item.icon" /></el-icon>
          </div>
        </div>
        <div class="stat-value">
          <span class="stat-prefix" v-if="item.prefix">{{ item.prefix }}</span>
          {{ item.value }}
        </div>
      </div>
    </div>

    <div class="bottom-grid">
      <div class="quick-section">
        <h3 class="section-title">快捷操作</h3>
        <div class="quick-grid">
          <div
            class="quick-item"
            v-for="item in quickActions"
            :key="item.label"
            @click="$router.push(item.path)"
          >
            <div class="quick-icon" :style="{ background: item.bgColor }">
              <el-icon :size="20" :color="item.color"><component :is="item.icon" /></el-icon>
            </div>
            <span class="quick-label">{{ item.label }}</span>
          </div>
        </div>
      </div>

      <div class="tips-section">
        <h3 class="section-title">系统提示</h3>
        <div class="tip-list">
          <div class="tip-item" v-for="tip in tips" :key="tip">
            <span class="tip-dot"></span>
            <span>{{ tip }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { User, Calendar, CreditCard, Odometer, Scissor, Box, Avatar, Bell, Timer, Notebook } from '@element-plus/icons-vue'
import { useUserStore } from '../../store/user'

const userStore = useUserStore()

const roleLabel = computed(() => {
  const map = { 1: '老板', 2: '店长', 3: '前台', 4: '美容师' }
  return map[userStore.userInfo.role] || '管理员'
})

const greeting = computed(() => {
  const h = new Date().getHours()
  if (h < 12) return '早上好'
  if (h < 18) return '下午好'
  return '晚上好'
})

const todayStr = computed(() => {
  const d = new Date()
  const weekDays = ['日', '一', '二', '三', '四', '五', '六']
  return `${d.getFullYear()}年${d.getMonth() + 1}月${d.getDate()}日 星期${weekDays[d.getDay()]}`
})

const statCards = [
  { title: '今日到店', value: 0, icon: User, color: '#3a7d7b', bgColor: '#e8f4f3', prefix: '' },
  { title: '今日营收', value: 0, icon: Odometer, color: '#7bae94', bgColor: '#edf6f1', prefix: '¥' },
  { title: '今日预约', value: 0, icon: Calendar, color: '#c49030', bgColor: '#fef9ee', prefix: '' },
  { title: '待跟进', value: 0, icon: CreditCard, color: '#d4836b', bgColor: '#fdf0eb', prefix: '' },
]

const quickActions = computed(() => {
  const role = userStore.userInfo.role
  const allQuickActions = [
    { label: '预约管理', path: '/appointment', icon: Calendar, color: '#3a7d7b', bgColor: '#e8f4f3', roles: [1, 2, 3] },
    { label: '次卡管理', path: '/count-card', icon: CreditCard, color: '#7bae94', bgColor: '#edf6f1', roles: [1, 2, 3] },
    { label: '年月卡', path: '/period-card', icon: Timer, color: '#c49030', bgColor: '#fef9ee', roles: [1, 2, 3] },
    { label: '客户管理', path: '/customer', icon: User, color: '#d4836b', bgColor: '#fdf0eb', roles: [1, 2, 3] },
    { label: '疗程方案', path: '/treatment-plan', icon: Notebook, color: '#3a7d7b', bgColor: '#e8f4f3', roles: [1, 2] },
    { label: '服务项目', path: '/service-item', icon: Scissor, color: '#7bae94', bgColor: '#edf6f1', roles: [1, 2] },
    { label: '产品管理', path: '/product', icon: Box, color: '#3a7d7b', bgColor: '#e8f4f3', roles: [1, 2] },
    { label: '员工管理', path: '/employee', icon: Avatar, color: '#7bae94', bgColor: '#edf6f1', roles: [1, 2] },
  ]
  return allQuickActions.filter(a => a.roles.includes(role))
})

const tips = [
  '暂无过期卡项需要续费提醒',
  '暂无库存预警',
  '暂无待确认预约',
]
</script>

<style scoped>
.dashboard {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.greeting {
  padding: 4px 0;
}

.greeting-title {
  font-size: 22px;
  font-weight: 700;
  color: var(--c-text);
  margin: 0;
}

.greeting-sub {
  font-size: 13px;
  color: var(--c-text-secondary);
  margin: 4px 0 0;
}

.stat-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.stat-card {
  background: #fff;
  border-radius: var(--radius-md);
  padding: 20px 22px;
  box-shadow: var(--shadow-sm);
  animation: slideUp 0.4s ease both;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(12px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.stat-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
}

.stat-title {
  font-size: 13px;
  color: var(--c-text-secondary);
  font-weight: 500;
}

.stat-icon-wrap {
  width: 38px;
  height: 38px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: var(--c-text);
  line-height: 1;
}

.stat-prefix {
  font-size: 16px;
  font-weight: 500;
  color: var(--c-text-secondary);
  margin-right: 2px;
}

.bottom-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.quick-section,
.tips-section {
  background: #fff;
  border-radius: var(--radius-md);
  padding: 22px;
  box-shadow: var(--shadow-sm);
}

.section-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--c-text);
  margin: 0 0 16px;
}

.quick-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
}

.quick-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 14px 8px;
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all 0.2s ease;
}

.quick-item:hover {
  background: var(--c-border-light);
  transform: translateY(-1px);
}

.quick-icon {
  width: 42px;
  height: 42px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.quick-label {
  font-size: 12px;
  color: var(--c-text-secondary);
  font-weight: 500;
}

.tip-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.tip-item {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 13px;
  color: var(--c-text-secondary);
}

.tip-dot {
  width: 5px;
  height: 5px;
  border-radius: 50%;
  background: var(--c-sage);
  flex-shrink: 0;
}
</style>
