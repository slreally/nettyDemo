package com.example.service;

import com.alibaba.fastjson.JSON;
import com.example.anno.RpcService;
import com.example.api.IUserService;
import com.example.pojo.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RpcService
public class UserServiceImpl implements IUserService {
    Map<Integer, User> userMap = new HashMap<>();
    @Override
    public User searchUserById(Integer id) {
        System.out.println("收到client请求");
        if (userMap.size() ==0) {
            User user = new User();
            user.setId(1);
            user.setName("zhangsan");
            userMap.put(1, user);
            User user1 = new User();
            user1.setId(2);
            user1.setName("lisi");
            userMap.put(2, user1);
        }
        User user = userMap.get(id);
        System.out.println(JSON.toJSONString(user));
        return user;
    }
}
