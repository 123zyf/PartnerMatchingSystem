package com.example.partnermatchingsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.partnermatchingsystem.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
* @author zyf
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2024-02-02 16:43:31
* @Entity com.example.partnermatchingsystem.model.User.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




