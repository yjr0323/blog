package com.yjr.controller;


import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.yjr.common.annotation.LogAnnotation;
import com.yjr.common.cache.RedisManager;
import com.yjr.common.constant.Base;
import com.yjr.common.constant.ResultCode;
import com.yjr.common.result.Result;
import com.yjr.entity.User;
import com.yjr.entity.UserLogCheck;
import com.yjr.oauth.OAuthSessionManager;
import com.yjr.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@CrossOrigin(allowCredentials = "true")
public class LoginController {


    @Autowired
    private UserService userService;
    @Autowired
    private RedisManager redisManager;

    //获取验证码
    @GetMapping("/getCheckCode")
    public Result getCheckCode(HttpServletResponse response){

        Result result = new Result();
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(102, 40);
        String code = lineCaptcha.getCode();
        Subject subject = SecurityUtils.getSubject();
        redisManager.set("code",code);
        try {
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            ServletOutputStream outputStream = response.getOutputStream();
            lineCaptcha.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }
        result.setResultCode(ResultCode.SUCCESS);
        return result;
    }

    @PostMapping("/login")
//    @LogAnnotation(module = "登录", operation = "登录")
    public Result login(@RequestBody UserLogCheck user) {
        Result r = new Result();
        String code =(String) redisManager.get("code");
        if (!code.equals(user.getCode())){
            r.setResultCode(ResultCode.ERROR);
            return r;
        }
        executeLogin(user.getAccount(), user.getPassword(),r);
        return r;
    }

    @PostMapping("/register")
    //@RequiresRoles(Base.ROLE_ADMIN)
    @LogAnnotation(module = "注册", operation = "注册")
    public Result register(@RequestBody User user) {

        Result r = new Result();

        User temp = userService.getUserByAccount(user.getAccount());
        if (null != temp) {
            r.setResultCode(ResultCode.USER_HAS_EXISTED);
            return r;
        }

        String account = user.getAccount();
        String password = user.getPassword();

        Long userId = userService.saveUser(user);

        if (userId > 0) {
            executeLogin(account, password, r);
        } else {
            r.setResultCode(ResultCode.USER_Register_ERROR);
        }
        return r;
    }


    private void executeLogin(String account, String password, Result r) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(account, password);

        try {
            subject.login(token);
            User currentUser = userService.getUserByAccount(account);
            subject.getSession().setAttribute(Base.CURRENT_USER, currentUser);

            r.setResultCode(ResultCode.SUCCESS);
            r.simple().put(OAuthSessionManager.OAUTH_TOKEN, subject.getSession().getId());
        } catch (UnknownAccountException e) {
            r.setResultCode(ResultCode.USER_NOT_EXIST);
        } catch (LockedAccountException e) {
            r.setResultCode(ResultCode.USER_ACCOUNT_FORBIDDEN);
        } catch (AuthenticationException e) {
            r.setResultCode(ResultCode.USER_LOGIN_ERROR);
        } catch (Exception e) {
            r.setResultCode(ResultCode.ERROR);
        }

    }

    @RequestMapping(value = "/handleLogin")
    public Result handleLogin(HttpServletRequest request) {
        String id = request.getHeader(OAuthSessionManager.OAUTH_TOKEN);
        System.out.println("超时登录。。。:" + id);
        return Result.error(ResultCode.SESSION_TIME_OUT);
    }


    @GetMapping("/logout")
    @LogAnnotation(module = "退出", operation = "退出")
    public Result logout() {

        Result r = new Result();
        Subject subject = SecurityUtils.getSubject();
        subject.logout();

        r.setResultCode(ResultCode.SUCCESS);
        return r;
    }


}
