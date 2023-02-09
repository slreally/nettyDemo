package com.example.cron;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

@RequestMapping
@RestController
public class TaskController {
    private static Map<String, ScheduledFuture<?>> futureMap = new ConcurrentHashMap<>();

    /**
     * 定时任务crontab
     */
    ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();


    @RequestMapping("/addTask/{name}")
    public boolean addTask(@PathVariable("name") String name) {
        add(name);
        return true;
    }

    @RequestMapping("/stopTask/{name}")
    public boolean stopTask(@PathVariable("name") String name) {
        del(name);
        return true;
    }

    @GetMapping("/getAllTask")
    public String getAllTask() {
        return String.join(",", futureMap.keySet());
    }

    private void add(String name) {
        if (futureMap.get(name) != null) {
            return;
        }
        //3秒后执行 每3秒执行一次任务
        //固定频率来执行线程任务，固定频率的含义就是可能设定的固定时间不足以完成线程任务，但是它不管，达到设定的延迟时间了就要执行下一次了
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(new SyncTask(name), 3000, 3000, TimeUnit.MILLISECONDS);
        //固定延迟（时间）来执行线程任务，它实际上是不管线程任务的执行时间的，每次都要把任务执行完成后再延迟固定时间后再执行下一次
        ScheduledFuture<?> scheduledFuture1 = scheduledExecutorService.scheduleWithFixedDelay(new SyncTask(name)::run2, 3000, 3000, TimeUnit.MILLISECONDS);
        futureMap.put(name, scheduledFuture);
        futureMap.put(name+"1", scheduledFuture1);
    }

    private void del(String name) {
        if (futureMap.get(name) == null) {
            return;
        }
        ScheduledFuture<?> scheduledFuture = futureMap.get(name);
        scheduledFuture.cancel(true);
        futureMap.remove(name);
    }

    public void delAll() {
        Iterator<Map.Entry<String, ScheduledFuture<?>>> iterator = futureMap.entrySet().iterator();
        while (iterator.hasNext()) {
            ScheduledFuture<?> scheduledFuture = iterator.next().getValue();
            scheduledFuture.cancel(true);
            iterator.remove();
        }
    }
}
