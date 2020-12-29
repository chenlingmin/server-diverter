package com.chenlm.cloud.coloring.async;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.http.HttpHeaders;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

import static com.chenlm.cloud.coloring.async.AsyncDecorators.HttpHeaderCallableDecorator;
import static com.chenlm.cloud.coloring.async.AsyncDecorators.HttpHeaderRunnableDecorator;

/**
 * @author Chenlm
 */
public class HttpHeaderInheritableExecutorProxySupport {

    @SuppressWarnings("unchecked")
    public <T> T createProxy(Executor bean) {
        ProxyFactoryBean factory = new ProxyFactoryBean();
        factory.setProxyTargetClass(true);
        factory.addAdvice(new ExecutorMethodInterceptor());
        factory.setTarget(bean);
        return (T) factory.getObject();
    }
}


class ExecutorMethodInterceptor implements MethodInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(ExecutorMethodInterceptor.class);

    @Override
    public Object invoke(MethodInvocation invocation)
            throws Throwable {

        HttpHeaders httpHeaders = HttpHeadersHolder.getHttpHeaders();
        if (null == httpHeaders) {
            logger.debug("非 request 作用域，无法传递 ");
            return invocation.proceed();
        } else if (invocation.getArguments() == null) {
            return invocation.proceed();
        } else {
            Object[] arguments = invocation.getArguments();

            for (int i = 0; i < arguments.length; i++) {
                if (arguments[i] instanceof Runnable) {
                    arguments[i] = HttpHeaderRunnableDecorator.decorate((Runnable) arguments[i]);
                    logger.debug("包装 {}.{} 中的 args[{}], 获得 requestAttributes 线程传递能力", invocation.getThis().getClass().getName(), invocation.getMethod().getName(), i);
                } else if (arguments[i] instanceof Callable) {
                    arguments[i] = HttpHeaderCallableDecorator.decorate((Callable) arguments[i]);
                    logger.debug("包装 {}.{} 中的 args[{}], 获得 requestAttributes 线程传递能力", invocation.getThis().getClass().getName(), invocation.getMethod().getName(), i);
                }
            }
            return invocation.proceed();
        }
    }
}