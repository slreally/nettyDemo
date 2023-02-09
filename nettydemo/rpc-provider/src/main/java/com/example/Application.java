package com.example;

import com.example.server.RpcNettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {
    @Autowired
    private RpcNettyServer rpcNettyServer;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("success=======");
    }

    /**
     * 启动netty
     * CommandRunner接口作用：
     * 项目启动后，预加载数据
     * 实现在应用启动后，去执行相关代码逻辑，且只会执行一次；
     * spring batch批量处理框架依赖这些执行器去触发执行任务；
     * 我们可以在run()方法里使用任何依赖，因为它们已经初始化好了
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        rpcNettyServer.start("127.0.0.1", 9001);
    }
}
