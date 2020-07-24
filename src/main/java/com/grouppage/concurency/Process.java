package com.grouppage.concurency;

import com.grouppage.domain.entity.Group;
import lombok.Data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
@Data
public class Process<T> implements Runnable{
    private CountDownLatch latch;
    private Value<T> value;

    private Object object;
    private Method method;
    private Object[] params;

    public Process(CountDownLatch latch, Class<T> tClass, Object object, Method method, Object[] params) {
        this.latch = latch;
        this.object = object;
        this.method = method;
        this.params = params;
        this.value = new Value<T>(tClass);
    }

    @Override
    public void run(){
        try {
            this.value.setData((T) method.invoke(object, params));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        latch.countDown();
    }

    public Value<T> getValue() {
        return value;
    }

    public Thread getThread(){
        return new Thread(this);
    }

}
