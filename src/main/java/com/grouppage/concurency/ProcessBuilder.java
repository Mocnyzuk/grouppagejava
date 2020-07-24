package com.grouppage.concurency;

import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;

public class ProcessBuilder {
    private CountDownLatch latch;
    private Class<?> aClass;
    private Object object;
    private Method method;
    private Object[] params;


    public Process build(){
        if(latch == null || aClass == null || object == null || method == null || params == null) return null;
        return new Process(latch, aClass, object, method, params);
    }

    public ProcessBuilder(){

    }
    public static ProcessBuilder create(){
        return new ProcessBuilder();
    }
    public ProcessBuilder withLatch(CountDownLatch latch){
        this.latch = latch;
        return this;
    }
    public ProcessBuilder withClass(Class<?> aClass){
        this.aClass = aClass;
        return this;
    }
    public ProcessBuilder withObject(Object object){
        this.object = object;
        return this;
    }
    public ProcessBuilder withMethod(String method){
        try {
            this.method = this.object.getClass().getMethod(method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return this;
    }
    public ProcessBuilder withParams(Object ... params){
        this.params = params;
        return this;
    }



}
