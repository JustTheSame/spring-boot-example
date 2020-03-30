package com.lance.qrcode.service;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lance.qrcode.utils.Base64Utils;
import com.lance.qrcode.utils.QRCodeUtils;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;


import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;

/**
 * @Description:
 * @author: zhaotian
 * @date: 2020/3/30
 */
@Service
public class QRCodeService {

//    public String crateQRCode(String content, int width, int height) throws IOException {
//
//        String resultImage = "";
//        if (!StringUtils.isEmpty(content)) {
//            ServletOutputStream stream = null;
//            ByteArrayOutputStream os = new ByteArrayOutputStream();
//            @SuppressWarnings("rawtypes")
//            HashMap<EncodeHintType, Comparable> hints = new HashMap<EncodeHintType, Comparable>();
//            hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); // 指定字符编码为“utf-8”
//            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M); // 指定二维码的纠错等级为中级
//            hints.put(EncodeHintType.MARGIN, 2); // 设置图片的边距
//
//            try {
//                QRCodeWriter writer = new QRCodeWriter();
//                BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
//
//                BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
//                ImageIO.write(bufferedImage, "png", os);
//                /**
//                 * 原生转码前面没有 data:image/png;base64 这些字段，返回给前端是无法被解析，可以让前端加，也可以在下面加上
//                 */
//                resultImage = new String("data:image/png;base64," + Base64.encode(os.toByteArray()));
//
//                return resultImage;
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                if (stream != null) {
//                    stream.flush();
//                    stream.close();
//                }
//            }
//        }
//        return null;
//    }

    public String createBase64QRCode(String content, int width, int height) throws IOException {

        File file = ResourceUtils.getFile("classpath:logo.png");
        String resultImage = "";
        try {
            ByteArrayOutputStream baos = QRCodeUtils.createWithLogo(content, file.getPath(), 300, 300,
                    90, 90, "png");
//            ImageIO.write(bufferedImage.asBufferedImage(), "png", os);

            /**
             * 原生转码前面没有 data:image/png;base64 这些字段，返回给前端是无法被解析，可以让前端加，也可以在下面加上
             */
            resultImage = new String("data:image/png;base64," + Base64Utils.encode(baos));
            return resultImage;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String createBase64QRCodeWithoutLogo(String content, int width, int height) throws IOException {

        String resultImage = "";
        try {
            ByteArrayOutputStream baos = QRCodeUtils.create(content, width, height, "png");
//            ImageIO.write(bufferedImage.asBufferedImage(), "png", os);

            /**
             * 原生转码前面没有 data:image/png;base64 这些字段，返回给前端是无法被解析，可以让前端加，也可以在下面加上
             */
            resultImage = new String("data:image/png;base64," + Base64Utils.encode(baos));
            return resultImage;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public OutputStream createQRCodeWithLogo(String content, int width, int height) throws IOException {

        File file = ResourceUtils.getFile("classpath:logo.png");

        try {
            OutputStream outputStream = QRCodeUtils.createWithLogo(content, file.getPath());
            return outputStream;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;

    }

    public OutputStream createQRCodeWithoutLogo(String content, int width, int height) throws IOException {

        try {
            OutputStream outputStream = QRCodeUtils.create(content, width, height);
            return outputStream;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;

    }

    public void createQRCodeToFilePath() throws IOException, WriterException {
        String filePath = "E:\\code.png";
        QRCodeUtils.create("nihao", filePath);
    }

    public void readQRCodeToFilePath() throws FormatException, ChecksumException, NotFoundException, IOException {
        String filePath = "E:\\code.png";
        String content = QRCodeUtils.read(filePath);
        System.out.println("Content: " + content);
        System.out.println(File.separatorChar);
    }

    public ByteArrayOutputStream create() throws IOException, WriterException {
        return QRCodeUtils.create("high");
    }
}
