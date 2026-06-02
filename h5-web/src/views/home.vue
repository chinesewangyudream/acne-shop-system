<template>
  <div class="home-page">
    <div class="home-header">
      <div class="header-content">
        <h2 class="greeting">{{ greeting }}</h2>
        <p class="sub" v-if="currentStoreName">{{ currentStoreName }}</p>
        <p class="sub" v-else>祛痘护理中心</p>
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
      <div class="service-card" @click="handleAppointment">
        <div class="service-icon" style="background: #edf6f1">
          <van-icon name="clock-o" size="22" color="#7bae94" />
        </div>
        <span class="service-label">在线预约</span>
      </div>
      <div class="service-card" @click="showStorePicker = true">
        <div class="service-icon" style="background: #fef9ee">
          <van-icon name="shop-o" size="22" color="#c49030" />
        </div>
        <span class="service-label">选择门店</span>
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

    <!-- 门店选择弹窗 -->
    <van-popup v-model:show="showStorePicker" position="bottom" round>
      <div class="store-picker-header">
        <span class="store-picker-title">选择门店</span>
      </div>
      <div class="store-list" v-if="stores.length > 0">
        <div
          class="store-item"
          v-for="store in stores"
          :key="store.id"
          :class="{ active: currentStoreId && currentStoreId == store.id }"
          @click="handleSelectStore(store)"
        >
          <div class="store-info">
            <span class="store-name">{{ store.storeName }}</span>
            <span class="store-addr">{{ store.address }}</span>
          </div>
          <van-icon v-if="currentStoreId && currentStoreId == store.id" name="success" color="var(--h5-teal)" size="18" />
        </div>
      </div>
      <van-empty v-else description="暂无营业门店" image="search" />
    </van-popup>
  </div>

  <van-tabbar v-model="active" route>
    <van-tabbar-item icon="home-o" to="/home">首页</van-tabbar-item>
    <van-tabbar-item icon="card" to="/cards">卡项</van-tabbar-item>
    <van-tabbar-item icon="clock-o" to="/appointment">预约</van-tabbar-item>
  </van-tabbar>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast, showDialog } from 'vant'
import { getMyAppointments, getStoreList, bindStore } from '../api'

const router = useRouter()
const active = ref(0)
const appointments = ref([])
const statusLabel = { 1: '待确认', 2: '已确认', 3: '已完成', 4: '已取消', 5: '已改期' }
const statusType = { 1: 'warning', 2: 'primary', 3: 'success', 4: 'danger', 5: 'primary' }

const stores = ref([])
const showStorePicker = ref(false)
const currentStoreId = ref(localStorage.getItem('h5_store_id') || '')
const currentStoreName = ref('')

const greeting = computed(() => {
  const h = new Date().getHours()
  if (h < 12) return '早上好'
  if (h < 18) return '下午好'
  return '晚上好'
})

// 加载预约数据
async function loadAppointments() {
  try {
    const customerId = localStorage.getItem('h5_customer_id')
    const storeId = localStorage.getItem('h5_store_id')
    if (!customerId || !storeId) return
    const res = await getMyAppointments({ customerId, storeId })
    appointments.value = res.data.slice(0, 5)
  } catch (e) { /* 未登录时忽略 */ }
}

// 选择门店
async function handleSelectStore(store) {
  try {
    await bindStore(store.id)
    currentStoreId.value = store.id
    currentStoreName.value = store.storeName
    localStorage.setItem('h5_store_id', store.id)
    localStorage.setItem('h5_store_name', store.storeName)
    showStorePicker.value = false
    showToast('已选择门店')
    // 重新加载预约数据
    await loadAppointments()
  } catch (e) {
    showToast('绑定门店失败')
  }
}

// 点击预约时，若未选门店则提示
function handleAppointment() {
  if (!currentStoreId.value) {
    showStorePicker.value = true
    return
  }
  router.push('/appointment')
}

// 初始化
onMounted(async () => {
  // 加载门店列表
  try {
    const res = await getStoreList()
    stores.value = res.data || []
    // 回显当前门店名称
    if (currentStoreId.value) {
      const cur = stores.value.find(s => s.id == currentStoreId.value)
      if (cur) {
        currentStoreName.value = cur.storeName
      }
    }
  } catch (e) { /* */ }

  // 加载预约
  await loadAppointments()

  // 若未选择门店，弹窗提醒
  if (!currentStoreId.value) {
    showDialog({
      title: '欢迎使用',
      message: '请先选择您常去的门店，以便为您提供更好的服务',
      confirmButtonText: '去选择',
    }).then(() => {
      showStorePicker.value = true
    })
  }
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

/* 门店选择弹窗 */
.store-picker-header {
  padding: 16px 20px;
  text-align: center;
  border-bottom: 1px solid #f2f0ec;
}

.store-picker-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--h5-text);
}

.store-list {
  padding: 8px 0;
  max-height: 50vh;
  overflow-y: auto;
}

.store-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 20px;
  cursor: pointer;
  transition: background 0.15s;
}

.store-item:active {
  background: #f7f6f4;
}

.store-item.active {
  background: #e8f4f3;
}

.store-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
  flex: 1;
}

.store-name {
  font-size: 15px;
  font-weight: 500;
  color: var(--h5-text);
}

.store-addr {
  font-size: 12px;
  color: var(--h5-text-secondary);
}
</style>
