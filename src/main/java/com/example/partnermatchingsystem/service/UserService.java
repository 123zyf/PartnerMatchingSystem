package com.example.partnermatchingsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.partnermatchingsystem.model.User;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author zyf
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-02-02 16:43:31
*/
public interface UserService extends IService<User> {


    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @param planetCode
     * @return 新用户id
     */
    long userRegister(String userAccount , String userPassword , String checkPassword, String planetCode);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 返回脱敏后的用户信息
     */
    User userLogin(String userAccount , String userPassword , HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);

    /**
     * 用户注销
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);
}
