package com.example.partnermatchingsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.partnermatchingsystem.common.ErrorCode;
import com.example.partnermatchingsystem.exception.BusinessException;
import com.example.partnermatchingsystem.mapper.UserMapper;
import com.example.partnermatchingsystem.model.User;
import com.example.partnermatchingsystem.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.partnermatchingsystem.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author zyf
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-02-02 16:43:31
*/
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;
    /**
     * 盐值、混淆密码
     */
    private static final String SALT = "zyf";


    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @param planetCode
     * @return 返回用户id
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {

        //1、校验
        if (StringUtils.isAnyBlank(userAccount,userAccount,checkPassword,planetCode)){ //非空
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if(userAccount.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号长度过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码过短");
        }
        if (planetCode.length()>5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球编号过长");
        }

        //账户不包含特殊字符：正则表达式
        String content = "^\\w+$";
        Matcher matcher = Pattern.compile(content).matcher(userAccount);
        if (!matcher.find()){
            return -1;
        }

        //密码和校验密码相同
        if (!userPassword.equals(checkPassword)){
            return -1;
        }

        //账户不重复、这里涉及到查询数据库的操作、而前面只要有一处条件出问题就非法、因此吧查询操作放到校验最后、避免出现性能浪费
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(); //构造查询条件
        queryWrapper.eq("userAccount",userAccount);
        long count = this.count(queryWrapper);  //查询    这里的this是：UserServiceImpl
        if (count > 0){
            return -1;
        }

        queryWrapper = new QueryWrapper<>(); //构造查询条件
        queryWrapper.eq("planetCode",planetCode);
        count = this.count(queryWrapper);  //查询    这里的this是：UserServiceImpl
        if (count > 0){
            return -1;
        }
        //2、加密
        String newPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //3、插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(newPassword);
        user.setPlanetCode(planetCode);
        boolean saveResult = this.save(user);
        if (!saveResult){   //插入失败
            return -1;
        }
        return user.getId();
    }

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 返回脱敏后的用户信息
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1、校验
        if (StringUtils.isAnyBlank(userAccount,userAccount)){ //非空
            return null;
        }
        if(userAccount.length()<4){
            return null;
        }
        if (userPassword.length() < 8 ){
            return null;

        }

        //账户不包含特殊字符：正则表达式
        String content = "^\\w+$";
        Matcher matcher = Pattern.compile(content).matcher(userAccount);
        if (!matcher.find()){
            return null;
        }


        //2、加密:要和数据库加密过的密码进行比较
        String newPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",newPassword);
        User user = userMapper.selectOne(queryWrapper);

        //用户不存在
        if (user == null){
            log.info("user login failed , userAccount can not match userPassword");
            return null;
        }

        //3、用户脱敏、防止数据库字段信息泄露给前端
        User safetyUser = getSafetyUser(user);

        //4、记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE , safetyUser);

        //5、返回脱敏后的用户信息
        return safetyUser;
    }

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser){
        if (originUser == null){
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
        safetyUser.setUserStatus(0);
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUserRole(originUser.getUserRole());
        return safetyUser;
    }

    /**
     * 用户注销
     * @param request
     * @return
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        //移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);

        return 1;
    }
}




