<template>
  <div class="home-page">
    <div class="home-header">
      <div class="header-content">
        <h2 class="greeting">{{ greeting }}</h2>
        <p class="sub">祛痘护理中心</p>
      </div>
      <div class="header-mark">痘</div>
    </div>

    <div class="service-grid">
      <div class="service-card" @click="$router.push('/cards')">
        <div class="service-icon" style="background: #e8f4f3">
          <van-icon name="card" size="22" color="#3a7d7b" />
        </div>
        <span class="service-label">我的卡项</span>
      </div>
      <div class="service-card" @click="$router.push('/appointment')">
        <div class="service-icon" style="background: #edf6f1">
          <van-icon name="clock-o" size="22" color="#7bae94" />
        </div>
        <span class="service-label">在线预约</span>
      </div>
      <div class="service-card">
        <div class="service-icon" style="background: #fef9ee">
          <van-icon name="phone-o" size="22" color="#c49030" />
        </div>
        <span class="service-label">联系门店</span>
      </div>
    </div>

    <div class="section">
      <div class="section-header">
        <span class="section-title">最近预约</span>
      </div>
      <div class="appointment-list" v-if="appointments.length > 0">
        <div class="appointment-item" v-for="item in appointments" :key="item.id">
          <div class="appt-left">
            <span class="appt-date">{{ item.appointmentDate }}</span>
            <span class="appt-time">{{ item.startTime }}</span>
          </div>
          <div class="appt-right">
            <van-tag :type="statusType[item.status]" plain size="medium">{{ statusLabel[item.status] }}</van-tag>
          </div>
        </div>
      </div>
      <van-empty v-else description="暂无预约记录" image="search" />
    </div>
  </div>

  <van-tabbar v-model="active" route>
    <van-tabbar-item icon="home-o" to="/home">首页</van-tabbar-item>
    <van-tabbar-item icon="card" to="/cards">卡项</van-tabbar-item>
    <van-tabbar-item icon="clock-o" to="/appointment">预约</van-tabbar-item>
  </van-tabbar>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getMyAppointments } from '../api'

const active = ref(0)
const appointments = ref([])
const statusLabel = { PENDING: '待确认', CONFIRMED: '已确认', CANCELLED: '已取消', COMPLETED: '已完成' }
const statusType = { PENDING: 'warning', CONFIRMED: 'primary', CANCELLED: 'danger', COMPLETED: 'success' }

const greeting = computed(() => {
  const h = new Date().getHours()
  if (h < 12) return '早上好'
  if (h < 18) return '下午好'
  return '晚上好'
})

onMounted(async () => {
  try {
    const phone = localStorage.getItem('h5_phone')
    const storeId = localStorage.getItem('h5_store_id')
    const res = await getMyAppointments({ customerId: phone, storeId })
    appointments.value = res.data.slice(0, 5)
  } catch (e) { /* 未登录时忽略 */ }
})
</script>

<style scoped>
.home-page {
  padding-bottom: 70px;
  min-height: 100vh;
  background: var(--h5-bg);
}

.home-header {
  background: linear-gradient(135deg, var(--h5-teal-dark) 0%, var(--h5-teal) 100%);
  padding: 40px 20px 36px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-radius: 0 0 24px 24px;
}

.header-content {
  flex: 1;
}

.greeting {
  color: #fff;
  font-size: 22px;
  font-weight: 700;
  margin: 0;
}

.sub {
  color: rgba(255, 255, 255, 0.5);
  font-size: 13px;
  margin: 4px 0 0;
}

.header-mark {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.15);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 18px;
}

.service-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
  padding: 20px 16px;
  margin-top: -16px;
}

.service-card {
  background: #fff;
  border-radius: 14px;
  padding: 18px 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  box-shadow: 0 2px 8px rgba(58, 125, 123, 0.05);
  cursor: pointer;
  transition: transform 0.2s;
}

.service-card:active {
  transform: scale(0.96);
}

.service-icon {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.service-label {
  font-size: 13px;
  color: var(--h5-text);
  font-weight: 500;
}

.section {
  padding: 0 16px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.section-title {
  font-size: 15px;
  font-weight: 600;
  color: var(--h5-text);
}

.appointment-list {
  background: #fff;
  border-radius: 14px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(58, 125, 123, 0.05);
}

.appointment-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 14px 16px;
  border-bottom: 1px solid #f2f0ec;
}

.appointment-item:last-child {
  border-bottom: none;
}

.appt-left {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.appt-date {
  font-size: 14px;
  font-weight: 500;
  color: var(--h5-text);
}

.appt-time {
  font-size: 12px;
  color: var(--h5-text-secondary);
}
</style>
