-- 祛痘店铺管理系统 数据库初始化脚本
-- 按概要设计说明书 V1.0 重建

CREATE DATABASE IF NOT EXISTS acne_shop DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE acne_shop;

-- 1. 门店表
DROP TABLE IF EXISTS store;
CREATE TABLE store (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_name VARCHAR(100) NOT NULL COMMENT '门店名称',
    address VARCHAR(255) COMMENT '门店地址',
    phone VARCHAR(20) COMMENT '联系电话',
    business_hours VARCHAR(100) COMMENT '营业时间',
    qr_code_url VARCHAR(500) COMMENT '门店二维码链接',
    status TINYINT DEFAULT 1 COMMENT '状态：1-营业 0-停业',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '逻辑删除：0-正常 1-删除'
) COMMENT '门店表';

-- 2. 员工表（替代原user表）
DROP TABLE IF EXISTS employee;
CREATE TABLE employee (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_id BIGINT COMMENT '所属门店（老板为空）',
    name VARCHAR(50) NOT NULL COMMENT '员工姓名',
    phone VARCHAR(20) NOT NULL UNIQUE COMMENT '手机号（登录账号）',
    password_hash VARCHAR(255) NOT NULL COMMENT '密码哈希',
    role TINYINT NOT NULL COMMENT '角色：1-老板 2-店长 3-前台 4-美容师',
    skills VARCHAR(500) COMMENT '技能标签，逗号分隔',
    commission_rules JSON COMMENT '提成规则配置',
    status TINYINT DEFAULT 1 COMMENT '状态：1-在职 0-离职',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '逻辑删除：0-正常 1-删除'
) COMMENT '员工表';

-- 3. 客户表
DROP TABLE IF EXISTS customer;
CREATE TABLE customer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_id BIGINT COMMENT '所属门店（首次到店门店）',
    name VARCHAR(50) NOT NULL COMMENT '客户姓名',
    gender TINYINT COMMENT '性别：0-女 1-男',
    age INT COMMENT '年龄',
    phone VARCHAR(20) NOT NULL COMMENT '手机号',
    allergy_history TEXT COMMENT '过敏史',
    acne_type VARCHAR(100) COMMENT '痘痘类型',
    severity TINYINT COMMENT '严重程度：1-轻度 2-中度 3-重度',
    tags VARCHAR(500) COMMENT '标签，逗号分隔',
    source_channel VARCHAR(100) COMMENT '来源渠道',
    is_blacklisted TINYINT DEFAULT 0 COMMENT '是否黑名单：0-否 1-是',
    blacklist_reason VARCHAR(255) COMMENT '黑名单原因',
    last_visit_at DATETIME COMMENT '最后到店时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '逻辑删除：0-正常 1-删除'
) COMMENT '客户表';

-- 4. 皮肤档案表
DROP TABLE IF EXISTS skin_record;
CREATE TABLE skin_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    acne_type VARCHAR(100) COMMENT '痘痘类型',
    severity TINYINT COMMENT '严重程度',
    affected_areas VARCHAR(255) COMMENT '患处描述',
    photo_urls JSON COMMENT '患处照片URL列表',
    note TEXT COMMENT '备注',
    record_date DATE NOT NULL COMMENT '记录日期',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) COMMENT '皮肤档案表';

-- 5. 客户跟进记录表
DROP TABLE IF EXISTS follow_up;
CREATE TABLE follow_up (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    employee_id BIGINT NOT NULL COMMENT '跟进员工',
    content TEXT NOT NULL COMMENT '跟进内容',
    next_follow_date DATE COMMENT '下次跟进日期',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '逻辑删除'
) COMMENT '客户跟进记录表';

-- 6. 服务项目表
DROP TABLE IF EXISTS service_item;
CREATE TABLE service_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_id BIGINT NOT NULL COMMENT '所属门店',
    name VARCHAR(100) NOT NULL COMMENT '项目名称',
    price DECIMAL(10,2) NOT NULL COMMENT '单次定价',
    duration INT NOT NULL COMMENT '时长（分钟）',
    description TEXT COMMENT '项目描述',
    status TINYINT DEFAULT 1 COMMENT '状态：1-启用 0-停用',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '逻辑删除'
) COMMENT '服务项目表';

-- 7. 疗程方案表
DROP TABLE IF EXISTS treatment_plan;
CREATE TABLE treatment_plan (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_id BIGINT NOT NULL COMMENT '所属门店',
    name VARCHAR(100) NOT NULL COMMENT '疗程名称',
    total_count INT NOT NULL COMMENT '总次数',
    price DECIMAL(10,2) NOT NULL COMMENT '套餐价格',
    service_item_ids JSON COMMENT '包含的服务项目ID列表',
    description TEXT COMMENT '疗程说明',
    status TINYINT DEFAULT 1 COMMENT '状态：1-启用 0-停用',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '逻辑删除'
) COMMENT '疗程方案表';

-- 8. 治疗记录表
DROP TABLE IF EXISTS treatment_record;
CREATE TABLE treatment_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    appointment_id BIGINT COMMENT '关联预约ID',
    employee_id BIGINT NOT NULL COMMENT '操作美容师',
    service_item_id BIGINT NOT NULL COMMENT '服务项目',
    content TEXT COMMENT '操作内容',
    products_used JSON COMMENT '使用产品记录[{product_id,quantity}]',
    before_photos JSON COMMENT '术前照片URL列表',
    after_photos JSON COMMENT '术后照片URL列表',
    feedback TEXT COMMENT '术后反馈',
    treatment_date DATETIME NOT NULL COMMENT '治疗时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '逻辑删除'
) COMMENT '治疗记录表';

-- 9. 产品表
DROP TABLE IF EXISTS product;
CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_id BIGINT NOT NULL COMMENT '所属门店',
    name VARCHAR(100) NOT NULL COMMENT '产品名称',
    specification VARCHAR(100) COMMENT '规格',
    purchase_price DECIMAL(10,2) COMMENT '进价',
    selling_price DECIMAL(10,2) COMMENT '售价',
    stock INT DEFAULT 0 COMMENT '库存数量',
    warning_stock INT DEFAULT 10 COMMENT '库存预警阈值',
    image_url VARCHAR(500) COMMENT '产品图片',
    status TINYINT DEFAULT 1 COMMENT '状态：1-在售 0-停售',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '逻辑删除'
) COMMENT '产品表';

-- 10. 库存变动记录表
DROP TABLE IF EXISTS stock_log;
CREATE TABLE stock_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL COMMENT '产品ID',
    type TINYINT NOT NULL COMMENT '类型：1-入库 2-出库',
    quantity INT NOT NULL COMMENT '变动数量',
    reason VARCHAR(255) COMMENT '变动原因',
    operator_id BIGINT NOT NULL COMMENT '操作人',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) COMMENT '库存变动记录表';

-- 11. 次卡表
DROP TABLE IF EXISTS count_card;
CREATE TABLE count_card (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    store_id BIGINT NOT NULL COMMENT '办卡门店（限本店使用）',
    treatment_plan_id BIGINT COMMENT '关联疗程方案',
    total_count INT NOT NULL COMMENT '购买总次数',
    remaining_count INT NOT NULL COMMENT '剩余次数',
    purchase_price DECIMAL(10,2) NOT NULL COMMENT '购买价格',
    status TINYINT DEFAULT 1 COMMENT '状态：1-正常 0-已退 2-已用完',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '逻辑删除'
) COMMENT '次卡表';

-- 12. 年卡/月卡表
DROP TABLE IF EXISTS period_card;
CREATE TABLE period_card (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    store_id BIGINT NOT NULL COMMENT '办卡门店（限本店使用）',
    card_type TINYINT NOT NULL COMMENT '卡类型：1-月卡 2-年卡',
    start_date DATE NOT NULL COMMENT '生效日期',
    end_date DATE NOT NULL COMMENT '到期日期',
    benefits JSON COMMENT '权益内容描述',
    purchase_price DECIMAL(10,2) NOT NULL COMMENT '购买价格',
    status TINYINT DEFAULT 1 COMMENT '状态：1-正常 0-已退 2-已过期',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '逻辑删除'
) COMMENT '年卡/月卡表';

-- 13. 消费记录表
DROP TABLE IF EXISTS consumption_record;
CREATE TABLE consumption_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    store_id BIGINT NOT NULL COMMENT '消费门店',
    type TINYINT NOT NULL COMMENT '类型：1-次卡消费 2-年月卡消费 3-充值 4-产品购买 5-退款',
    amount DECIMAL(10,2) NOT NULL COMMENT '金额',
    card_id BIGINT COMMENT '关联卡项ID',
    card_type TINYINT COMMENT '卡类型：1-次卡 2-年月卡',
    description VARCHAR(255) COMMENT '说明',
    operator_id BIGINT NOT NULL COMMENT '操作人',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) COMMENT '消费记录表';

-- 14. 预约表
DROP TABLE IF EXISTS appointment;
CREATE TABLE appointment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    store_id BIGINT NOT NULL COMMENT '预约门店',
    service_item_id BIGINT NOT NULL COMMENT '服务项目',
    employee_id BIGINT COMMENT '美容师',
    appointment_date DATE NOT NULL COMMENT '预约日期',
    start_time TIME NOT NULL COMMENT '开始时间',
    end_time TIME NOT NULL COMMENT '结束时间',
    source TINYINT NOT NULL DEFAULT 2 COMMENT '来源：1-顾客自助 2-前台代约',
    status TINYINT DEFAULT 1 COMMENT '状态：1-待确认 2-已确认 3-已完成 4-已取消 5-已改期',
    remark VARCHAR(255) COMMENT '备注',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '逻辑删除'
) COMMENT '预约表';

-- 15. 排班表
DROP TABLE IF EXISTS schedule;
CREATE TABLE schedule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL COMMENT '员工ID',
    store_id BIGINT NOT NULL COMMENT '门店ID',
    work_date DATE NOT NULL COMMENT '排班日期',
    shift_type TINYINT NOT NULL COMMENT '班次：1-早班 2-晚班 3-全天 4-休息',
    start_time TIME COMMENT '上班时间',
    end_time TIME COMMENT '下班时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '逻辑删除'
) COMMENT '排班表';

-- 16. 考勤记录表
DROP TABLE IF EXISTS attendance;
CREATE TABLE attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL COMMENT '员工ID',
    work_date DATE NOT NULL COMMENT '日期',
    clock_in DATETIME COMMENT '签到时间',
    clock_out DATETIME COMMENT '签退时间',
    leave_type TINYINT COMMENT '请假类型：1-事假 2-病假 3-年假',
    leave_status TINYINT DEFAULT 0 COMMENT '0-未请假 1-待审批 2-已批准 3-已拒绝',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) COMMENT '考勤记录表';

-- 17. 操作日志表
DROP TABLE IF EXISTS operation_log;
CREATE TABLE operation_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    operator_id BIGINT COMMENT '操作人ID',
    operator_name VARCHAR(50) COMMENT '操作人姓名',
    module VARCHAR(50) NOT NULL COMMENT '操作模块',
    action VARCHAR(100) NOT NULL COMMENT '操作动作',
    detail TEXT COMMENT '操作详情',
    ip VARCHAR(50) COMMENT 'IP地址',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) COMMENT '操作日志表';

-- ========== 种子数据 ==========

-- 门店
INSERT INTO store (store_name, address, phone, business_hours, status) VALUES
('旗舰店', '广州市天河区xx路1号', '020-12345678', '09:00-21:00', 1),
('天河店', '广州市天河区yy路2号', '020-87654321', '09:00-21:00', 1);

-- 员工（密码均为 admin123 的BCrypt哈希）
INSERT INTO employee (store_id, name, phone, password_hash, role, status) VALUES
(NULL, '老板', '13650901858', '$2a$10$eXu.ABidpLE8Y2I5IXttJOiELMy0M9MnJtXi/kuQ7EXZIRGWgULVa', 1, 1),
(1, '张店长', '13800000002', '$2a$10$eXu.ABidpLE8Y2I5IXttJOiELMy0M9MnJtXi/kuQ7EXZIRGWgULVa', 2, 1),
(2, '李店长', '13800000003', '$2a$10$eXu.ABidpLE8Y2I5IXttJOiELMy0M9MnJtXi/kuQ7EXZIRGWgULVa', 2, 1);
