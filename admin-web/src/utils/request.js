import axios from 'axios'
import { ElMessage, ElLoading } from 'element-plus'
import router from '../router'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 全局 loading 控制
let loadingCount = 0
let loadingInstance = null

function startLoading() {
  if (loadingCount === 0) {
    loadingInstance = ElLoading.service({ lock: true, text: '加载中...', background: 'rgba(0, 0, 0, 0.15)' })
  }
  loadingCount++
}

function stopLoading() {
  loadingCount--
  if (loadingCount <= 0) {
    loadingCount = 0
    if (loadingInstance) {
      loadingInstance.close()
      loadingInstance = null
    }
  }
}

request.interceptors.request.use(config => {
  startLoading()
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  if (config.params) {
    config.params = Object.fromEntries(
      Object.entries(config.params).filter(([, v]) => v !== '' && v !== null && v !== undefined)
    )
  }
  return config
})

request.interceptors.response.use(
  response => {
    stopLoading()
    const res = response.data
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      if (res.code === 401) {
        localStorage.removeItem('token')
        router.push('/login')
      }
      if (res.code === 403) {
        ElMessage.error('无权限访问该功能')
      }
      return Promise.reject(new Error(res.message))
    }
    return res
  },
  error => {
    stopLoading()
    ElMessage.error(error.message || '网络异常')
    return Promise.reject(error)
  }
)

export default request
