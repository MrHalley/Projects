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

## Linux

### 使用教程

> 基本命令

```shell
// vim查看行号
# :set nu
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

// 查看日志
# docker logs <container>
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



> docker 安装ElasticSearch和Kibana
```shell
// 1.下载ealastic search和kibana
# docker pull elasticsearch:7.4.2
# docker pull kibana:7.4.2
// 2.配置
# mkdir -p /mydata/elasticsearch/config  #创建目录
# mkdir -p /mydata/elasticsearch/data
# echo "http.host: 0.0.0.0" >/mydata/elasticsearch/config/elasticsearch.yml
# chmod -R 777 /mydata/elasticsearch/ # 将mydata/elasticsearch/文件夹中文件都可读可写

//3.启动Elastic search
# docker run --name elasticsearch -p 9200:9200 -p 9300:9300 \
-e  "discovery.type=single-node" \
-e ES_JAVA_OPTS="-Xms64m -Xmx512m" \
-v /mydata/elasticsearch/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
-v /mydata/elasticsearch/data:/usr/share/elasticsearch/data \
-v  /mydata/elasticsearch/plugins:/usr/share/elasticsearch/plugins \
-d elasticsearch:7.4.2 


//4.设置开机启动elasticsearch
# docker update elasticsearch --restart=always

//5.启动kibana
# docker run --name kibana -e ELASTICSEARCH_HOSTS=http://192.168.56.10:9200 -p 5601:5601 -d kibana:7.4.2

//6.测试 
	--> 查看elasticsearch版本信息： http://192.168.56.10:9200/ 
	--> 访问Kibana： http://192.168.56.10:5601/app/kibana 
```



> docker 安装nginx

``` shell
//1.随便启动一个 nginx 实例，只是为了复制出配置
# docker run -p 80:80 --name nginx -d nginx:1.10

//2.将容器内的配置文件拷贝到当前目录：
# docker container cp nginx:/etc/nginx .

//3.修改文件名称：mv nginx conf 把这个 conf 移动到/mydata/nginx 下

//4.终止原容器：
# docker stop nginx
//5.执行命令删除原容器：
# docker rm $ContainerId
//6.创建新的 nginx；执行以下命令
# docker run -p 80:80 --name nginx \
-v /mydata/nginx/html:/usr/share/nginx/html \
-v /mydata/nginx/logs:/var/log/nginx \
-v /mydata/nginx/conf:/etc/nginx \
-d nginx:1.10
```



## [SpringCloudAlibaba](https://github.com/alibaba/spring-cloud-alibaba/blob/2.2.x/README-zh.md)

### [Nacos 文档](https://nacos.io/zh-cn/docs/quick-start-docker.html)

 [Nacos Discovery](https://github.com/alibaba/spring-cloud-alibaba/blob/2.2.x/spring-cloud-alibaba-examples/nacos-example/nacos-discovery-example/readme-zh.md)

 [Nacos Config](https://github.com/alibaba/spring-cloud-alibaba/blob/2.2.x/spring-cloud-alibaba-examples/nacos-example/nacos-config-example/readme-zh.md)

####  使用教程

```shell
//启动nacos 单机版(windows)
# startup.cmd -m standalone

//nacos 注册中心可视化界面 http://localhost:8848/nacos 默认账号密码 nacos nacos
```
> 配置中心

![配置中心](https://typora-images-repository.oss-cn-beijing.aliyuncs.com/mall/notes/20220529165514.png)

## [跨源资源共享（CORS）](https://developer.mozilla.org/zh-CN/docs/Web/HTTP/CORS)

### OSS对象存储

[Alibaba Cloud OSS Example](https://github.com/alibaba/aliyun-spring-boot/tree/master/aliyun-spring-boot-samples/aliyun-oss-spring-boot-sample)

[oss-sdk](https://help.aliyun.com/document_detail/32011.html)



## Mysql

### 事务

```sql
-- 设置当前会话事务隔离级别
SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
```



## ElasticSearch

> 自定义词库

```shell
//1.安装nginx。创建字典

//2.修改/mydata/elasticsearch/plugins/ik/config/中的 IKAnalyzer.cfg.xml 配置远程拓展字典

//3.更新完成后，es 只会对新增的数据用新词分词。历史数据是不会重新分词的。如果想要历史数据重新分词。需要执行：
# POST my_index/_update_by_query?conflicts=proceed
```
## [Nginx]([nginx](http://nginx.org/en/))



## 性能监控

jvisualvm、jconsole



## 压力测试

JMeter



## Redis

> 缓存问题

| 问题   | 描述                                  | 解决方案                 |
| ---- | ----------------------------------- | -------------------- |
| 缓存穿透 | 访问一个一定不存在的key，导致所有请求访问到DB，失去缓存的意义   | null结果缓存，添加失效时间      |
| 缓存雪崩 | 大量key在同一时间失效，导致大量数据请求访问到DB，DB压力过大雪崩 | 设置随机失效时间，不让大量key一起失效 |
| 缓存击穿 | 热点key失效，导致短时间大量请求直接到DB              | 加锁，只允许一个请求访问数据库      |

