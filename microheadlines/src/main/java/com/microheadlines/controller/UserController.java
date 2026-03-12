package com.microheadlines.controller;


import com.microheadlines.common.Result;
import com.microheadlines.common.ResultCodeEnum;
import com.microheadlines.entity.User;
import com.microheadlines.service.impl.UserServiceImpl;
import com.microheadlines.util.AESUtil;
import com.microheadlines.util.JwtUtil;
import org.apache.el.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("user")


public class UserController {
    @Autowired
    UserServiceImpl userService;
    /**
     * @author：ma
     * @description:后端登录逻辑，本应该写道service层
     * @param：前端的User对象，会由mvc自动由json转为对象
     * @return：
     */
    @PostMapping("login")
    protected Result login(@RequestBody User user){
        //接受用户密码
        //调service方法实现登录
        User byUsername = userService.findByUsername(user.getUsername());
        if(byUsername!=null){
            //登陆成功逻辑
            try {
                if(Objects.equals(AESUtil.decrypt(byUsername.getPassword()), user.getPassword())){
                    String accessToken = JwtUtil.getInstance().generateAccessToken(
                            byUsername.getId().longValue(),
                            byUsername.getUsername(),
                            byUsername.getNickName());
                            byUsername.setPassword("");
                    Map data = new HashMap();
                    data.put("token",accessToken);
                    data.put("userInfo",byUsername);
                    return Result.build(data, ResultCodeEnum.SUCCESS);
                }
            } catch (Exception e) {
                return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
            }
            return Result.build(null,ResultCodeEnum.PASSWORD_ERROR);
        }
        //登录失败逻辑
        //用于测试的对象密码密文plzEdzAY4YQxR4SJZrlKaYO4RlsIa4sII2OqFj5OdZrULg==
        return Result.build(null,ResultCodeEnum.USERNAME_ERROR);
        //响应登录信息，已被springmvc封装
    }
    /**
     * @author：ma
     * @description:通过token获取登录信息，实现记住用户功能
     * @param：自带的token
     * @return：返回的用户信息
     */
    @GetMapping("getInfo")
    public Result getUserInfo(@RequestHeader("token") String token){

        //获取token
        //校验token
        Result result = Result.build(null, ResultCodeEnum.NOTLOGIN);
        //此层token判断是否是合格token
        if(token!=null&&JwtUtil.getInstance().isAccessToken(token)){
            //IF(通过校验)把用户信息放入result对象返回
            //此层if判断token是否过期
            if(!JwtUtil.getInstance().isTokenExpired(token)){
                Long userIdFromToken = JwtUtil.getInstance().getUserIdFromToken(token);
                //此层if判断根据token内含的id是否为空
                if(userIdFromToken!=null){
                    //这里根据此id去查数据库是否有此对象
                    User byUserId = userService.findByUserId(userIdFromToken.toString());
                    if(byUserId!=null){
                        Map data=new HashMap();
                        byUserId.setPassword("");
                        data.put("userInfo",byUserId);
                        return Result.build(data,200,"success");
                    }
                }
            }
        }
        //else返回504null
        return result;
    }
    /**
     * @author：ma
     * @description:检查用户名是否占用
     * @param：前端input的账号
     * @return：给前端的是否占用提示
     */
    @GetMapping("userNameUsed")
    public Result userNameUsed(@RequestParam String userName){
        //获取账号，查找对象
        User byUsername = userService.findByUsername(userName);

        //找到了反。。。找不到反。。。
        if(byUsername!=null){
            return Result.build(null,ResultCodeEnum.USERNAME_USED);
        }
        return Result.ok(null);

    }
    /**
     * @author：ma
     * @description:注册业务
     * @param：前端的注册信息
     * @return：注册响应
     */
    @PostMapping("regist")
    public Result regist(@RequestBody User user){
        //先接受前端信息
        //查后端是否有此用户
        if(userService.findByUsername(user.getUsername())==null){
            int i = userService.addRegistUser(user);
            if(i==1){
                return Result.ok(null);
            }else return Result.build(null,ResultCodeEnum.USERNAME_USED);
        }
        return Result.build(null,ResultCodeEnum.USERNAME_USED);
        //没有再进行添加
        //添加完响应注册成功
        //未成功响应未知
        //占用显示503
    }

}
