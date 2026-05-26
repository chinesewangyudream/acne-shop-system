import request from '../utils/request'

// 客户
export const customerPage = (params) => request.get('/admin/customer/page', { params })
export const customerAdd = (data) => request.post('/admin/customer', data)
export const customerUpdate = (data) => request.put('/admin/customer', data)
export const customerDelete = (id) => request.delete(`/admin/customer/${id}`)

// 预约
export const appointmentPage = (params) => request.get('/admin/appointment/page', { params })
export const appointmentAdd = (data) => request.post('/admin/appointment', data)
export const appointmentCancel = (id) => request.put(`/admin/appointment/cancel/${id}`)
export const appointmentComplete = (id) => request.put(`/admin/appointment/complete/${id}`)

// 会员卡
export const memberCardPage = (params) => request.get('/admin/member-card/page', { params })
export const memberCardAdd = (data) => request.post('/admin/member-card', data)
export const memberCardUpdate = (data) => request.put('/admin/member-card', data)
export const memberCardUse = (id) => request.put(`/admin/member-card/use/${id}`)

// 服务项目
export const serviceItemPage = (params) => request.get('/admin/service-item/page', { params })
export const serviceItemAdd = (data) => request.post('/admin/service-item', data)
export const serviceItemUpdate = (data) => request.put('/admin/service-item', data)
export const serviceItemDelete = (id) => request.delete(`/admin/service-item/${id}`)

// 产品
export const productPage = (params) => request.get('/admin/product/page', { params })
export const productAdd = (data) => request.post('/admin/product', data)
export const productUpdate = (data) => request.put('/admin/product', data)
export const productDelete = (id) => request.delete(`/admin/product/${id}`)

// 员工
export const userPage = (params) => request.get('/admin/user/page', { params })
export const userAdd = (data) => request.post('/admin/user', data)
export const userUpdate = (data) => request.put('/admin/user', data)
export const userDelete = (id) => request.delete(`/admin/user/${id}`)

// 门店
export const storePage = (params) => request.get('/admin/store/page', { params })
export const storeAdd = (data) => request.post('/admin/store', data)
export const storeUpdate = (data) => request.put('/admin/store', data)
export const storeDelete = (id) => request.delete(`/admin/store/${id}`)
