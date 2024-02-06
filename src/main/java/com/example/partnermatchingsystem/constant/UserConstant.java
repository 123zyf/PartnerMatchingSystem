package com.example.partnermatchingsystem.constant;

/**
 * 用户常量
 * @author zyf
 * @Data 2024/2/3 - 11:24
 * @Data
 */

//接口属性默认都是public static
public interface UserConstant {
    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "userLoginState";

    /**
     * 角色身份 0:普通用户  1：管理员
     */
    int DEFAULT_ROLE = 0 ;
    int ADMIN_ROLE = 1 ;
}
