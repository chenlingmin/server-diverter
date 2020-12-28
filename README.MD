# 服务分流组件

本组件参考了 [ribbon-discovery-filter-spring-cloud-starter](https://github.com/jmnarloch/ribbon-discovery-filter-spring-cloud-starter) 的实现，实现了基于 spring-cloud-netflix 应用服务按照标签区分，根据流量的标签，进行服务的智能选择的效果，以达到灰度发布的效果。

## 用法

### 快速开始

在 zuul 网关、以及需要灰度的应用服务添加依赖 `spring-boot-netflix-server-diverter-starter` 

```xml
<dependency>
    <groupId>com.chenlm.cloud</groupId>
    <artifactId>spring-boot-netflix-server-diverter-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

不希望使用 spring-boot-starter 自动配置配置的功能，也可以依赖 `netflix-server-diverter`，使用 `@EnableServerDiverter` 注解。

```xml
<dependency>
    <groupId>com.chenlm.cloud</groupId>
    <artifactId>netflix-server-diverter</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
``` 

再配置 `eureka.client.instance.matedata-map.env` 给服务注册上标签，未配置默认会取 `spring.profiles.active` 的值，这样服务就获得了根据请求头路由不同标签服务的能力，在 `samples` 的样例项目中，使用了默认的根据请求头 `x-env` 选择服务的策略，运行结果如下：


```
curl -H x-env:gray localhost/server-a/test
server-a-gray -> server-b-gray%

curl -H x-env:prod localhost/server-a/test
server-a-prod -> server-b-prod%

curl -H x-env:gray localhost/server-a/async                                                                                                                                                                                                             
server-a-gray -> server-b-gray%

curl -H x-env:prod localhost/server-a/async                                                                                                                                                                                                             
server-a-prod -> server-b-prod%
```

当然，你也可以自定义服务策略，实现 `ServerSelector` 接口，将其注册成 `Bean`，目前已有的实现是 `SimplerHeaderServerSelector`。

```
/**
 * 服务选择器
 * 用户可以自定义多个服务选择器，应该注意选择器之间的顺序
 */
public interface ServerSelector {
    /**
     * 表示该选择器是否支持此类请求
     */
    boolean support(HttpServletRequest request);

    /**
     * 选择标签服务
     */
    String select(HttpServletRequest request);
}

public class SimpleHeaderServerSelector implements ServerSelector{
    private final String headerName;
    public SimpleHeaderServerSelector(String headerName) {this.headerName = headerName;}
    @Override
    public boolean support(HttpServletRequest request) {
        return request.getHeader(headerName) != null;
    }
    @Override
    public String select(HttpServletRequest request) {
        return request.getHeader(headerName);
    }
}

```


例如配置从请求头 `x-hello`、或 `x-world` 中获取环境信息，如下配置。

```
@Configuration
public class TestConfiguration {
    @Bean
    public ServerSelector serverSelector1() {
        return new SimpleHeaderServerSelector("x-hello");
    }
    @Bean
    public ServerSelector serverSelector2() {
        return new SimpleHeaderServerSelector("x-world");
    }
}
```


### 配置项
   
* `server.diverter.enabled` 是否启用服务分流，默认 `true` 
* `server.diverter.server-mark-name` 服务注册的 metadataMap 的 key，默认为 `env` 
* `server.diverter.http-header-name` 流量染色的http请求头，默认为 `x-env` 
 

## 原理

### 服务自动打上标签

服务启动时，会自动向 eurake 注册的内容中，添加 metadataMap.env 标签，标签值取 `spring.profiles.active` 的值。

如果需要自定义，直接在服务中配置 `eureka.client.instance.matedata-map.env` 即可。

注意：上述的标签名称，以 `server.diverter.server-mark-name` 配置为准，默认为 `env`。


### 按照标签选择服务

如图，基本原理是通过请求头区分流量，可用与灰度与生产的流量切分

![](http://www.plantuml.com/plantuml/svg/XP1FIyD04CNlyocUUB0BAVZ7W_OWYFqM8cNMZhMutIoRj135xwwJQcOZ0SaXp9ltVYyJEbdBopOAKgxNiyWxSSZot7BS187EuERuFhs-khzTBZ54DfcxVo8tv_fWrFB08SouWeS3pnz_i4_pEx-GU5YePLJnrtza3dwr49sXUTl6n9bt2D_9jak2EhUlzVGgrQ7JWOZwoz_t--28x0vUDP8Fal510D2RVhNLXKElQhUfbuu-2uAV87WZ6C2F03Jy8_sc2aMfhKoiK399WMNOqBOoiKBH_GK0)

### 标签不存在时选择服务

当流量标签所在的服务不存在时，则退化到原来的负载路由策略。下图中，a_server_2 宕机，prod 流量能正常走 zuul -> a_server_1 -> b_server1 路径；而 gray 流量由于 a_server_2 宕机，所以 zuul 先访问 a_server_1，再访问到 b_server_2 中，即 zuul -> a_server_1 -> b_server_2。

![](http://www.plantuml.com/plantuml/svg/XP11IiD058RtxnHFt3IG5gqkD8k2MBn4AULDywp1wKoOf9KeWXVm0fx40xdn5kPrx4nQ0YMBCF_ytvy__rdEZuLXB9tD1uRMdA3ZPKrcfHdbZ5Jvz_drrTtjT8gmHrQEcXF8pOKy4SdjYpc1ECwXM-gUj1vGYWeJfE7ilyLyE54Pcsqcbk1Vjju7Aho_jOzc4ZSVJL1bFz-VlnzV1-MFettR42qMWEgvC2KvMgDkkRef6QshdspVHMbr8mS0sEtvkjde123-AYqgTbjs-0Q8w4VVQulB14Nr8mLrdC1x9q7r9AYOi1Ebk1T308GffLEAr474tdyLgIYcsFqSXCcFu778xOV5y0S0)

### 异步调用流量染色

* 默认处理了 Spring 管理的 `Executor`，使其获得了父子线程传递 `RequestAttributes` 的能力。
* 目前实现了 `RestTemplate`、`AsyncRestTemplate`、`Feign` 在 Spring 管理的线程池上流量染色的能力。 
* 手动 `new Thread` 中，无法获取 `RequestAttributes`，会导致流量染色无法传递，需要使用 `AsyncDecorators.runnable` 对任务进行包装，使其获得传递 `RequestAttributes` 的能力， 例如：
  
```java
new Thread(AsyncDecorators.runnbale.decorate(new Runnable(){
    RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
    Assert.notNull(requestAttributes);
})).start();
```

### 异步线程无上下文，使用服务自身标签进行传递

异步线程无上下文，形成的原因可能是：
* request 作用域未传递到子线程中，可能是使用了非 Spring 管理的线程池，或者手动 `new Thread()` 未进行包装；
* 定时任务、MQ 等。

在这种情况下，会使用服务自身标签进行传递，也就是会选择与自身标签一样的服务，如图  a_server_1 选择 b_server_1， a_server_2 选择 b_server_2

![](http://www.plantuml.com/plantuml/svg/ZP9FYzD06CRlxwTunKi3QR7gGNEGYaMltbNAf9dMucGI9aabYb1KA0XL4Fz0yL0FWYW5mOEoT7U_J9kcts9dckqasGtRJGvXtloUTvvdtca58H5Xv3Auuj3UW5FqXUVKE76fWO9sGyBxZA9Q8mcoxzMRD-xURhTHI_g2n7iajwwfDvSutdE-HoAeKp9rZp1M8oRQQ22fFpfryQH-nxOe7reAgUPDT8F-EOsiQJo8UVTXKIBfXgORabujNYVp7-cVD-jFFw4K_AojBAcsfP1ANjlIcafR3xH4syhe2snr9N-V38eQGHqfGDjQ_fziHbE0uDc0-qGG5vcKXB5FqHFXKw_gX--nM2q0O7ixNaGHCm3omP5EGCMG2dm9A9yjjAjrcWPkgofyNhrkmAj21xkYWvrto1lOfGQWJrHvCh7OddiXHKPxeON9rrdoztFw_j_gmvVbOhOPVLnFt-9kFEPZlZmybYUOZd-b1x-JRvFLk-_9RBoU7sacIrwrK_EIEodzlfqK0m3bkUj89XR3G_dIQZivfQorA7SYbvq0)


