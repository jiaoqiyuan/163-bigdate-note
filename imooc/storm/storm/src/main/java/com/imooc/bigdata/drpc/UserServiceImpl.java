package com.imooc.bigdata.drpc;

public class UserServiceImpl implements UserService {
    @Override
    public void addUser(String name, int age) {
        System.out.println("Server invoked: add user sucess..., name is " + name);
    }
}
