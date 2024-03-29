package com.example.partnermatchingsystem.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 头像
     */
    private Integer avatarUrl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     *  用户状态0：正常
     */
    private Integer userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 0：普通角色、1：管理员
     */
    private Integer userRole;
    /**
     * 是否删除（逻辑删除：0，1）
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 用户角色，0：普通用户，1：管理员
     */
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 星球编号
     */
    private String planetCode;
}