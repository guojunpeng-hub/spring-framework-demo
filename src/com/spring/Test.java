package com.spring;

import com.service.UserService;

/**
 * @author xiaoguo
 * @date2022/1/20 0020 16:20
 */
public class Test {
    public static void main(String[] args) throws ClassNotFoundException {
        SpringApplicationContext context = new SpringApplicationContext(AppConfig.class);
        UserService userService = (UserService) context.getBean("userService");
        userService.test();
        System.out.println(context.getBean("userService"));
        System.out.println(context.getBean("userService"));
        System.out.println(context.getBean("userService"));
        System.out.println(context.getBean("userService"));
    }
}
