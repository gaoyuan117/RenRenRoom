/*
 * PayConstants     2016/12/2 17:11
 * Copyright (c) 2016 Koterwong All right reserved
 */
package com.justwayward.renren;

/**
 * Created by Koterwong on 2016/12/2 17:11
 */
public interface Constants {
    interface AliPay {
        /**
         * 支付宝AppID
         */
        String APPID = "2017072607900238";

        /**
         * 支付宝回调后台的NotifyUrl
         */
        String NOTIFY_URL = AppConfig.BaseUrl + "/pay/alipayNotify.php";

        /**
         * 商户私钥，pkcs8格式
         */

        String RSA_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDN4j94ayu/aS7wTiHhXUSxEszWd9gcLRYkWjQ1x+G67/+vPaksUsNTtODTXdog3dsKlpY+oY+PgfrZfN79NxgAyN6ZScPcJiESZ6rGtRw0vOEHmQJqSi2gyGM8vem0hzcMnbnjZ9h4yliiBEryiSFlXoO4h2/Qe9KOfF+64PwTT6m+GaXE5v5JFmvXGPzLi0LMLiPsRNNnL2NtMZ5bTThSJqmx+Y2P2eVJdkhRi2CXviJmP1aaRHuhAaQgI/d1UY9kZsckssKef02ZSoSTr/gP+hh767GUMF/HTYPZn6EOAiJjpdRg3ZUVpCzlVGrD6OG+CNOCwCkDkYnhavmtlNe9AgMBAAECggEBAK8egl4qyktFbGd6DddjPcpf81t5FJjiwLKrBz23hok7GoA0MnFA9GN+tr0SVzcdffLDo4giY7ntFTFoxUMcWY5RTQWVeMMIq5i8FMZvxk4VWlLcEvHVpnnb+Izx/JdRa8GPZ03GRpp3gmxMunMRsRZIDkIm8TSkHbSUldifBcplsT1NbfPGzYjaPeZaoJATfvdtSmN4pAcs8u/ECOkzKYE6gtgGVf4wABLLMmyQ3DqZMHXUpiO9MYTTBtb9b/fj/tLZFDAZmWj1IG3UtWv7/shKI9d08O5mKkD8q/7v5p/WGDNLS+GzR62/VXA6csBSgfn2U2BneWVkNYsSBSpIuQECgYEA8MI5I0vzAS2Q9VJ9LVSABeNUOJDUxGd46sN5gpb9gw1c2jjbNZQd0QYyYcpBHKvlqHy8gTRGAzXK2Z9/3nPLzTKiZSxJMIYoayGzetX0dn7h/E0cFB/xbjUGMxhnu5ZKDcIIF/2EAkm947mS6g51XLn2UWRxczqrEftYoM5SKkMCgYEA2urWBiQQMtJynxNWtWeFcAApRKFgJxnLEormSWjr/CfRujnid7SUaJqBmURDK8SCdI1+AKONzaUYK5htsQJQA8eN1ELbRapZMV6oN5a1bnFPdRW24z5adVwE+l0ou/QPaPEap1A/0BKzbIQmvik6yI8hJIyMm5E24Jv4aHdx1f8CgYBbeD8DFQDfCrPPMKbUSQ1Wr1a5IvVjJTjAYMHShpEMcFA9rGpEQeDOID+V/aSdQwW+7J0lrXmIELDf24RWPMJKVJl0PM4VvHKwuR4aZh05w90R4S3wy24Lg+O2F+iZUXETJwuyKaQ0xlV5pnCVsiaOsyxNIg8kSz7hVxAGED0RRwKBgG82ylAIVipbe1uRKYWy97k0e8Kf8QcsNVxQMDmvEePiHsvqyEBj/tk9VQWWBkHf7R5lG1QbwthsPOE46ND6CchBwq3I4Th9GB7apb7H05gt/F3RED5V2QKyIt1GIoGJtaJsikvhmumL9pb0c/I7Y9+rxvyYYn5NwYFN+16m/6/lAoGBAMDNCuWGwzwwoiWvtb0Xbwy5zF7O4jsAItZi0rmAJd/yqw10i9EZpwZvB2qGgNvio+D27mqtYQ18tRsg6SHETM33d578giJYUVT3axipkwikGn401iD2FITr+76FshWOCKW37/QTgKku7eYP6/1ep8v/RU/P20TLqqxUyHFtV5+m";
    }

    interface WxPay {
        /**
         * 微信 AppID，在微信开放平台创建应用，并开通支付能力
         */
        String APP_ID = "wxd3e33762e921be58";

        /**
         * 商户号
         */
        String WX_SHOP_NUM = "1447967202";
        /**
         * 微信应用密钥
         */
        String WX_API_KEUSTORE = "juzhenbaojuzhenbaojuzhenbaojuzhe";

        String NOTIFY_URL = AppConfig.BaseUrl + "pay/wxpayNotify.php";
    }
}
