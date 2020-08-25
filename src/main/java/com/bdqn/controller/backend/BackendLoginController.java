package com.bdqn.controller.backend;

import com.bdqn.pojo.BackendUser;
import com.bdqn.service.backend.BackendUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/manager")
public class BackendLoginController {

    @Resource
    private BackendUserService backendUserService;

    @RequestMapping("/login")
    public String login(){
        return "backendlogin";
    }
    @RequestMapping("/dologin")
    public String doLogin(HttpServletRequest request, @RequestParam String userCode, @RequestParam String userPassword){
        BackendUser backendUser = backendUserService.doLogin(userCode,userPassword);
        if(backendUser == null){
            request.setAttribute("error","用户名或密码错误");
            return "backendlogin";
        }
        request.getSession().setAttribute("userSession",backendUser);
        return "backend/main";
    }
}
