package com.grouppage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Service
public class ExecService {

    private final AsyncTaskExecutor executor;

    @Autowired
    public ExecService(AsyncTaskExecutor executor) {
        this.executor = executor;
    }

    public <T> Future<T> executeCallable(Callable<T> callable) {
        return executor.submit(callable);
    }
    public void executeRunnable(Runnable runnable){
        executor.execute(runnable);
    }
}
