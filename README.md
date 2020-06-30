# DataStore-Api

![build](https://travis-ci.org/chuntaojun/Hbase-Api.svg?branch=master)

#### 如何构建部署的 JAR

进入指定模块下，执行`gradle build`即可，比如`Consumer`，则进入此模块的根目录下`/Consumer`，运行打包命令，然后在`out`目录中查找`jar`包

#### 运行方式

screen java -jar -Dspring.cloud.nacos.discovery.ip={服务器公网ip} -Dspring.profiles.active=prod xxx.jar

#### 目录层次

 - config:相关配置文件
 - service:服务接口与实现
 
   - HBase:HBase服务实现
   - hadoop:hadoop服务实现
   - solr:solr服务实现
   - spark:spark服务实现
 
 - router:对外http访问接口
 
#### 项目文档
 
 [ShowDoc](https://www.showdoc.cc/chuntaojun)

#### 框架

##### 内部框架
- spring-boot web应用框架
- reactor 响应式编程
- disruptor 高性能队列

##### 使用

> router package 作用

`Http`接口提供

```java
POST(StringConst.API + "new/add").and(accept(MediaType.APPLICATION_JSON_UTF8))
                        .and(contentType(MediaType.APPLICATION_JSON_UTF8)),
                        newsHandler::saveNew)
```

- `POST(String url)` ——> http的请求方式以及请求地址
- `and()` ——> 设置请求数据编码以及返回体编码
- 处理的此http请求的方法，method(ServerRequest request)

> handler package 作用

```java
@Override
public Mono<ServerResponse> saveNew(ServerRequest request) {
    // 将请求提转为某一个具体的对象
    return request.bodyToMono(News.class)
    // 数据转换操作
                .map(SimHashAlogUtils::nlp)
                .map(newBean -> Message.buildMessage(MessageUtils.MQ_TOPIC_STORE, newBean))
                .map(message -> {
                    Mono<Message> messageMono = producerService.publish(message);
                    return messageMono.map(ResultData::buildSuccessFromData);
                })
                // 转换数据流
                .flatMap(ResponseRender::render)
                // 以上的操作由那个线程执行
                .subscribeOn(Schedulers.elastic());
    }
```

#### 消息队列的使用

> 如何向消息中心注册一个消息消费者

topic 为消息主题，表示订阅所有消息主题为`topic`的消息，消费者为`ConsumerService`或者`BatchConsumerService`，如果是选择批量消息消费的话，reqNum为每次获取的消息条数，应该实现BatchConsumerService接口

ProducerService.register(String topic, ConsumerService service)

ProducerService.register(String topic, BatchConsumerService batchService, int reqNum)

示例代码
```java
public class HBaseNewsServiceImpl implements BatchConsumerService<List<Message>> {

    @Autowired
    private ProducerService producerService;

    @PostConstruct
    public void init() {
        producerService.register("HBase-Store", this, 10);
    }
}
```

 
> 单条消息消费模式

 - 继承 ConsumerService 接口， 实现 onEvent 方法即可
 
> 批量消息消费模式

 - 继承 BatchConsumerService 接口，实现 onEvent 方法即可
 
示例代码

```java
public class HBaseNewsServiceImpl implements BatchConsumerService<List<Message>> {
    
    @Override
    public void onEvent(List<Message> data) {
        log.info(data.toString());
        for (Message event : data) {
            Mono<ResultData<Boolean>> result = putNews((News) event.getData());
            result.subscribe(booleanResultData -> {
                if (event.getRetryCnt() >= 3) {
                    log.error("[HBase-API Error] 新闻存储失败，已超过重试次数");
                    return;
                }
                if (!booleanResultData.getData()) {
                    event.setRetryCnt(event.getRetryCnt() + 1);
                    producerService.publish(event);
                }
            });
        }
    }
}
```
 
