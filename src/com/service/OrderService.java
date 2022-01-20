package com.service;

import com.spring.annotation.Component;
import com.spring.annotation.Scope;

/**
 * @author xiaoguo
 * @date2022/1/20 0020 16:22
 */
@Component("orderService")
@Scope(value = "prototy")
public class OrderService {

    public void test(){
        System.out.println("orderService 的 test 方法");
    }

}
