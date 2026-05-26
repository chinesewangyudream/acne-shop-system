# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

祛痘店铺管理系统，三端分离架构：Spring Boot 后端 + Vue3 管理后台 + Vue3 H5 顾客扫码页。

## 开发命令

### 后端 (backend/)
```bash
mvn spring-boot:run          # 启动后端 (端口 8080)
mvn compile                   # 编译检查
mvn test                      # 运行测试
```

### 管理后台 (admin-web/)
```bash
npm run dev                   # 启动开发服务器 (端口 3000)
npm run build                 # 生产构建
```

### H5 顾客端 (h5-web/)
```bash
npm run dev                   # 启动开发服务器 (端口 3001)
npm run build                 # 生产构建
```

### 数据库初始化
执行 `backend/src/main/resources/db/init.sql`，创建 `acne_shop` 库和 11 张表。默认管理员: `admin / admin123`

## 技术栈

| 层 | 技术 |
|---|---|
| 后端 | Spring Boot 3.2.5, Java 17+, MyBatis-Plus 3.5.6, Spring Security + JWT (jjwt 0.12.5) |
| 数据库 | MySQL + Redis |
| 管理后台 | Vue 3 + Vite + Element Plus + Pinia + Vue Router 4 |
| H5 顾客端 | Vue 3 + Vite + Vant 4 + Pinia + Vue Router 4 |

## 架构要点

### API 路由隔离
- 管理端: `/api/admin/**` — 需 JWT 认证（登录接口除外）
- 顾客端: `/api/h5/**` — 部分公开（登录、查卡、预约），部分需认证
- Controller 按端分包: `controller/admin/` vs `controller/h5/`

### 后端包结构 (`com.acneshop`)
- `common/` — 统一响应封装 (`Result<T>`, `ResultCode` 枚举, `PageResult`)
- `entity/` — 所有实体继承 `BaseEntity`（id, createTime, updateTime, deleted 逻辑删除字段）
- `service/` + `service/impl/` — 接口继承 `IService<T>`，实现类继承 `ServiceImpl<Mapper, T>`
- `security/` — JWT 过滤链: `JwtAuthFilter` → `JwtTokenProvider` 生成/解析 token，token 中携带 role 和 storeId
- `config/SecurityConfig` — H5 登录/查卡/预约接口 permitAll，其余需认证
- `exception/` — `BusinessException` + `ResultCode` 枚举，`GlobalExceptionHandler` 统一捕获

### 多门店数据隔离
- 每个实体含 `storeId` 字段
- JWT token 中嵌入 storeId，店长只能访问本店数据，ADMIN 可访问全店
- 会员卡、次卡**仅限办卡门店使用**，不可跨店

### 前端请求封装
- 两个前端各有 `utils/request.js`，统一 Axios 拦截：自动带 Bearer token，401 自动跳登录
- 管理后台 token 存 `localStorage.token`，H5 端存 `localStorage.h5_token`

### 数据库约定
- MyBatis-Plus 逻辑删除: `deleted` 字段 (0=正常, 1=删除)
- 主键自增，下划线转驼峰
- 日期格式: `yyyy-MM-dd HH:mm:ss`，时区 Asia/Shanghai
