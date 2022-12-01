package com.example.netty.rpc.provider.service;

import com.example.netty.rpc.api.IUserService;
import com.example.netty.rpc.protocal.annotation.MyRemoteService;

@MyRemoteService
public class UserServiceImpl implements IUserService {

    @Override
    public String saveUser(String name) {
        return name;
    }
}
