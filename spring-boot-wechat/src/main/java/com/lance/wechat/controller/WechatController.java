package com.lance.wechat.controller;

import com.lance.qrcode.service.QRCodeService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutImageMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

@Controller
@RequestMapping("/wechat")
@Slf4j
public class WechatController {

    @Autowired
    private WxMpService wxMpService;

    public static Map<String, String> mediaId = new HashMap<>();

    @GetMapping("template")
    @ResponseBody
    public void template() throws WxErrorException {
        WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
        templateMessage.setTemplateId("");
        templateMessage.setToUser("");
        templateMessage.setUrl("http://www.baidu.com");
        List<WxMpTemplateData> datas = new ArrayList<>();
//        {{first.DATA}}
//        患者姓名：{{keyword1.DATA}}
//        医生姓名：{{keyword2.DATA}}
//        申请时间：{{keyword3.DATA}}
//        {{remark.DATA}}
        WxMpTemplateData first = new WxMpTemplateData();
        first.setName("first");
        first.setValue("您提交的患者报到已通过医生审核。");
        WxMpTemplateData word1 = new WxMpTemplateData();
        word1.setName("keyword1");
        word1.setValue("Jane");
        WxMpTemplateData word2 = new WxMpTemplateData();
        word2.setName("keyword2");
        word2.setValue("Perl");
        WxMpTemplateData word3 = new WxMpTemplateData();
        word3.setName("keyword3");
        word3.setValue("2019-02-01 14:24:23");
        WxMpTemplateData remark = new WxMpTemplateData();
        remark.setName("remark");
        remark.setValue("您可以体验随访服务，随时跟医生沟通。医生可能会对您的病情进行诊后随访！");
        datas.add(first);
        datas.add(word1);
        datas.add(word2);
        datas.add(word3);
        datas.add(remark);
        templateMessage.setData(datas);
        wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
    }

    @GetMapping("menu")
    @ResponseBody
    public WxMenu wxMenu() throws WxErrorException {
        WxMenu menu = new WxMenu();
        WxMenuButton button1 = new WxMenuButton();
        button1.setType(WxConsts.MenuButtonType.CLICK);
        button1.setName("生成二维码");
        button1.setKey("QR_CODE");
        WxMenuButton button2 = new WxMenuButton();
        button2.setName("菜单");
        WxMenuButton button21 = new WxMenuButton();
        button21.setType(WxConsts.MenuButtonType.VIEW);
        button21.setName("图片");
        button21.setUrl("http://industrial.natapp1.cc/wechat/authorize?returnUrl=http://industrial.natapp1.cc/wechat/image");
        WxMenuButton button22 = new WxMenuButton();
        button22.setType(WxConsts.MenuButtonType.VIEW);
        button22.setName("简历");
        button22.setUrl("http://industrial.natapp1.cc/wechat/authorize?returnUrl=http://industrial.natapp1.cc/wechat/resume");
        button2.getSubButtons().add(button21);
        button2.getSubButtons().add(button22);
        WxMenuButton button3 = new WxMenuButton();
        button3.setName("手机绑定");
        WxMenuButton button31 = new WxMenuButton();
        button31.setType(WxConsts.MenuButtonType.CLICK);
        button31.setName("绑定手机");
        button31.setKey("BIND_KEY");
        WxMenuButton button32 = new WxMenuButton();
        button32.setType(WxConsts.MenuButtonType.CLICK);
        button32.setName("解绑手机");
        button32.setKey("UNBIND_KEY");
        button3.getSubButtons().add(button31);
        button3.getSubButtons().add(button32);

//        menu.getButtons().add(button2);
        menu.getButtons().add(button3);
        menu.getButtons().add(button1);
        wxMpService.getMenuService().menuCreate(menu);
        return menu;
    }

    @GetMapping("/show")
    @ResponseBody
    public WxMpMenu menu() throws WxErrorException {
        WxMpMenu wxMenu = wxMpService.getMenuService().menuGet();
        return wxMenu;
    }

    @GetMapping("mpuser")
    @ResponseBody
    public WxMpUser mpUser() throws WxErrorException {
        String lang = "zh_CN";
        String openid = "";
        WxMpUser user = wxMpService.getUserService().userInfo(openid, lang);

        return user;
    }

    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl) throws UnsupportedEncodingException {
        String url = "http://industrial.natapp1.cc/wechat/userInfo";
        String redirectURL = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_USERINFO, URLEncoder.encode(returnUrl, "UTF-8"));
        log.info("【微信网页授权】获取code,redirectURL={}", redirectURL);
        return "redirect:" + redirectURL;
    }

    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl) throws Exception {
        log.info("【微信网页授权】code={}", code);
        log.info("【微信网页授权】state={}", returnUrl);
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken;
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            log.info("【微信网页授权】{}", e);
            throw new Exception(e.getError().getErrorMsg());
        }
        String openId = wxMpOAuth2AccessToken.getOpenId();
        log.info("【微信网页授权】openId={}", openId);
        return "redirect:" + returnUrl + "?openid=" + openId;
    }

    @GetMapping("resume")
    public ModelAndView resume(@RequestParam(value = "openid", required = false) String openid) {

        ModelAndView mv = new ModelAndView();
        mv.setViewName("index");
        return mv;

    }

    @GetMapping("build")
    public ModelAndView build() {

        ModelAndView mv = new ModelAndView();
        mv.setViewName("build");
        return mv;

    }

    @GetMapping("love")
    public ModelAndView love() {

        ModelAndView mv = new ModelAndView();
        mv.setViewName("love");
        return mv;

    }

    @GetMapping("image")
    public ModelAndView image(@RequestParam(value = "openid", required = false) String openid) {

        ModelAndView mv = new ModelAndView();
        String base64Img = "http://industrial.natapp1.cc/EtretatSunrise_ZH-CN10891175350_1920x1080.jpg";
        mv.setViewName("image");
        mv.addObject("openid", openid);
        mv.addObject("base64Img", base64Img);
        return mv;

    }

    @GetMapping("qrcode")
    public void page(@RequestParam(value = "openid", required = false) String openid, HttpServletRequest request, HttpServletResponse response) throws IOException {

        log.info("qrcode~");
        Long returnTime = Calendar.getInstance().getTimeInMillis() / 1000;

        WxMpXmlMessage message = WxMpXmlMessage.fromXml(request.getInputStream());

        if (message.getMsgType().equals(WxConsts.EventType.CLICK)) {


            WxMpXmlOutImageMessage wxMpXmlOutImageMessage = WxMpXmlOutImageMessage.IMAGE().toUser(message.getFromUser()).fromUser(message.getToUser()).mediaId("oCZ5JiwxiOijsori0Bjx6VnQnP_WxSxhv_Rwvo9i_E4_1SDDYVJHAu1shUDzOipV").build();

            response.getWriter().write(wxMpXmlOutImageMessage.toXml());

        }

    }

    @GetMapping("/check")
    public void getWxResult(HttpServletRequest request, HttpServletResponse response) {
        // 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
        String signature = request.getParameter("signature");
        log.info("signature: " + signature);
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        log.info("timestamp: " + timestamp);
        // 随机数
        String nonce = request.getParameter("nonce");
        log.info("nonce: " + nonce);
        // 随机字符串
        String echostr = request.getParameter("echostr");
        log.info("echostr: " + echostr);

        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，否则接入失败
        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            log.error("不合法");
        }
        PrintWriter o = null;
        try {
            o = new PrintWriter(response.getWriter());
            o.print(echostr);
        } catch (IOException e) {
            log.error("写回微信端错误{}", e.getMessage());
        } finally {
            o.close();
        }

    }


    /**
     * 接收公众号的各种信息
     *
     * @param request
     * @return
     */
    @PostMapping("/check")
    @ResponseBody
    public void postWxResult(HttpServletRequest request, HttpServletResponse response) throws IOException, WxErrorException {

        log.info("hi~");
        Long returnTime = Calendar.getInstance().getTimeInMillis() / 1000;

        WxMpXmlMessage message = WxMpXmlMessage.fromXml(request.getInputStream());

        if (message.getMsgType().equals(WxConsts.XmlMsgType.EVENT)) {
            log.info("event~");

            //判断event
            if (message.getEvent().equals(WxConsts.EventType.SUBSCRIBE)) {
                //do something
                String openId = message.getFromUser();
                log.info("user openId: " + openId);
            }

            String openId = message.getFromUser();

            if (message.getEvent().equals(WxConsts.EventType.CLICK)) {
                if (message.getEventKey().equals("BIND_KEY")) {

//                    WxMpXmlOutTextMessage wxMpXmlOutTextMessage = WxMpXmlOutMessage.TEXT().toUser(message.getFromUser()).fromUser(message.getToUser()).content("请输入绑定的手机号").build();
//                    response.setCharacterEncoding("UTF-8");
//                    response.getWriter().write(wxMpXmlOutTextMessage.toXml());
                    WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
                    templateMessage.setTemplateId("");
                    templateMessage.setToUser(openId);
                    templateMessage.setUrl("http://industrial.natapp1.cc/wechat/build");
                    List<WxMpTemplateData> datas = new ArrayList<WxMpTemplateData>();
//        {{first.DATA}}
//        患者姓名：{{keyword1.DATA}}
//        医生姓名：{{keyword2.DATA}}
//        申请时间：{{keyword3.DATA}}
//        {{remark.DATA}}
                    WxMpTemplateData first = new WxMpTemplateData();
                    first.setName("first");
                    first.setValue("您提交的患者报到已通过医生审核。");
                    WxMpTemplateData word1 = new WxMpTemplateData();
                    word1.setName("keyword1");
                    word1.setValue("Jane");
                    WxMpTemplateData word2 = new WxMpTemplateData();
                    word2.setName("keyword2");
                    word2.setValue("God");
                    WxMpTemplateData word3 = new WxMpTemplateData();
                    word3.setName("keyword3");
                    word3.setValue("2019-02-01 14:24:23");
                    WxMpTemplateData remark = new WxMpTemplateData();
                    remark.setName("remark");
                    remark.setValue("您可以体验随访服务，随时跟医生沟通。医生可能会对您的病情进行诊后随访！");
                    datas.add(first);
                    datas.add(word1);
                    datas.add(word2);
                    datas.add(word3);
                    datas.add(remark);
                    templateMessage.setData(datas);
                    wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
                } else if (message.getEventKey().equals("UNBIND_KEY")) {

                    WxMpXmlOutTextMessage wxMpXmlOutTextMessage = WxMpXmlOutMessage.TEXT().toUser(message.getFromUser()).fromUser(message.getToUser()).content("解绑手机号已经完成").build();
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(wxMpXmlOutTextMessage.toXml());
                } else {
                    if (mediaId.get(openId) == null) {
                        QRCodeService qrCodeService = new QRCodeService();
                        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) qrCodeService.createQRCodeWithoutLogo(openId, 150, 150);

                        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                        WxMediaUploadResult res = wxMpService.getMaterialService().mediaUpload(WxConsts.MediaFileType.IMAGE, "png", inputStream);
// 或者

                        mediaId.put(openId, res.getMediaId());
                        log.info(res.getType());
                        log.info(String.valueOf(res.getCreatedAt()));
                        log.info(res.getMediaId());
                        log.info("mediaId size: " + mediaId.size());
                    }

                    log.info("click + user openId: " + openId);
                    log.info("click + key: " + message.getEventKey());
                    WxMpXmlOutImageMessage wxMpXmlOutImageMessage = WxMpXmlOutImageMessage.IMAGE().toUser(message.getFromUser()).fromUser(message.getToUser()).mediaId(mediaId.get(openId)).build();

                    response.getWriter().write(wxMpXmlOutImageMessage.toXml());

                }


            }

        }
        if (message.getMsgType().equals(WxConsts.XmlMsgType.TEXT)) {
            log.info("text~");
            // 文本消息
            log.info("开发者微信号：" + message.getToUser());
            log.info("发送方帐号：" + message.getFromUser());
            log.info("消息创建时间：" + new Date(message.getCreateTime() * 1000L));
            log.info("消息内容：" + message.getContent());
            log.info("消息Id：" + message.getMsgId());
            WxMpXmlOutTextMessage wxMpXmlOutTextMessage = WxMpXmlOutMessage.TEXT().toUser(message.getFromUser()).fromUser(message.getToUser()).content(message.getContent()).build();

            WxMpXmlOutImageMessage wxMpXmlOutImageMessage = WxMpXmlOutImageMessage.IMAGE().toUser(message.getFromUser()).fromUser(message.getToUser()).mediaId("oCZ5JiwxiOijsori0Bjx6VnQnP_WxSxhv_Rwvo9i_E4_1SDDYVJHAu1shUDzOipV").build();

//            StringBuffer str = new StringBuffer();
//            str.append("<xml>");
//            str.append("<ToUserName><![CDATA[" + message.getFromUser() + "]]></ToUserName>");
//            str.append("<FromUserName><![CDATA[" + message.getToUser() + "]]></FromUserName>");
//            str.append("<CreateTime>" + returnTime + "</CreateTime>");
//            str.append("<MsgType><![CDATA[" + message.getMsgType() + "]]></MsgType>");
//            str.append("<Content><![CDATA[" + message.getContent() + "]]></Content>");
//            str.append("</xml>");
//            System.out.println(str.toString());
//            response.getWriter().write(str.toString());
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(wxMpXmlOutTextMessage.toXml());
//            return String.valueOf(str);
        }
//        return null;
    }
}

