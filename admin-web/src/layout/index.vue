<template>
  <el-container style="height: 100vh">
    <el-aside width="220px" style="background-color: #304156">
      <div style="height: 60px; display: flex; align-items: center; justify-content: center">
        <span style="color: #fff; font-size: 18px; font-weight: bold">祛痘店铺管理</span>
      </div>
      <el-menu :default-active="route.path" router background-color="#304156" text-color="#bfcbd9" active-text-color="#409eff">
        <el-menu-item index="/dashboard">
          <el-icon><Odometer /></el-icon><span>工作台</span>
        </el-menu-item>
        <el-menu-item index="/customer">
          <el-icon><User /></el-icon><span>客户管理</span>
        </el-menu-item>
        <el-menu-item index="/appointment">
          <el-icon><Calendar /></el-icon><span>预约管理</span>
        </el-menu-item>
        <el-menu-item index="/member-card">
          <el-icon><CreditCard /></el-icon><span>卡项管理</span>
        </el-menu-item>
        <el-menu-item index="/service-item">
          <el-icon><Scissor /></el-icon><span>服务项目</span>
        </el-menu-item>
        <el-menu-item index="/product">
          <el-icon><Box /></el-icon><span>产品管理</span>
        </el-menu-item>
        <el-menu-item index="/employee">
          <el-icon><Avatar /></el-icon><span>员工管理</span>
        </el-menu-item>
        <el-menu-item index="/store">
          <el-icon><OfficeBuilding /></el-icon><span>门店管理</span>
        </el-menu-item>
        <el-menu-item index="/statistics">
          <el-icon><DataAnalysis /></el-icon><span>数据统计</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="display: flex; align-items: center; justify-content: space-between; border-bottom: 1px solid #eee">
        <span style="font-size: 16px">{{ route.meta.title }}</span>
        <el-dropdown @command="handleCommand">
          <span style="cursor: pointer">{{ userStore.userInfo.realName || '管理员' }} <el-icon><ArrowDown /></el-icon></span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
      <el-main style="background-color: #f0f2f5; padding: 20px">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

function handleCommand(cmd) {
  if (cmd === 'logout') {
    userStore.logout()
    router.push('/login')
  }
}
</script>
