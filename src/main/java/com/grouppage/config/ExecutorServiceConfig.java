package com.grouppage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorServiceConfig {

    @Bean
    public ExecutorService cachedThreadPool(){
        return Executors.newCachedThreadPool();
    }

//    @Bean
//    public AsyncTaskExecutor taskExecutor(){
//        return new ThreadPoolTaskExecutor();
//    }
}
