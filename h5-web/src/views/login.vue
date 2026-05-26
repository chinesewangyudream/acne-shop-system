<template>
  <div style="min-height: 100vh; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); display: flex; align-items: center; justify-content: center; padding: 20px">
    <div style="width: 100%; max-width: 400px">
      <h2 style="color: #fff; text-align: center; margin-bottom: 30px">祛痘护理中心</h2>
      <van-cell-group inset style="border-radius: 12px; overflow: hidden">
        <van-field v-model="phone" label="手机号" placeholder="请输入手机号" type="tel" maxlength="11" />
        <van-field v-model="code" label="验证码" placeholder="请输入验证码" maxlength="6">
          <template #button>
            <van-button size="small" type="primary" :disabled="countdown > 0" @click="handleSendCode">
              {{ countdown > 0 ? `${countdown}s` : '获取验证码' }}
            </van-button>
          </template>
        </van-field>
      </van-cell-group>
      <div style="padding: 20px">
        <van-button type="primary" block round size="large" @click="handleLogin">登 录</van-button>
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
