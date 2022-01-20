package com.service;

import com.spring.annotation.AutoWired;
import com.spring.annotation.Component;
import com.spring.annotation.Scope;

/**
 * @author xiaoguo
 * @date2022/1/20 0020 16:22
 */
@Component("userService")
@Scope(value = "prototy")
public class UserService {

    @AutoWired
    private OrderService orderService;

    public void test(){
        System.out.println("userService 的 test 方法");
    }

}
