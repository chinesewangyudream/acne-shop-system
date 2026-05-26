<template>
  <div style="padding-bottom: 60px">
    <van-nav-bar title="祛痘护理中心" />

    <van-cell-group inset style="margin-top: 16px" title="我的服务">
      <van-cell title="我的卡项" is-link to="/cards" icon="card" />
      <van-cell title="在线预约" is-link to="/appointment" icon="clock" />
    </van-cell-group>

    <van-cell-group inset style="margin-top: 16px" title="最近预约">
      <van-empty v-if="appointments.length === 0" description="暂无预约记录" />
      <van-cell v-for="item in appointments" :key="item.id"
        :title="item.appointmentDate + ' ' + item.startTime"
        :value="statusLabel[item.status]"
        :label="'备注: ' + (item.remark || '无')" />
    </van-cell-group>
  </div>

  <van-tabbar v-model="active" route>
    <van-tabbar-item icon="home-o" to="/home">首页</van-tabbar-item>
    <van-tabbar-item icon="card" to="/cards">卡项</van-tabbar-item>
    <van-tabbar-item icon="clock-o" to="/appointment">预约</van-tabbar-item>
  </van-tabbar>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyAppointments } from '../api'

const active = ref(0)
const appointments = ref([])
const statusLabel = { PENDING: '待确认', CONFIRMED: '已确认', CANCELLED: '已取消', COMPLETED: '已完成' }

onMounted(async () => {
  try {
    const phone = localStorage.getItem('h5_phone')
    const storeId = localStorage.getItem('h5_store_id')
    const res = await getMyAppointments({ customerId: phone, storeId })
    appointments.value = res.data.slice(0, 5)
  } catch (e) { /* 未登录时忽略 */ }
})
</script>
