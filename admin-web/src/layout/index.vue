<template>
  <el-container style="height: 100vh">
    <el-aside :width="isCollapse ? '64px' : sidebarWidth" class="sidebar">
      <div class="sidebar-logo" @click="isCollapse = !isCollapse">
        <div class="logo-mark">痘</div>
        <transition name="fade">
          <span v-show="!isCollapse" class="logo-text">店铺管理</span>
        </transition>
      </div>
      <el-menu
        :default-active="route.path"
        :collapse="isCollapse"
        router
        class="sidebar-menu"
      >
        <el-menu-item v-for="item in menuItems" :key="item.path" :index="item.path">
          <el-icon><component :is="item.icon" /></el-icon>
          <span>{{ item.label }}</span>
        </el-menu-item>
      </el-menu>
      <div class="sidebar-footer" v-show="!isCollapse">
        <span class="version">v1.0</span>
      </div>
    </el-aside>
    <el-container class="main-container">
      <el-header class="header">
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item>{{ route.meta.title }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand" trigger="click">
            <div class="user-info">
              <el-avatar :size="32" class="user-avatar">
                {{ (userStore.userInfo.name || '管')[0] }}
              </el-avatar>
              <div class="user-detail" v-show="true">
                <span class="user-name">{{ userStore.userInfo.name || '管理员' }}</span>
                <span class="user-role">{{ roleLabel }}</span>
              </div>
              <el-icon class="arrow-icon"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">
                  <el-icon><SwitchButton /></el-icon>退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main class="main-content">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../store/user'
import { Odometer, User, Calendar, CreditCard, Scissor, Box, Avatar, OfficeBuilding, DataAnalysis, Timer, Notebook } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const isCollapse = ref(false)
const sidebarWidth = '240px'

const allMenuItems = [
  { path: '/dashboard', label: '工作台', icon: Odometer, roles: [1, 2] },
  { path: '/customer', label: '客户管理', icon: User, roles: [1, 2, 3] },
  { path: '/appointment', label: '预约管理', icon: Calendar, roles: [1, 2, 3] },
  { path: '/count-card', label: '次卡管理', icon: CreditCard, roles: [1, 2, 3] },
  { path: '/period-card', label: '年月卡管理', icon: Timer, roles: [1, 2, 3] },
  { path: '/treatment-plan', label: '疗程方案', icon: Notebook, roles: [1, 2] },
  { path: '/service-item', label: '服务项目', icon: Scissor, roles: [1, 2] },
  { path: '/product', label: '产品管理', icon: Box, roles: [1, 2] },
  { path: '/employee', label: '员工管理', icon: Avatar, roles: [1, 2] },
  { path: '/store', label: '门店管理', icon: OfficeBuilding, roles: [1] },
  { path: '/statistics', label: '数据统计', icon: DataAnalysis, roles: [1, 2] },
]

const menuItems = computed(() => {
  const role = userStore.userInfo.role
  return allMenuItems.filter(item => item.roles.includes(role))
})

const roleLabel = computed(() => {
  const map = { 1: '老板', 2: '店长', 3: '前台', 4: '美容师' }
  return map[userStore.userInfo.role] || '管理员'
})

function handleCommand(cmd) {
  if (cmd === 'logout') {
    userStore.logout()
    router.push('/login')
  }
}
</script>

<style scoped>
.sidebar {
  background: var(--c-bg-sidebar);
  transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  border-right: none;
}

.sidebar-logo {
  height: var(--header-h);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  cursor: pointer;
  padding: 0 16px;
  flex-shrink: 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.06);
}

.logo-mark {
  width: 34px;
  height: 34px;
  border-radius: 10px;
  background: var(--c-teal);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 15px;
  flex-shrink: 0;
}

.logo-text {
  font-size: 15px;
  font-weight: 600;
  color: #e0e8e8;
  letter-spacing: 2px;
  white-space: nowrap;
}

.sidebar-menu {
  flex: 1;
  overflow-y: auto;
  padding: 12px 0;
  --el-menu-bg-color: transparent;
  --el-menu-text-color: var(--c-text-sidebar);
  --el-menu-active-color: var(--c-text-sidebar-active);
  --el-menu-hover-bg-color: var(--c-bg-sidebar-hover);
}

.sidebar-menu :deep(.el-menu-item) {
  height: 42px !important;
  line-height: 42px !important;
  margin: 2px 10px !important;
  border-radius: 8px !important;
  font-size: 13.5px !important;
  transition: all 0.2s ease !important;
}

.sidebar-menu :deep(.el-menu-item:hover) {
  background: var(--c-bg-sidebar-hover) !important;
  color: #c0d4d4 !important;
}

.sidebar-menu :deep(.el-menu-item.is-active) {
  background: var(--c-teal) !important;
  color: #fff !important;
  font-weight: 500;
  box-shadow: 0 2px 8px rgba(58, 125, 123, 0.3);
}

.sidebar-menu :deep(.el-menu-item .el-icon) {
  font-size: 17px !important;
  margin-right: 10px !important;
}

.sidebar-footer {
  padding: 12px 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.06);
}

.version {
  font-size: 11px;
  color: rgba(155, 176, 176, 0.5);
}

.main-container {
  background: var(--c-bg);
}

.header {
  height: var(--header-h);
  background: var(--c-bg-card);
  border-bottom: 1px solid var(--c-border);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 28px;
  box-shadow: var(--shadow-xs);
  z-index: 1;
}

.header-left {
  display: flex;
  align-items: center;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 6px 10px;
  border-radius: var(--radius-sm);
  transition: background 0.2s;
}

.user-info:hover {
  background: var(--c-border-light);
}

.user-avatar {
  background: var(--c-teal);
  color: #fff;
  font-size: 14px;
  font-weight: 600;
}

.user-detail {
  display: flex;
  flex-direction: column;
}

.user-name {
  font-size: 13px;
  color: var(--c-text);
  font-weight: 600;
  line-height: 1.2;
}

.user-role {
  font-size: 11px;
  color: var(--c-text-secondary);
  line-height: 1.2;
}

.arrow-icon {
  color: var(--c-text-secondary);
  font-size: 12px;
}

.main-content {
  padding: 24px;
  overflow-y: auto;
}
</style>
