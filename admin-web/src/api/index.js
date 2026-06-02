import request from '../utils/request'

// 认证
export const login = (data) => request.post('/admin/auth/login', data)
export const getInfo = () => request.get('/admin/auth/info')

// 客户
export const customerPage = (params) => request.get('/admin/customer/page', { params })
export const customerAdd = (data) => request.post('/admin/customer', data)
export const customerUpdate = (data) => request.put('/admin/customer', data)
export const customerDelete = (id) => request.delete(`/admin/customer/${id}`)
export const customerBlacklist = (id, reason) => request.put(`/admin/customer/blacklist/${id}`, null, { params: { reason } })
export const customerUnblacklist = (id) => request.put(`/admin/customer/unblacklist/${id}`)

// 预约
export const appointmentPage = (params) => request.get('/admin/appointment/page', { params })
export const appointmentAdd = (data) => request.post('/admin/appointment', data)
export const appointmentConfirm = (id) => request.put(`/admin/appointment/confirm/${id}`)
export const appointmentCancel = (id) => request.put(`/admin/appointment/cancel/${id}`)
export const appointmentComplete = (id) => request.put(`/admin/appointment/complete/${id}`)
export const appointmentReschedule = (id, data) => request.put(`/admin/appointment/reschedule/${id}`, data)

// 次卡
export const countCardPage = (params) => request.get('/admin/count-card/page', { params })
export const countCardAdd = (data) => request.post('/admin/count-card', data)
export const countCardUpdate = (data) => request.put('/admin/count-card', data)
export const countCardUse = (id) => request.put(`/admin/count-card/use/${id}`)
export const countCardRefund = (id) => request.put(`/admin/count-card/refund/${id}`)

// 年月卡
export const periodCardPage = (params) => request.get('/admin/period-card/page', { params })
export const periodCardAdd = (data) => request.post('/admin/period-card', data)
export const periodCardUpdate = (data) => request.put('/admin/period-card', data)
export const periodCardRefund = (id) => request.put(`/admin/period-card/refund/${id}`)

// 疗程方案
export const treatmentPlanPage = (params) => request.get('/admin/treatment-plan/page', { params })
export const treatmentPlanAdd = (data) => request.post('/admin/treatment-plan', data)
export const treatmentPlanUpdate = (data) => request.put('/admin/treatment-plan', data)
export const treatmentPlanDelete = (id) => request.delete(`/admin/treatment-plan/${id}`)

// 皮肤档案
export const skinRecordList = (customerId) => request.get('/admin/skin-record/list', { params: { customerId } })
export const skinRecordAdd = (data) => request.post('/admin/skin-record', data)
export const skinRecordUpdate = (data) => request.put('/admin/skin-record', data)
export const skinRecordDelete = (id) => request.delete(`/admin/skin-record/${id}`)

// 服务项目
export const serviceItemPage = (params) => request.get('/admin/service-item/page', { params })
export const serviceItemList = (params) => request.get('/admin/service-item/list', { params })
export const serviceItemAdd = (data) => request.post('/admin/service-item', data)
export const serviceItemUpdate = (data) => request.put('/admin/service-item', data)
export const serviceItemDelete = (id) => request.delete(`/admin/service-item/${id}`)

// 产品
export const productPage = (params) => request.get('/admin/product/page', { params })
export const productAdd = (data) => request.post('/admin/product', data)
export const productUpdate = (data) => request.put('/admin/product', data)
export const productDelete = (id) => request.delete(`/admin/product/${id}`)
export const productStockWarning = (params) => request.get('/admin/product/warning', { params })

// 库存变动
export const stockLogPage = (params) => request.get('/admin/stock-log/page', { params })
export const stockIn = (data) => request.post('/admin/stock-log/in', data)
export const stockOut = (data) => request.post('/admin/stock-log/out', data)

// 员工
export const employeePage = (params) => request.get('/admin/employee/page', { params })
export const employeeList = (params) => request.get('/admin/employee/list', { params })
export const employeeAdd = (data) => request.post('/admin/employee', data)
export const employeeUpdate = (data) => request.put('/admin/employee', data)
export const employeeDelete = (id) => request.delete(`/admin/employee/${id}`)

// 门店
export const storePage = (params) => request.get('/admin/store/page', { params })
export const storeList = () => request.get('/admin/store/list')
export const storeAdd = (data) => request.post('/admin/store', data)
export const storeUpdate = (data) => request.put('/admin/store', data)
export const storeDelete = (id) => request.delete(`/admin/store/${id}`)

// 考勤
export const attendancePage = (params) => request.get('/admin/attendance/page', { params })
export const attendanceClockIn = (employeeId) => request.post('/admin/attendance/clock-in', null, { params: { employeeId } })
export const attendanceClockOut = (id) => request.put(`/admin/attendance/clock-out/${id}`)
export const attendanceApproveLeave = (id) => request.put(`/admin/attendance/leave-approve/${id}`)
export const attendanceRejectLeave = (id) => request.put(`/admin/attendance/leave-reject/${id}`)

// 操作日志
export const operationLogPage = (params) => request.get('/admin/operation-log/page', { params })

// 消费记录
export const consumptionRecordPage = (params) => request.get('/admin/consumption-record/page', { params })
export const consumptionRecordAdd = (data) => request.post('/admin/consumption-record', data)

// 核销记录
export const writeoffRecordPage = (params) => request.get('/admin/writeoff-record/page', { params })
export const writeoffRecordDetail = (id) => request.get(`/admin/writeoff-record/${id}`)
