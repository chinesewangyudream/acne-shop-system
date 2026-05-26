import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Login',
    component: () => import('../views/login.vue')
  },
  {
    path: '/home',
    name: 'Home',
    component: () => import('../views/home.vue')
  },
  {
    path: '/cards',
    name: 'Cards',
    component: () => import('../views/cards.vue')
  },
  {
    path: '/appointment',
    name: 'Appointment',
    component: () => import('../views/appointment.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('h5_token')
  if (to.path !== '/' && !token) {
    next('/')
  } else {
    next()
  }
})

export default router
