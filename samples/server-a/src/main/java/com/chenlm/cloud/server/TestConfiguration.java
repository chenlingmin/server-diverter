package com.chenlm.cloud.server;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TestConfiguration {



    @Bean
    @LoadBalanced
    public RestTemplate restTemplate () {
        return new RestTemplate();
    }

    @Bean
    @LoadBalanced
    public AsyncRestTemplate asyncRestTemplate() {
        return new AsyncRestTemplate();
    }


    //    @Bean
    public AsyncListenableTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //线程名字前缀
        executor.setThreadNamePrefix("MyExecutor-");
        // setRejectedExecutionHandler：当pool已经达到max size的时候，如何处理新任务
        // CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.initialize();
        return executor;
    }
////
//    @Bean
////    @ConditionalOnMissingBean
//    public AsyncConfigurer asyncConfigurer() {
//        return new AsyncConfigurer() {
//            @Override
//            public Executor getAsyncExecutor() {
//                ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//                //线程名字前缀
//                executor.setThreadNamePrefix("YouExecutor-");
//                // setRejectedExecutionHandler：当pool已经达到max size的时候，如何处理新任务
//                // CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
//                executor.initialize();
//                return executor;            }
//
//            @Override
//            public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
//                return new SimpleAsyncUncaughtExceptionHandler();
//            }
//        };
//    }
}
