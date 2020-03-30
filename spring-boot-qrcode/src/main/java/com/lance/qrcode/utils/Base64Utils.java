package com.lance.qrcode.utils;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

/**
 * @ProjectName: qrcode
 * @Package: com.example.qrcode.utils
 * @ClassName: Base64Utils
 * @Author: z003nj4s
 * @Description: ${description}
 * @Date: 1/10/2019 4:02 PM
 * @Version: 1.0
 * https://www.jianshu.com/p/16bc8c4c03a4
 */
public class Base64Utils {
    public static String encode(ByteArrayOutputStream outputStream) {
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }
}
