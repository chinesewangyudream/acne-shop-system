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
      { path: 'dashboard', name: 'Dashboard', component: () => import('../views/dashboard/index.vue'), meta: { title: '工作台' } },
      { path: 'customer', name: 'Customer', component: () => import('../views/customer/index.vue'), meta: { title: '客户管理' } },
      { path: 'appointment', name: 'Appointment', component: () => import('../views/appointment/index.vue'), meta: { title: '预约管理' } },
      { path: 'member-card', name: 'MemberCard', component: () => import('../views/memberCard/index.vue'), meta: { title: '卡项管理' } },
      { path: 'service-item', name: 'ServiceItem', component: () => import('../views/serviceItem/index.vue'), meta: { title: '服务项目' } },
      { path: 'product', name: 'Product', component: () => import('../views/product/index.vue'), meta: { title: '产品管理' } },
      { path: 'employee', name: 'Employee', component: () => import('../views/employee/index.vue'), meta: { title: '员工管理' } },
      { path: 'store', name: 'Store', component: () => import('../views/store/index.vue'), meta: { title: '门店管理' } },
      { path: 'statistics', name: 'Statistics', component: () => import('../views/statistics/index.vue'), meta: { title: '数据统计' } },
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
