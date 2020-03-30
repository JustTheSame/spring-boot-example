package com.lance.qrcode.service;

import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @ProjectName: qrcode
 * @Package: com.example.qrcode.service
 * @ClassName: TestQRCodeService
 * @Author: z003nj4s
 * @Description: ${description}
 * @Date: 1/10/2019 3:18 PM
 * @Version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestQRCodeService {

    @Autowired
    private QRCodeService service;

    @Test
    public void testCreateQRCodeToFilePath() throws IOException, WriterException {
        service.createQRCodeToFilePath();
    }

    @Test
    public void testReadQRCodeToFilePath() throws IOException, WriterException, FormatException, ChecksumException, NotFoundException {
        service.readQRCodeToFilePath();
    }
}
