/*
 * Copyright 2019 Enterprise Proxy Authors
 *
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an “AS IS” BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.klaytn.enterpriseproxy.txgw.config;

import com.klaytn.enterpriseproxy.txgw.exception.TxGatewayAsyncExceptionHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;


@Configurable
@EnableAsync
public class TxGatewayAsyncConfig implements AsyncConfigurer {
    /**
     * Executor Pool Size
     * @number
     */
    @Value("${txgw.router.async.poolSize:10}")
    private int poolSize;

    /**
     * Executor Max Pool Size
     * @number
     */
    @Value("${txgw.router.async.maxPoolSize:10}")
    private int maxPoolSize;

    /**
     * Executor Queue Capacity
     * @number
     */
    @Value("${txgw.router.async.queueCapacity:10}")
    private int queueCapacity;

    /**
     * Executor Keep Alive Seconds
     * @number
     */
    @Value("${txgw.router.async.keepAliveSeconds:10}")
    private int keepAliveSeconds;




    /**
     * TX GATEWAY Async initialize
     *
     * @return
     */
    @Bean(name="txGatewayAsyncExecutor")
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor txGatewayAsyncExecutor = new ThreadPoolTaskExecutor();
        txGatewayAsyncExecutor.setCorePoolSize(poolSize);
        txGatewayAsyncExecutor.setMaxPoolSize(maxPoolSize);
        txGatewayAsyncExecutor.setQueueCapacity(queueCapacity);
        txGatewayAsyncExecutor.setKeepAliveSeconds(keepAliveSeconds);
        txGatewayAsyncExecutor.setAllowCoreThreadTimeOut(true);
        txGatewayAsyncExecutor.setWaitForTasksToCompleteOnShutdown(true);
        txGatewayAsyncExecutor.setThreadNamePrefix("TXGATEWAY-ASYNC-");
        txGatewayAsyncExecutor.initialize();
        return txGatewayAsyncExecutor;
    }


    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new TxGatewayAsyncExceptionHandler();
    }
}