import request from '../utils/request'

export const sendSmsCode = (phone) => request.post(`/h5/auth/send-code?phone=${phone}`)
export const h5Login = (data) => request.post('/h5/auth/login', data)
export const getCardList = (params) => request.get('/h5/card/list', { params })
export const getCountCardDetail = (id) => request.get(`/h5/card/count/${id}`)
export const getPeriodCardDetail = (id) => request.get(`/h5/card/period/${id}`)
export const getServices = (storeId) => request.get('/h5/appointment/services', { params: { storeId } })
export const createAppointment = (data) => request.post('/h5/appointment', data)
export const getMyAppointments = (params) => request.get('/h5/appointment/my', { params })
export const cancelAppointment = (id) => request.put(`/h5/appointment/cancel/${id}`)
