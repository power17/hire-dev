# HireDev - 在线招聘系统

一个基于微服务架构的在线招聘系统，采用 Spring Cloud 技术栈构建。

## 项目结构

- `auth-service-8111`: 认证服务
- `gateway-8000`: API网关
- `hire-api`: 公共API模块
- `hire-common`: 公共组件模块
- `hire-pojo`: 数据传输对象模块
- `service-company-6001`: 公司服务
- `service-file-5001`: 文件服务
- `service-resource-4001`: 资源服务
- `service-user-7001`: 用户服务
- `service-work-3001`: 职位服务

## 技术栈

- Spring Boot
- Spring Cloud
- Maven

## 快速开始

1. 克隆项目
2. 构建项目: `mvn clean install`
3. 启动各微服务

## 配置

- 端口配置:
  - 认证服务: 8111
  - 网关: 8000
  - 公司服务: 6001
  - 文件服务: 5001
  - 资源服务: 4001
  - 用户服务: 7001
  - 职位服务: 3001

## docker命令
nacos地址：http://localhost:8848/nacos
```shell
docker pull nacos/nacos-server:v2.1.0

docker run --name nacos `
-e MODE=standalone `
-e JVM_XMS=128m `
-e JVM_XMX=128m `
-e JVM_XMN=64m `
-e JVM_MS=64m `
-e JVM_MMS=64m `
-p 8848:8848 `
-d nacos/nacos-server:v2.1.1


docker update nacos --restart=always

```
