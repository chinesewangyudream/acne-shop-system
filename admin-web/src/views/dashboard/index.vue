<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover"><el-statistic title="今日到店" :value="0" /></el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover"><el-statistic title="今日营收" :value="0" prefix="¥" /></el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover"><el-statistic title="今日预约" :value="0" /></el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover"><el-statistic title="待跟进客户" :value="0" /></el-card>
      </el-col>
    </el-row>
    <el-card style="margin-top: 20px">
      <template #header>快捷操作</template>
      <el-space wrap>
        <el-button type="primary" @click="$router.push('/appointment')">预约管理</el-button>
        <el-button type="success" @click="$router.push('/member-card')">卡项管理</el-button>
        <el-button type="warning" @click="$router.push('/customer')">客户管理</el-button>
      </el-space>
    </el-card>
  </div>
</template>
