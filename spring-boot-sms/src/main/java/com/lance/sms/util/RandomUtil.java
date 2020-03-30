package com.lance.sms.util;

import org.apache.commons.lang3.RandomStringUtils;

public final class RandomUtil {

    private static final int CODE_COUNT = 4;

    public static String generateCode() {
        return RandomStringUtils.randomAlphabetic(CODE_COUNT);
    }

}
