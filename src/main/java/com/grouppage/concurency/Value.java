package com.grouppage.concurency;

import lombok.Data;

public class Value<T> {
   private Class<T> aClass;
   private T data;

    public Value(Class<T> aClass) {
        this.aClass = aClass;
    }

    public Class<T> getaClass() {
        return aClass;
    }

    public void setaClass(Class<T> aClass) {
        this.aClass = aClass;
    }

    public T getData() {
        return aClass.cast(data);
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Value{" +
                "aClass=" + aClass +
                ", data=" + data +
                '}';
    }
}
