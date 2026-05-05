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

## Maven常用命令

```bash
# 构建整个项目
mvn clean install

# 跳过测试构建
mvn clean install -DskipTests

# 构建单个模块（含依赖）
mvn clean install -pl service-user-7001 -am

# 启动服务
mvn spring-boot:run

# 打包
mvn clean package

# 查看依赖树
mvn dependency:tree

# 查看有效配置
mvn help:effective-pom
```

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
192.168.237.128:nacos
nacos地址：http://localhost:8848/nacos
```shell
docker pull nacos/nacos-server:v2.1.0

docker run --name nacos -e MODE=standalone -e JVM_XMS=128m -e JVM_XMX=128m -e JVM_XMN=64m -e JVM_MS=64m -e JVM_MMS=64m -p 8848:8848 -d nacos/nacos-server:v2.1.1 

docker update nacos --restart=always

```
redis
```shell
docker pull redis:6.2.6

mkdir -p /home/redis6/conf
touch /home/redis6/conf/redis.conf


docker run -p 6379:6379 --name redis \
-v /home/redis6/data:/data \
-v /home/redis6/conf/redis.conf:/etc/redis/redis.conf \
-d redis:6.2.7 \
redis-server /etc/redis/redis.conf

docker update redis --restart=always

```
```shell
docker run -p 9000:9000 -p 9090:9090 \
 --name minio \
 -d --restart=always \
 -e "MINIO_ACCESS_KEY=imooc" \
 -e "MINIO_SECRET_KEY=imooc123456" \
 -v /home/minio/data:/data \
 minio/minio server /data --console-address ":9090" -address ":9000"
```
seata
```shell
docker pull seataio/seata-server:1.5.2
docker run --name seata-server -p 8091:8091 -p 7091:7091 -v /home/seata-server/resources:/seata-server/resources -d  seataio/seata-server:1.5.2 
```

192.168.237.129: rabbitmq
账号：guest
密码：guest
```shell
sudo docker run -d \
  --name rabbitmq \
  -p 5671:5671 \
  -p 5672:5672 \
  -p 4369:4369 \
  -p 15671:15671 \
  -p 15672:15672 \
  -p 25672:25672 \
  --restart always \
  -d rabbitmq:management

```

