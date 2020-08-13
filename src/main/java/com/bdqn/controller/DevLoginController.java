package com.bdqn.controller;

import com.bdqn.pojo.DevUser;
import com.bdqn.service.DevUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/dev")//请求前缀
public class DevLoginController {

    @Resource
    private DevUserService devUserService;

    @RequestMapping("/login")
    public String devLogin(){
        return "devlogin";
    }
    //登录
    @RequestMapping("/dologin")
    public String dologin(HttpServletRequest request, Model model, @RequestParam String devCode, @RequestParam String devPassword){
        //判空处理
        if(devPassword == "" || devCode == ""){
            request.setAttribute("error","用户名或者密码不能为空");
            model.addAttribute("error","用户名或者密码不能为空");
            return "redirect:/dev/login";
        }
        DevUser devUser = devUserService.doLogin(devCode,devPassword);
        if(devUser == null){
            request.setAttribute("error","用户名或者密码错误");
            model.addAttribute("error","用户名或者密码错误");
            return "redirect:/dev/login";
        }
        request.getSession().setAttribute("devUserSession",devUser);
        return "developer/main";
    }
    //退出
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().removeAttribute("devUserSession");
        return "redirect:/dev/login";
    }
}
