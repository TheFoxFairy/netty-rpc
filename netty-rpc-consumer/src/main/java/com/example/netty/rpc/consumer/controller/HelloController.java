package com.example.netty.rpc.consumer.controller;

import com.example.netty.rpc.api.IUserService;
import com.example.netty.rpc.protocal.annotation.MyRemoteReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @MyRemoteReference
    private IUserService iUserService;


    @RequestMapping("/say")
    public String say(){
        return iUserService.saveUser("胡桃");
    }
}