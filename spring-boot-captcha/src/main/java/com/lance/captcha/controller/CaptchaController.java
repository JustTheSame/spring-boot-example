package com.lance.captcha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @Description:
 *
 * @author: zhaotian
 * @date: 2020/3/30
 */
@Controller
public class CaptchaController {


    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.session = request.getSession();
    }

    @RequestMapping(value = "/img")
    public ModelAndView image() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index");
        return mv;
    }


    @GetMapping(value = "image")
    public void authImage() throws IOException {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        // 生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        session.removeAttribute("verCode");
        session.removeAttribute("codeTime");

        session.setAttribute("verCode", verifyCode.toLowerCase());
        session.setAttribute("codeTime", LocalDateTime.now());

        // 生成图片
        int w = 100, h = 30;
        OutputStream out = response.getOutputStream();
        VerifyCodeUtils.outputImage(w, h, out, verifyCode);
    }

    @RequestMapping("valid")
    @ResponseBody
    public void validImage(String code, @SessionAttribute("verCode") Object verCode) {

        if (null == verCode) {
            request.setAttribute("error", "验证码已失效，请重新输入");
//            return WrapMapper.wrap(Wrapper.ERROR_CODE, "验证码已失效，请重新输入");
        }
        String verCodeStr = verCode.toString();
        LocalDateTime localDateTime = (LocalDateTime) session.getAttribute("codeTime");

        long past = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        if (verCodeStr == null || code == null || code.isEmpty() || !verCodeStr.equalsIgnoreCase(code)) {

            request.setAttribute("error", "验证码错误");
            System.out.println("验证码错误");
//            return WrapMapper.wrap(Wrapper.ERROR_CODE, "验证码错误");

        } else if ((now - past) / 1000 / 60 > 2) {//两分钟
            request.setAttribute("error", "验证码已过期，重新获取");
            System.out.println("验证码已过期，重新获取");
//            return WrapMapper.wrap(Wrapper.ERROR_CODE, "验证码已过期，重新获取");
        } else {
            //验证成功，删除存储的验证码
            session.removeAttribute("verCode");
//            return WrapMapper.ok();
        }
    }
}
