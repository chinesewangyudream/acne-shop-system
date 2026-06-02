import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/login/index.vue')
  },
  {
    path: '/',
    component: () => import('../layout/index.vue'),
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', name: 'Dashboard', component: () => import('../views/dashboard/index.vue'), meta: { title: '工作台', roles: [1, 2] } },
      { path: 'customer', name: 'Customer', component: () => import('../views/customer/index.vue'), meta: { title: '客户管理', roles: [1, 2, 3] } },
      { path: 'appointment', name: 'Appointment', component: () => import('../views/appointment/index.vue'), meta: { title: '预约管理', roles: [1, 2, 3] } },
      { path: 'count-card', name: 'CountCard', component: () => import('../views/countCard/index.vue'), meta: { title: '次卡管理', roles: [1, 2, 3] } },
      { path: 'period-card', name: 'PeriodCard', component: () => import('../views/periodCard/index.vue'), meta: { title: '年月卡管理', roles: [1, 2, 3] } },
      { path: 'writeoff-record', name: 'WriteoffRecord', component: () => import('../views/writeoffRecord/index.vue'), meta: { title: '核销记录', roles: [1, 2, 3] } },
      { path: 'treatment-plan', name: 'TreatmentPlan', component: () => import('../views/treatmentPlan/index.vue'), meta: { title: '疗程方案', roles: [1, 2] } },
      { path: 'service-item', name: 'ServiceItem', component: () => import('../views/serviceItem/index.vue'), meta: { title: '服务项目', roles: [1, 2] } },
      { path: 'product', name: 'Product', component: () => import('../views/product/index.vue'), meta: { title: '产品管理', roles: [1, 2] } },
      { path: 'employee', name: 'Employee', component: () => import('../views/employee/index.vue'), meta: { title: '员工管理', roles: [1, 2] } },
      { path: 'store', name: 'Store', component: () => import('../views/store/index.vue'), meta: { title: '门店管理', roles: [1] } },
      { path: 'statistics', name: 'Statistics', component: () => import('../views/statistics/index.vue'), meta: { title: '数据统计', roles: [1, 2] } },
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path === '/login') {
    next()
    return
  }
  if (!token) {
    next('/login')
    return
  }
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  // 旧数据role是字符串，清掉让用户重新登录
  if (typeof userInfo.role === 'string') {
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    next('/login')
    return
  }
  const requiredRoles = to.meta?.roles
  if (requiredRoles && requiredRoles.length > 0 && !requiredRoles.includes(userInfo.role)) {
    // 角色不匹配时，找第一个有权限的页面，避免无限重定向
    const firstAllowed = routes[1].children.find(child => child.meta?.roles?.includes(userInfo.role))
    if (firstAllowed && to.path !== '/' + firstAllowed.path) {
      next('/' + firstAllowed.path)
    } else {
      next()
    }
    return
  }
  next()
})

export default router
