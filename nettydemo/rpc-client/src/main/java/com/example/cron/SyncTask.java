package com.example.cron;

import lombok.SneakyThrows;

import java.util.Date;

public class SyncTask implements Runnable{
    private String name;

    public SyncTask(String name) {
        this.name = name;
    }

    @SneakyThrows
    @Override
    public void run() {
        System.out.println(name + " 动态任务执行 " + new Date());
        Thread.sleep(1000);
    }

    public void run2() {
        System.out.println(name + new Date());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
