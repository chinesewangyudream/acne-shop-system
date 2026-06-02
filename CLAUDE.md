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
执行 `backend/src/main/resources/db/init.sql`，创建 `acne_shop` 库、18 张表和 20 个索引。默认管理员: 老板账号 `13650901858 / admin123`

## 技术栈

| 层 | 技术 |
|---|---|
| 后端 | Spring Boot 3.2.5, Java 21, MyBatis-Plus 3.5.6, Spring Security + JWT (jjwt 0.12.5), Hutool 5.8.27 |
| 数据库 | MySQL (端口 3307) + Redis |
| 管理后台 | Vue 3 + Vite + Element Plus + Pinia + Vue Router 4 |
| H5 顾客端 | Vue 3 + Vite + Vant 4 + Pinia + Vue Router 4 |

## 架构要点

### API 路由隔离
- 管理端: `/api/admin/**` — 需 JWT 认证（登录接口除外）
- 顾客端: `/api/h5/**` — 部分公开（登录、查卡、预约），部分需认证
- Controller 按端分包: `controller/admin/` vs `controller/h5/`

### 后端包结构 (`com.acneshop`)
- `common/` — 统一响应封装 (`Result<T>`, `ResultCode` 枚举, `PageResult`)
- `entity/` — 大部分实体继承 `BaseEntity`（id, createTime, updateTime, deleted），例外：`ConsumptionRecord`、`WriteoffRecord` 等记录类不继承 BaseEntity（无逻辑删除和更新时间需求）
- `dto/` — 请求 DTO：`LoginDTO`（管理端：手机号+密码）、`H5LoginDTO`（H5：手机号+验证码+门店ID）
- `vo/` — 响应 VO：`LoginVO`（token, employeeId, name, phone, role, storeId）
- `config/` — SecurityConfig, CorsConfig, RedisConfig, WebMvcConfig(文件上传映射), MybatisPlusConfig(分页插件), MyMetaObjectHandler(createTime/updateTime 自动填充)
- `security/` — JWT 过滤链: `JwtAuthFilter` → `JwtTokenProvider` 生成/解析 token，token 中携带 role 和 storeId
- `exception/` — `BusinessException` + `ResultCode` 枚举，`GlobalExceptionHandler` 统一捕获

### 四级角色体系
| role 值 | 角色名 | Spring Security 角色 | 数据范围 |
|---------|--------|---------------------|---------|
| 1 | 老板 | ROLE_BOSS | 全店（可指定 storeId 筛选） |
| 2 | 店长 | ROLE_MANAGER | 仅本店 |
| 3 | 前台 | ROLE_RECEPTIONIST | 仅本店 |
| 4 | 美容师 | ROLE_BEAUTICIAN | 仅本店 |

数据隔离核心方法：`SecurityUtils.effectiveStoreId(requestStoreId)` — 老板可指定门店，其余角色强制使用自己的 storeId。

### 多门店数据隔离
- 每个实体含 `storeId` 字段
- JWT token 中嵌入 storeId，店长只能访问本店数据，BOSS 可访问全店
- 会员卡、次卡**仅限办卡门店使用**，不可跨店

### 前端请求封装
- 两个前端各有 `utils/request.js`，统一 Axios 拦截：自动带 Bearer token，401 自动跳登录
- 管理后台 token 存 `localStorage.token`，H5 端存 `localStorage.h5_token`
- **管理后台全局 loading**：`ElLoading.service()` 请求计数器模式，请求开始全屏遮罩，全部响应后关闭
- **页面级 v-loading**：每个页面的 `el-table` 添加了 `v-loading="loading"` 指令，`loadData()` 中 try/finally 控制加载状态

### 前端权限控制
- 侧边栏菜单按角色过滤（`layout/index.vue` 的 `allMenuItems.roles` 数组）
- 路由守卫 `beforeEach`：无 token 跳登录，角色不匹配重定向到首个有权限页面
- 新增页面时需同步修改三处：`api/index.js`（API）、`router/index.js`（路由+roles）、`layout/index.vue`（菜单项+roles+图标）

### 数据库约定
- MyBatis-Plus 逻辑删除: `deleted` 字段 (0=正常, 1=删除)，部分记录表（consumption_record, writeoff_record, attendance 等）无此字段
- 主键自增，下划线转驼峰
- 日期格式: `yyyy-MM-dd HH:mm:ss`，时区 Asia/Shanghai
- 索引：init.sql 末尾定义了 P0（12个，覆盖核心分页查询）+ P1（8个，提升筛选场景）索引，联合索引设计遵循 `store_id 在最左 + 排序字段在最右` 原则

### 新增后端模块的标准模式
后端三层均为 MyBatis-Plus 空壳，全靠通用方法：
1. Entity：继承 `BaseEntity` 或自定义字段（参照 `ConsumptionRecord` 模式）
2. Mapper：继承 `BaseMapper<T>`，`@Mapper` 注解
3. Service：接口继承 `IService<T>`，实现类继承 `ServiceImpl<Mapper, T>`
4. Controller：`@RestController` + `@RequestMapping("/admin/xxx")` + `@PreAuthorize("hasAnyRole('BOSS', 'MANAGER', 'RECEPTIONIST')")`
5. 分页查询统一用 `LambdaQueryWrapper` + `SecurityUtils.effectiveStoreId()` 数据隔离 + `orderByDesc(排序字段)`

### 文件上传
- 上传目录：`./uploads/`，通过 `WebMvcConfig` 映射为 `/uploads/**` 静态资源
- SecurityConfig 中 `/uploads/**` 设为 permitAll
