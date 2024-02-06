package com.example.partnermatchingsystem.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册请求体
 * @author zyf
 * @Data 2024/2/2 - 21:30
 */
@Data
public class UserLoginRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -4395157730514752411L;
    private String userAccount;
    private String userPassword;
}
