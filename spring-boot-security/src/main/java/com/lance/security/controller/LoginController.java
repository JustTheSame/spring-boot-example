package com.lance.security.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {


    @RequestMapping("/v1/login")
    public String userLogin1() {
        return "login-page1";
    }

    @RequestMapping("/v2/login")
    public String userLogin2() {
        return "login-page2";
    }

    @RequestMapping("/login-error")
    public String loginError() {
        return "login-error";
    }

    @RequestMapping("/login-success")
    public String loginSuccess() {
        return "login-success";
    }

    @PostMapping("/login")
    public ModelAndView login(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("login-success");
        mv.addObject("username", request.getParameter("username"));
        mv.addObject("password", request.getParameter("password"));
        return mv;
    }
}
