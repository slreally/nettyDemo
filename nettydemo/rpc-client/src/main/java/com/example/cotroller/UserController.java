package com.example.cotroller;

import com.alibaba.fastjson.JSON;
import com.example.anno.RpcReference;
import com.example.api.IUserService;
import com.example.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class UserController {

    @RpcReference
    IUserService userService;

    @GetMapping("/user/{id}")
    public Object get(@PathVariable("id") Integer id) {
        System.out.println("收到web请求" + id);
        User user = userService.searchUserById(id);
        System.out.println(JSON.toJSONString(user));
        return user;
    }
}
