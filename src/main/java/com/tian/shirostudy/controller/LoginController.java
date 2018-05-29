package com.tian.shirostudy.controller;

import com.fasterxml.jackson.databind.deser.Deserializers;
import com.tian.common.other.BusinessException;
import com.tian.common.other.ResponseData;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2018/1/24 0024.
 */
@Controller
public class LoginController extends BaseController{

    @RequestMapping("login")
    public ModelAndView login(String username, String password){
        // 创建subject实例
        Subject currentUser = SecurityUtils.getSubject();
        // 判断当前用户是否登录
        if(currentUser.isAuthenticated() == false){
            //
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            try{
                currentUser.login(token);
            }catch (AuthenticationException e){
                e.printStackTrace();
                currentUser.logout();
                throw new BusinessException(500, "用户名密码错误");
            }
        }
        //
        return new ModelAndView("success");

    }

    @RequestMapping("admin")
    public ModelAndView admin(){
        return new ModelAndView("admin");
    }

    @RequestMapping("user")
    public ModelAndView user(){
        return new ModelAndView("user");
    }

    @RequestMapping("logout2")
    public ModelAndView logout(HttpServletRequest request){
        request.getSession().invalidate();
        return new ModelAndView("login");
    }
}
