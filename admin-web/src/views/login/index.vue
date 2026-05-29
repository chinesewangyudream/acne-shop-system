<template>
  <div class="login-page">
    <div class="login-left">
      <div class="deco-circle deco-circle-1"></div>
      <div class="deco-circle deco-circle-2"></div>
      <div class="brand">
        <div class="brand-mark">痘</div>
        <h1 class="brand-title">祛痘店铺<br/>管理系统</h1>
        <p class="brand-desc">客户 · 预约 · 卡项 · 门店<br/>一站式运营管理平台</p>
      </div>
      <div class="brand-features">
        <div class="feature" v-for="f in features" :key="f.text">
          <span class="feature-dot"></span>
          <span>{{ f.text }}</span>
        </div>
      </div>
    </div>
    <div class="login-right">
      <div class="login-form-wrap">
        <h3 class="form-title">欢迎回来</h3>
        <p class="form-subtitle">登录以继续管理您的店铺</p>
        <el-form ref="formRef" :model="form" :rules="rules" @keyup.enter="handleLogin" class="login-form">
          <el-form-item prop="phone">
            <el-input v-model="form.phone" placeholder="手机号" :prefix-icon="Iphone" size="large" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password" type="password" placeholder="密码" :prefix-icon="Lock" size="large" show-password />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="large" class="login-btn" :loading="loading" @click="handleLogin">
              登 录
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { Iphone, Lock } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../store/user'
import { login } from '../../api'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref()
const loading = ref(false)

const form = reactive({ phone: '', password: '' })
const rules = {
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const features = [
  { text: '智能预约排程' },
  { text: '会员卡项管理' },
  { text: '多门店数据隔离' },
  { text: '经营数据看板' },
]

async function handleLogin() {
  await formRef.value.validate()
  loading.value = true
  try {
    const res = await login(form)
    userStore.setLogin(res.data)
    ElMessage.success('登录成功')
    router.push('/')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  height: 100vh;
  display: flex;
  overflow: hidden;
}

.login-left {
  flex: 1;
  background: var(--c-bg-sidebar);
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 80px;
  position: relative;
  overflow: hidden;
}

.deco-circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.08;
}

.deco-circle-1 {
  width: 400px;
  height: 400px;
  background: var(--c-teal);
  top: -120px;
  right: -80px;
}

.deco-circle-2 {
  width: 300px;
  height: 300px;
  background: var(--c-coral);
  bottom: -80px;
  left: -60px;
}

.brand {
  position: relative;
  z-index: 1;
}

.brand-mark {
  width: 56px;
  height: 56px;
  border-radius: 16px;
  background: var(--c-teal);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 24px;
  margin-bottom: 28px;
}

.brand-title {
  font-size: 38px;
  font-weight: 700;
  color: #fff;
  margin: 0;
  line-height: 1.3;
  letter-spacing: 1px;
}

.brand-desc {
  margin-top: 16px;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.45);
  line-height: 1.8;
}

.brand-features {
  margin-top: 48px;
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.feature {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.5);
}

.feature-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: var(--c-teal);
  flex-shrink: 0;
}

.login-right {
  width: 460px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
}

.login-form-wrap {
  width: 320px;
}

.form-title {
  font-size: 26px;
  font-weight: 700;
  color: var(--c-text);
  margin: 0;
}

.form-subtitle {
  font-size: 14px;
  color: var(--c-text-secondary);
  margin: 8px 0 36px;
}

.login-form :deep(.el-input__wrapper) {
  padding: 4px 14px;
  border-radius: 10px !important;
}

.login-btn {
  width: 100%;
  height: 46px;
  font-size: 15px;
  font-weight: 600;
  border-radius: 10px !important;
  letter-spacing: 4px;
}
</style>
