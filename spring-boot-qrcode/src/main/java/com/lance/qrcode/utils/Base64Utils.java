package com.lance.qrcode.utils;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

/**
 * @Description:
 * @author: zhaotian
 * @date: 2020/3/30
 */
public class Base64Utils {
    public static String encode(ByteArrayOutputStream outputStream) {
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }
}
