package com.lance.qrcode.controller;

import com.google.zxing.WriterException;
import com.lance.qrcode.service.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @Description:
 * @author: zhaotian
 * @date: 2020/3/30
 */
@Controller
@RequestMapping("/qrcode")
public class QRCodeController {

    @Autowired
    private QRCodeService qrCodeService;

    @RequestMapping(value = "/getQRCode")
    public ModelAndView getQRCode() throws IOException {
        ModelAndView mv = new ModelAndView();
        String base64Img = qrCodeService.createBase64QRCodeWithoutLogo("http://www.baidu.com", 600, 600);
        System.out.println(base64Img);
        mv.setViewName("index");
        mv.addObject("base64Img", base64Img);
        return mv;
    }

    @RequestMapping("download")
    public void downloadFileAction(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding(request.getCharacterEncoding());
        response.setContentType("application/octet-stream");
        FileInputStream fis = null;
        try {
            try {
                ByteArrayOutputStream baos = qrCodeService.create();
                response.setHeader("Content-Disposition", "attachment; filename=qr.png");
                ServletOutputStream out = response.getOutputStream();
                baos.writeTo(out);
                response.flushBuffer();
            } catch (WriterException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
