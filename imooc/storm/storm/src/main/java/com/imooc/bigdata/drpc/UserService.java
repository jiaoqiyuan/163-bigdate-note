package com.imooc.bigdata.drpc;

/**
 * 用户的服务
 */
public interface UserService {
    public static final long versionID = 88888888;
    /**
     * 添加用户
     */
    public void addUser(String name, int age);
}
