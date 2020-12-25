package com.chenlm.cloud.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@Component
public class AsyncServerBApi {

    private static final Logger logger = LoggerFactory.getLogger(AsyncServerBApi.class);

    @Autowired
    private ServerBApi serverBApi;

    @Async
    public Future<String> test() {
        logger.info("hello");
        return new AsyncResult<String>(serverBApi.test());
    }
}
