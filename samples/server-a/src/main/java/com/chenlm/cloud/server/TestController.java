package com.chenlm.cloud.server;

import com.chenlm.cloud.coloring.async.AsyncDecorators;
import com.chenlm.cloud.coloring.async.HttpHeadersHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class TestController {
    @Autowired
    private ServerBApi serverBApi;
    @Autowired
    private AsyncServerBApi asyncServerBApi;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AsyncRestTemplate asyncRestTemplate;

    @Value("${spring.profiles.active: #{null}}")
    private String value;
    @Value("${spring.application.name}")
    private String name;

    @GetMapping("tttt")
    public String tttt() {
        new Thread(AsyncDecorators.HttpHeaderRunnableDecorator.decorate(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                HttpHeaders httpHeaders = HttpHeadersHolder.getHttpHeaders();
                System.out.println(httpHeaders);
                String test = serverBApi.test();
            }
        })).start();
        return "ok";
    }


    @GetMapping("test")
    public String get() {
        String test = serverBApi.test();
        return name + "-" + value + " -> " + test;
    }

    @GetMapping("async")
    public String async() throws ExecutionException, InterruptedException {
        Future<String> stringFuture = asyncServerBApi.test();
        return name + "-" + value + " -> " + stringFuture.get();
    }

    @GetMapping("rest")
    public String rest() {
        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://server-b/test", String.class);
        return name + "-" + value + " -> " + forEntity.getBody();
    }

    @GetMapping("async-rest")
    public String asyncRest() throws ExecutionException, InterruptedException {
        ListenableFuture<ResponseEntity<String>> forEntity = asyncRestTemplate.getForEntity("http://server-b/test", String.class);
        return name + "-" + value + " -> " + forEntity.get().getBody();
    }
}
