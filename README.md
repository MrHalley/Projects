# 谷粒商城

## Vagrant

### 使用教程

[使用vagrant安装centos7](https://www.jianshu.com/p/9e0883d6132a)

[Vagrant自动创建Vmware虚拟机](https://blog.csdn.net/qq_33745102/article/details/119904146)

[vagrant官方文档](https://www.bookstack.cn/read/Vagrant/fa4d7ed9b438feed.md#%C2%BB%20Vagrant%20VMware%20Utility%20Service)

[使用XShell连接Vagrant](https://blog.csdn.net/qq_38826019/article/details/114848864)

> 默认账号密码 vagrant vagrant

### 遇见的问题

> Windows+VMWare(暂时使用virtualbox)

```shell
//1.vagrant 启动报错是由于没有启动vagrant-vmware-utility服务。见官方文档（https://www.bookstack.cn/read/Vagrant/fa4d7ed9b438feed.md#%C2%BB%20Vagrant%20VMware%20Utility%20Service）
# vagrant up
Vagrant encountered an unexpected communications error with the
Vagrant VMware Utility driver. Please try to run the command
again. If this error persists, please contact support@hashicorp.com
# net.exe start vagrant-vmware-utility
# vagrant up
Bringing machine 'default' up with 'vmware_desktop' provider...
==> default: Verifying vmnet devices are healthy...
==> default: Fixed port collision for 22 => 2222. Now on port 2200.
==> default: Starting the VMware VM...
==> default: Waiting for the VM to receive an address...
==> default: Forwarding ports...
    default: -- 22 => 2200
==> default: Waiting for machine to boot. This may take a few minutes...
    default: SSH address: 127.0.0.1:2222
    default: SSH username: vagrant
    default: SSH auth method: private key
==> default: Machine booted and ready!

//
```

## Docker

### 使用教程

[安装Docker](https://docs.docker.com/engine/install/centos/)

[镜像加速](https://cr.console.aliyun.com/cn-hangzhou/instances/mirrors)

[镜像仓库Docker Hub](https://hub.docker.com/)

>docker 基本命令

```shell
// 开机自启动docker
# sudo systemctl enable docker

// 启动docker
# systemctl start docker

// 查看docker所有容器
# docker ps -a
```

>docker 安装Mysql

```shell

// 下载镜像
# docker pull mysql:5.7

// 检查当前所有镜像
# docker images
REPOSITORY   TAG       IMAGE ID       CREATED        SIZE
mysql        5.7       c20987f18b13   4 months ago   448MB

// 创建实例并启动
-p 3306:3306将容器的端口映射到主机的3306端口
--name mysql 命名容器名称 
-v /mydata/mysql/log:/var/log/mysql 将日志文件挂载到主机
-v /mydata/mysql/data:/var/lib/mysql 将数据文件挂载到主机
-v /mydata/mysql/conf:/etc/mysql 将配置文件夹挂载到主机
-e MYSQL_ROOT_PASSWORD=root\ 配置mysql密码
-d mysql:5.7 以mysql 5.7镜像启动
# docker run -p 3306:3306 --name mysql \
-v /mydata/mysql/log:/var/log/mysql \
-v /mydata/mysql/data:/var/lib/mysql \
-v /mydata/mysql/conf:/etc/mysql \
-e MYSQL_ROOT_PASSWORD=root \
-d mysql:5.7

//查看docker中正在运行的容器
# docker ps
CONTAINER ID   IMAGE       COMMAND                  CREATED         STATUS         PORTS                                                  NAMES
69966a5614e2   mysql:5.7   "docker-entrypoint.s…"   4 minutes ago   Up 5 seconds   0.0.0.0:3306->3306/tcp, :::3306->3306/tcp, 33060/tcp   mysql

//进入docker容器
# docker exec -it mysql /bin/bash

//启动mysql
# docker start mysql

//重启mysql
# docker restart mysql

//启动docker自启动mysql
# docker update mysql --restart=always
```



> docker 安装 redis

```shell
# docker pull redis
# mkdir -p /mydata/redis/conf
//创建空 redis.conf文件
# touch /mydata/redis/conf/redis.conf
# docker run -p 6379:6379 --name redis -v /mydata/redis/data:/data \
-v /mydata/redis/conf/redis.conf:/etc/redis/redis.conf \
-d redis redis-server /etc/redis/redis.conf
//docker执行redis镜像 redis-cli 命令连接
# docker exec -it redis redis-cli

//启动docker自启动redis
# docker update redis --restart=always
```

## [SpringCloudAlibaba](https://github.com/alibaba/spring-cloud-alibaba/blob/2.2.x/README-zh.md)

### Nacos

 [Nacos Discovery](https://github.com/alibaba/spring-cloud-alibaba/blob/2.2.x/spring-cloud-alibaba-examples/nacos-example/nacos-discovery-example/readme-zh.md)

 [Nacos Config](https://github.com/alibaba/spring-cloud-alibaba/blob/2.2.x/spring-cloud-alibaba-examples/nacos-example/nacos-config-example/readme-zh.md)

####  使用教程

```shell
//启动nacos 单机版(windows)
# startup.cmd -m standalone

//nacos 注册中心可视化界面 http://localhost:8848/nacos 默认账号密码 nacos nacos
```
> 配置中心

![配置中心](https://typora-images-repository.oss-cn-beijing.aliyuncs.com/mall/20220529165514.png)
