-- 祛痘店铺管理系统 数据库初始化脚本
CREATE DATABASE IF NOT EXISTS acne_shop DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE acne_shop;

-- 门店表
CREATE TABLE store (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '门店名称',
    address VARCHAR(255) COMMENT '门店地址',
    phone VARCHAR(20) COMMENT '门店电话',
    qrcode VARCHAR(500) COMMENT '门店二维码链接',
    status INT DEFAULT 1 COMMENT '状态 1启用 0停用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '逻辑删除'
) COMMENT '门店表';

-- 员工/用户表
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '登录账号',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    phone VARCHAR(20) COMMENT '手机号',
    role VARCHAR(30) NOT NULL COMMENT '角色: ADMIN/STORE_MANAGER/RECEPTIONIST/BEAUTICIAN',
    store_id BIGINT COMMENT '所属门店ID',
    skill_tags VARCHAR(255) COMMENT '技能标签(逗号分隔)',
    status INT DEFAULT 1 COMMENT '状态 1启用 0停用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
) COMMENT '员工表';

-- 客户表
CREATE TABLE customer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    gender VARCHAR(10) COMMENT '性别',
    phone VARCHAR(20) NOT NULL COMMENT '手机号',
    birthday DATE COMMENT '生日',
    allergy_history TEXT COMMENT '过敏史',
    acne_type VARCHAR(50) COMMENT '痘痘类型',
    skin_type VARCHAR(50) COMMENT '肤质',
    severity VARCHAR(30) COMMENT '严重程度',
    tags VARCHAR(255) COMMENT '标签(逗号分隔)',
    source VARCHAR(50) COMMENT '来源渠道',
    store_id BIGINT COMMENT '所属门店ID(首次到店)',
    status INT DEFAULT 1 COMMENT '状态 1正常 0黑名单',
    last_visit_time DATETIME COMMENT '最后到店时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
) COMMENT '客户表';

-- 服务项目表
CREATE TABLE service_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '项目名称',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    duration INT NOT NULL COMMENT '时长(分钟)',
    description TEXT COMMENT '项目描述',
    store_id BIGINT COMMENT '所属门店ID',
    status INT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
) COMMENT '服务项目表';

-- 预约表
CREATE TABLE appointment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    store_id BIGINT NOT NULL COMMENT '门店ID',
    service_item_id BIGINT NOT NULL COMMENT '服务项目ID',
    beautician_id BIGINT COMMENT '美容师ID',
    appointment_date DATE NOT NULL COMMENT '预约日期',
    start_time TIME NOT NULL COMMENT '开始时间',
    end_time TIME NOT NULL COMMENT '结束时间',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING/CONFIRMED/CANCELLED/COMPLETED',
    remark VARCHAR(500) COMMENT '备注',
    source VARCHAR(20) DEFAULT 'STAFF' COMMENT 'SELF/STAFF',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
) COMMENT '预约表';

-- 会员卡表
CREATE TABLE member_card (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    store_id BIGINT NOT NULL COMMENT '办卡门店ID',
    card_type VARCHAR(20) NOT NULL COMMENT 'COUNT/YEAR/MONTH',
    name VARCHAR(100) NOT NULL COMMENT '卡名称',
    price DECIMAL(10,2) NOT NULL COMMENT '购卡价格',
    total_count INT COMMENT '次卡总次数',
    remaining_count INT COMMENT '次卡剩余次数',
    start_date DATE COMMENT '生效日期',
    end_date DATE COMMENT '到期日期',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT 'ACTIVE/EXPIRED/REFUNDED',
    benefits TEXT COMMENT '权益内容JSON',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
) COMMENT '会员卡表';

-- 产品表
CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '产品名称',
    spec VARCHAR(100) COMMENT '规格',
    purchase_price DECIMAL(10,2) COMMENT '进价',
    sell_price DECIMAL(10,2) NOT NULL COMMENT '售价',
    stock INT DEFAULT 0 COMMENT '库存',
    warning_stock INT DEFAULT 10 COMMENT '库存预警值',
    store_id BIGINT COMMENT '所属门店ID',
    status INT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
) COMMENT '产品表';

-- 治疗记录表
CREATE TABLE treatment_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    store_id BIGINT NOT NULL COMMENT '门店ID',
    service_item_id BIGINT NOT NULL COMMENT '服务项目ID',
    beautician_id BIGINT COMMENT '美容师ID',
    member_card_id BIGINT COMMENT '使用的会员卡ID',
    content TEXT COMMENT '治疗内容',
    products_used VARCHAR(500) COMMENT '使用产品',
    before_photo VARCHAR(500) COMMENT '术前照片',
    after_photo VARCHAR(500) COMMENT '术后照片',
    feedback TEXT COMMENT '客户反馈',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
) COMMENT '治疗记录表';

-- 消费记录表
CREATE TABLE consume_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    store_id BIGINT NOT NULL COMMENT '门店ID',
    member_card_id BIGINT COMMENT '关联会员卡ID',
    type VARCHAR(20) NOT NULL COMMENT 'SERVICE/PRODUCT/RECHARGE/REFUND',
    amount DECIMAL(10,2) NOT NULL COMMENT '金额',
    pay_method VARCHAR(20) COMMENT 'CASH/WECHAT/ALIPAY/CARD',
    description VARCHAR(500) COMMENT '说明',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
) COMMENT '消费记录表';

-- 客户跟进记录表
CREATE TABLE follow_up (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    store_id BIGINT NOT NULL COMMENT '门店ID',
    staff_id BIGINT NOT NULL COMMENT '跟进员工ID',
    content TEXT COMMENT '跟进内容',
    next_follow_time DATETIME COMMENT '下次跟进时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
) COMMENT '客户跟进记录表';

-- 排班表
CREATE TABLE schedule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    staff_id BIGINT NOT NULL COMMENT '员工ID',
    store_id BIGINT NOT NULL COMMENT '门店ID',
    work_date DATE NOT NULL COMMENT '日期',
    start_time TIME NOT NULL COMMENT '上班时间',
    end_time TIME NOT NULL COMMENT '下班时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0
) COMMENT '排班表';

-- 初始管理员 (密码: admin123, BCrypt加密)
INSERT INTO user (username, password, real_name, role, status) VALUES
('admin', '$2a$10$eXu.ABidpLE8Y2I5IXttJOiELMy0M9MnJtXi/kuQ7EXZIRGWgULVa', '系统管理员', 'ADMIN', 1);

-- 示例门店
INSERT INTO store (name, address, phone) VALUES
('旗舰店', '广州市天河区xx路1号', '020-12345678'),
('天河店', '广州市天河区yy路2号', '020-87654321');
