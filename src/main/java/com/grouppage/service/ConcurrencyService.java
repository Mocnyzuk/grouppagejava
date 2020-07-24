package com.grouppage.service;

import com.grouppage.concurency.Process;
import com.grouppage.concurency.Value;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class ConcurrencyService {
    private Runnable[] processes;

    public ConcurrencyService(Runnable... processes ) {
        this.processes = processes;
        this.run();

    }
    public boolean processTasks(boolean isVoid){
        return isVoid && this.checkIfFinished();
    }
    public List<Value> processTasks(){
        return this.getValuesFromTask();
    }

    private void run(){
        Arrays.stream(this.processes)
                .map(Thread::new)
                .forEach(Thread::start);
    }

    private boolean checkIfFinished(){
        try {
            for (Runnable process : this.processes) {
                if(process instanceof Process){
                    return ((Process) process).getLatch().await(3, TimeUnit.SECONDS);
                }
            }
            return true;
        } catch (InterruptedException e) {
            return false;
        }

    }
    private List<Value> getValuesFromTask(){
        try {
            return Arrays.stream(this.processes)
                    .filter(p -> p instanceof Process)
                    .map(p -> ((Process) p).getValue())
                    .peek(System.out::println)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }
}
