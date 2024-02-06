package com.example.partnermatchingsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.partnermatchingsystem.common.BaseResponse;
import com.example.partnermatchingsystem.common.ErrorCode;
import com.example.partnermatchingsystem.common.ResultUtils;
import com.example.partnermatchingsystem.exception.BusinessException;
import com.example.partnermatchingsystem.model.User;
import com.example.partnermatchingsystem.model.request.UserLoginRequest;
import com.example.partnermatchingsystem.model.request.UserRegisterRequest;
import com.example.partnermatchingsystem.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.partnermatchingsystem.constant.UserConstant.ADMIN_ROLE;
import static com.example.partnermatchingsystem.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 * @author zyf
 * @Data 2024/2/2 - 21:19
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest == null){
            //return ResultUtils.error(ErrorCode.PARAMS_ERROR);
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,planetCode)){
            return null;
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        //return new BaseResponse<>(0,"ok",result);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     * @param userLoginRequest
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if(userLoginRequest == null){
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }
        User result = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(result);
    }

    /**
     * 用户注销
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){
        if(request == null){
            return null;
        }

        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 用户管理：需要鉴权
     */


    /**
     * 用户查询
     * @param username
     * @return
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username,HttpServletRequest request){
        //1、鉴权：仅管理员可查询
        if(!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        //2、查询
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)) {  //判断原因：这里的username不是登录的用户、而是用户要进行查询的对象
            userQueryWrapper.like("username", username); //实现了用户名的模糊查询
        }
        List<User> userList = userService.list(userQueryWrapper);
        //将集合中user对象转换为脱敏过的用户对象、再将脱敏对象重新转换为集合返回
        List<User> result = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(result);
    }

    /**
     * 用户删除
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody Long id,HttpServletRequest request){
        //1、仅管理员可查询
        if(!isAdmin(request)){
            return null;
        }

        //2、删除
        if(id<=0) {
            return null;
        }
        boolean result = userService.removeById(id);
        return ResultUtils.success(result);
    }

    /**
     * 是否为管理员   5 6 7 8 9 10 11 12 13 14 15 16 17 18 19
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request){
        //仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        if (user == null || user.getUserRole()!=ADMIN_ROLE){
            return false;   //没有获取登录态 或 权限为普通人员、查询直接返回空数组
        }
        return true;
    }

    /**
     * 用户登录过后、再进入不用被拦截
     * 获取用户登录态
     * @param request
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);//登录态取到的只是用户的登录凭据
        User currentUser = (User) userObj;
        if (currentUser == null) {  //判断用户是否登录
            return null;
        }
        //根据登录凭据获取到数据库用户信息、再返回脱敏过的用户信息
        Long id = currentUser.getId();
        User user = userService.getById(id);
        User result = userService.getSafetyUser(user);
        return ResultUtils.success(result);
    }



}
