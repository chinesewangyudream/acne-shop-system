<template>
  <div class="login-page">
    <div class="login-deco"></div>
    <div class="login-content">
      <div class="brand">
        <div class="brand-mark">痘</div>
        <h1 class="brand-title">祛痘护理中心</h1>
        <p class="brand-sub">专业护肤 · 科学祛痘</p>
      </div>

      <div class="form-card">
        <van-cell-group inset>
          <van-field v-model="phone" label="手机号" placeholder="请输入手机号" type="tel" maxlength="11" />
          <van-field v-model="code" label="验证码" placeholder="请输入验证码" maxlength="6">
            <template #button>
              <van-button size="small" type="primary" :disabled="countdown > 0" @click="handleSendCode" class="code-btn">
                {{ countdown > 0 ? `${countdown}s` : '获取验证码' }}
              </van-button>
            </template>
          </van-field>
        </van-cell-group>

        <div class="btn-wrap">
          <van-button type="primary" block round size="large" @click="handleLogin" class="login-btn">登 录</van-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { showToast } from 'vant'
import { sendSmsCode, h5Login } from '../api'

const router = useRouter()
const route = useRoute()
const phone = ref('')
const code = ref('')
const countdown = ref(0)

const storeId = route.query.storeId || ''

async function handleSendCode() {
  if (!phone.value || phone.value.length !== 11) {
    showToast('请输入正确的手机号')
    return
  }
  await sendSmsCode(phone.value)
  showToast('验证码已发送')
  countdown.value = 60
  const timer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) clearInterval(timer)
  }, 1000)
}

async function handleLogin() {
  if (!phone.value || !code.value) {
    showToast('请输入手机号和验证码')
    return
  }
  const res = await h5Login({ phone: phone.value, code: code.value, storeId })
  localStorage.setItem('h5_token', res.data.token)
  localStorage.setItem('h5_phone', phone.value)
  showToast('登录成功')
  router.push('/home')
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  background: var(--h5-teal-dark);
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-deco {
  position: absolute;
  width: 500px;
  height: 500px;
  border-radius: 50%;
  background: var(--h5-teal);
  opacity: 0.15;
  top: -200px;
  right: -120px;
}

.login-content {
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

.code-btn {
  font-size: 12px !important;
  border-radius: 8px !important;
}

.btn-wrap {
  padding: 20px 20px 16px;
}

.login-btn {
  height: 46px;
  font-size: 15px;
  font-weight: 600;
  letter-spacing: 4px;
}
</style>
