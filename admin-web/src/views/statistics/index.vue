<template>
  <el-card>
    <template #header><span>数据统计</span></template>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="营业概览" name="overview">
        <el-row :gutter="20">
          <el-col :span="6"><el-card shadow="hover"><el-statistic title="本月营收" :value="0" prefix="¥" /></el-card></el-col>
          <el-col :span="6"><el-card shadow="hover"><el-statistic title="本月到店人次" :value="0" /></el-card></el-col>
          <el-col :span="6"><el-card shadow="hover"><el-statistic title="平均客单价" :value="0" prefix="¥" /></el-card></el-col>
          <el-col :span="6"><el-card shadow="hover"><el-statistic title="新增客户" :value="0" /></el-card></el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="门店对比" name="compare">
        <p style="color: #999; text-align: center; padding: 40px">门店对比数据（待对接）</p>
      </el-tab-pane>
      <el-tab-pane label="员工绩效" name="performance">
        <p style="color: #999; text-align: center; padding: 40px">员工绩效数据（待对接）</p>
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<script setup>
import { ref } from 'vue'
const activeTab = ref('overview')
</script>
