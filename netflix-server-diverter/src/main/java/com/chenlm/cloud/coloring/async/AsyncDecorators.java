package com.chenlm.cloud.coloring.async;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Callable;

/**
 * @author Chenlm
 */
public final class AsyncDecorators {

    public static final Decorator<Runnable> runnable = runnable -> {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        return () -> {
            try {
                RequestContextHolder.setRequestAttributes(requestAttributes);
                runnable.run();
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        };
    };


    public static final Decorator<Callable> callable = callable -> {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        return () -> {
            try {
                RequestContextHolder.setRequestAttributes(requestAttributes);
                return callable.call();
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        };
    };
}
