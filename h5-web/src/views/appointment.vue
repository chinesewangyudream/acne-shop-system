<template>
  <div style="padding-bottom: 60px">
    <van-nav-bar title="在线预约" left-arrow @click-left="$router.back()" />

    <van-cell-group inset style="margin-top: 12px">
      <van-field v-model="form.appointmentDate" is-link readonly label="预约日期" placeholder="选择日期" @click="showDatePicker = true" />
      <van-field v-model="form.startTime" is-link readonly label="开始时间" placeholder="选择时间" @click="showTimePicker = true" />
      <van-field v-model="selectedServiceName" is-link readonly label="服务项目" placeholder="选择项目" @click="showServicePicker = true" />
      <van-field v-model="form.remark" label="备注" placeholder="选填" type="textarea" rows="2" />
    </van-cell-group>

    <div style="padding: 20px">
      <van-button type="primary" block round @click="handleSubmit">提交预约</van-button>
    </div>

    <van-popup v-model:show="showDatePicker" position="bottom" round>
      <van-date-picker v-model="datePickerValue" @confirm="onDateConfirm" @cancel="showDatePicker = false" :min-date="new Date()" />
    </van-popup>

    <van-popup v-model:show="showTimePicker" position="bottom" round>
      <van-time-picker v-model="timePickerValue" @confirm="onTimeConfirm" @cancel="showTimePicker = false" />
    </van-popup>

    <van-popup v-model:show="showServicePicker" position="bottom" round>
      <van-picker :columns="serviceColumns" @confirm="onServiceConfirm" @cancel="showServicePicker = false" />
    </van-popup>
  </div>

  <van-tabbar v-model="active" route>
    <van-tabbar-item icon="home-o" to="/home">首页</van-tabbar-item>
    <van-tabbar-item icon="card" to="/cards">卡项</van-tabbar-item>
    <van-tabbar-item icon="clock-o" to="/appointment">预约</van-tabbar-item>
  </van-tabbar>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { showToast } from 'vant'
import { useRouter } from 'vue-router'
import { getServices, createAppointment } from '../api'

const router = useRouter()
const active = ref(2)
const showDatePicker = ref(false)
const showTimePicker = ref(false)
const showServicePicker = ref(false)

const form = reactive({ appointmentDate: '', startTime: '', serviceItemId: '', remark: '' })
const selectedServiceName = ref('')
const services = ref([])

const datePickerValue = ref(['2026', '01', '01'])
const timePickerValue = ref(['09', '00'])

const serviceColumns = computed(() => services.value.map(s => ({ text: `${s.name} - ¥${s.price}`, value: s.id })))

function onDateConfirm({ selectedValues }) {
  form.appointmentDate = selectedValues.join('-')
  showDatePicker.value = false
}

function onTimeConfirm({ selectedValues }) {
  form.startTime = selectedValues.join(':')
  showTimePicker.value = false
}

function onServiceConfirm({ selectedOptions }) {
  form.serviceItemId = selectedOptions[0].value
  selectedServiceName.value = selectedOptions[0].text
  showServicePicker.value = false
}

async function handleSubmit() {
  if (!form.appointmentDate || !form.startTime || !form.serviceItemId) {
    showToast('请填写完整预约信息')
    return
  }
  const storeId = localStorage.getItem('h5_store_id')
  await createAppointment({ ...form, storeId, source: 'SELF', status: 'PENDING' })
  showToast('预约成功')
  router.push('/home')
}

onMounted(async () => {
  const storeId = localStorage.getItem('h5_store_id')
  if (storeId) {
    const res = await getServices(storeId)
    services.value = res.data
  }
})
</script>
