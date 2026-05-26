<template>
  <div style="padding-bottom: 60px">
    <van-nav-bar title="我的卡项" left-arrow @click-left="$router.back()" />

    <van-empty v-if="cards.length === 0" description="暂无卡项" />

    <van-cell-group v-for="card in cards" :key="card.id" inset style="margin: 12px 0">
      <div style="padding: 16px; background: linear-gradient(135deg, #667eea, #764ba2); border-radius: 12px; color: #fff; margin: 12px">
        <div style="display: flex; justify-content: space-between; align-items: center">
          <div>
            <div style="font-size: 18px; font-weight: bold">{{ card.name }}</div>
            <div style="margin-top: 4px; font-size: 12px; opacity: 0.8">{{ { COUNT: '次卡', YEAR: '年卡', MONTH: '月卡' }[card.cardType] }}</div>
          </div>
          <div style="text-align: right">
            <div v-if="card.cardType === 'COUNT'" style="font-size: 24px; font-weight: bold">{{ card.remainingCount }}次</div>
            <div style="font-size: 12px; opacity: 0.8">{{ card.remainingCount }} / {{ card.totalCount }}</div>
          </div>
        </div>
        <div v-if="card.startDate" style="margin-top: 12px; font-size: 12px; opacity: 0.8">
          有效期: {{ card.startDate }} ~ {{ card.endDate }}
        </div>
        <div style="margin-top: 8px">
          <van-tag :type="card.status === 'ACTIVE' ? 'success' : 'danger'" plain>
            {{ { ACTIVE: '生效中', EXPIRED: '已过期', REFUNDED: '已退款' }[card.status] }}
          </van-tag>
        </div>
      </div>
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
import { getCardList } from '../api'

const active = ref(1)
const cards = ref([])

onMounted(async () => {
  try {
    const phone = localStorage.getItem('h5_phone')
    const storeId = localStorage.getItem('h5_store_id')
    const res = await getCardList({ phone, storeId })
    cards.value = res.data
  } catch (e) { /* */ }
})
</script>
