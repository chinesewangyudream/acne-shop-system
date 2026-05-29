<template>
  <div class="cards-page">
    <van-nav-bar title="我的卡项" left-arrow @click-left="$router.back()" />

    <van-empty v-if="countCards.length === 0 && periodCards.length === 0" description="暂无卡项" image="search" style="padding-top: 80px" />

    <div class="card-list">
      <div class="member-card" v-for="card in countCards" :key="'c'+card.id">
        <div class="card-inner card-active">
          <div class="card-top">
            <div class="card-info">
              <span class="card-name">次卡</span>
              <span class="card-type">剩余 {{ card.remainingCount }} / {{ card.totalCount }} 次</span>
            </div>
            <van-tag type="success" plain size="medium">生效中</van-tag>
          </div>
          <div class="card-body">
            <span class="price-value">¥{{ card.purchasePrice }}</span>
          </div>
        </div>
      </div>

      <div class="member-card" v-for="card in periodCards" :key="'p'+card.id">
        <div class="card-inner" :class="card.status === 1 ? 'card-active' : 'card-inactive'">
          <div class="card-top">
            <div class="card-info">
              <span class="card-name">{{ card.cardType === 1 ? '月卡' : '年卡' }}</span>
              <span class="card-type">{{ card.startDate }} ~ {{ card.endDate }}</span>
            </div>
            <van-tag :type="card.status === 1 ? 'success' : 'danger'" plain size="medium">
              {{ card.status === 1 ? '生效中' : card.status === 2 ? '已过期' : '已退' }}
            </van-tag>
          </div>
          <div class="card-body">
            <span class="price-value">¥{{ card.purchasePrice }}</span>
          </div>
        </div>
      </div>
    </div>
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
const countCards = ref([])
const periodCards = ref([])

onMounted(async () => {
  try {
    const customerId = localStorage.getItem('h5_customer_id')
    const storeId = localStorage.getItem('h5_store_id')
    const res = await getCardList({ customerId, storeId })
    countCards.value = res.data.countCards || []
    periodCards.value = res.data.periodCards || []
  } catch (e) { /* */ }
})
</script>

<style scoped>
.cards-page {
  padding-bottom: 70px;
  min-height: 100vh;
  background: var(--h5-bg);
}

.card-list {
  padding: 12px 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.member-card {
  border-radius: 16px;
  overflow: hidden;
}

.card-inner {
  padding: 20px;
  border-radius: 16px;
  color: #fff;
}

.card-active {
  background: linear-gradient(135deg, #3a7d7b 0%, #5ba3a1 100%);
  box-shadow: 0 4px 16px rgba(58, 125, 123, 0.25);
}

.card-inactive {
  background: linear-gradient(135deg, #8c9ba5 0%, #a8b5bd 100%);
  box-shadow: 0 4px 16px rgba(140, 155, 165, 0.15);
}

.card-top {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.card-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.card-name {
  font-size: 18px;
  font-weight: 600;
}

.card-type {
  font-size: 12px;
  opacity: 0.7;
}

.card-inner :deep(.van-tag) {
  background: rgba(255, 255, 255, 0.2) !important;
  border-color: transparent !important;
  color: #fff !important;
}

.card-body {
  margin-bottom: 8px;
}

.price-value {
  font-size: 32px;
  font-weight: 700;
  line-height: 1;
}
</style>
