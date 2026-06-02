<template>
  <div class="register-page">
    <div class="register-deco"></div>
    <div class="register-content">
      <div class="brand">
        <div class="brand-mark">痘</div>
        <h1 class="brand-title">祛痘护理中心</h1>
        <p class="brand-sub">注册新账号</p>
      </div>

      <div class="form-card">
        <van-cell-group inset>
          <van-field v-model="phone" label="手机号" placeholder="请输入手机号" type="tel" maxlength="11" />
          <van-field v-model="password" label="密码" placeholder="请设置密码(6-20位)" type="password" />
          <van-field v-model="confirmPassword" label="确认密码" placeholder="请再次输入密码" type="password" />
          <van-field v-model="name" label="姓名" placeholder="选填" />
        </van-cell-group>

        <div class="btn-wrap">
          <van-button type="primary" block round size="large" @click="handleRegister" class="register-btn">注 册</van-button>
        </div>

        <div class="login-link">
          已有账号？<span @click="goLogin">去登录</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { showToast } from 'vant'
import { h5Register } from '../api'

const router = useRouter()
const route = useRoute()
const phone = ref('')
const password = ref('')
const confirmPassword = ref('')
const name = ref('')

const storeId = route.query.storeId || ''

function goLogin() {
  router.push({ path: '/', query: route.query })
}

async function handleRegister() {
  if (!phone.value || phone.value.length !== 11) {
    showToast('请输入正确的手机号')
    return
  }
  if (!password.value || password.value.length < 6 || password.value.length > 20) {
    showToast('密码长度为6-20位')
    return
  }
  if (password.value !== confirmPassword.value) {
    showToast('两次密码输入不一致')
    return
  }
  await h5Register({ phone: phone.value, password: password.value, name: name.value, storeId })
  showToast('注册成功')
  router.push({ path: '/', query: route.query })
}
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  background: var(--h5-teal-dark);
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.register-deco {
  position: absolute;
  width: 500px;
  height: 500px;
  border-radius: 50%;
  background: var(--h5-teal);
  opacity: 0.15;
  top: -200px;
  right: -120px;
}

.register-content {
  width: 100%;
  max-width: 400px;
  padding: 0 20px;
  position: relative;
  z-index: 1;
}

.brand {
  text-align: center;
  margin-bottom: 40px;
}

.brand-mark {
  width: 52px;
  height: 52px;
  border-radius: 14px;
  background: var(--h5-teal);
  color: #fff;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 22px;
  margin-bottom: 16px;
}

.brand-title {
  color: #fff;
  font-size: 24px;
  font-weight: 700;
  margin: 0;
  letter-spacing: 1px;
}

.brand-sub {
  color: rgba(255, 255, 255, 0.45);
  font-size: 13px;
  margin: 8px 0 0;
  letter-spacing: 2px;
}

.form-card {
  background: #fff;
  border-radius: 18px;
  padding: 24px 0 8px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
}

.form-card :deep(.van-cell-group--inset) {
  margin: 0 !important;
  border-radius: 0 !important;
  box-shadow: none !important;
}

.form-card :deep(.van-cell) {
  padding: 14px 20px;
}

.form-card :deep(.van-cell::after) {
  left: 20px !important;
  right: 20px !important;
}

.btn-wrap {
  padding: 20px 20px 16px;
}

.register-btn {
  height: 46px;
  font-size: 15px;
  font-weight: 600;
  letter-spacing: 4px;
}

.login-link {
  text-align: center;
  font-size: 13px;
  color: #999;
  padding: 0 20px 16px;
}

.login-link span {
  color: var(--h5-teal);
  cursor: pointer;
  font-weight: 500;
}
</style>
