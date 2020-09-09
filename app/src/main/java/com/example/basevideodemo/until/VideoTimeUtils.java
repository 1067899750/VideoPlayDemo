package com.example.basevideodemo.until;
/**
 *
 * @description
 * @author puyantao
 * @date 2020/9/9 11:37
 */
public class VideoTimeUtils {
    public static String getTime(long m) {
        if (m < 60 && m > 0) {//秒
            return NumFormat(0) + ":" + NumFormat(m);
        }
        if (m < 3600) {//分
            return NumFormat(m / 60) + ":" + NumFormat(m % 60);
        }
        if (m < 3600 * 24) {//时
            return NumFormat(m / 60 / 60) + ":" + NumFormat(m / 60 % 60) + ":" + NumFormat(m % 60);
        }
        if (m >= 3600 * 24) {//天
            return NumFormat(m / 60 / 60 / 24) + "天" + NumFormat(m / 60 / 60 % 24) + ":" + NumFormat(m / 60 % 60) + ":" + NumFormat(m % 60);
        }
        return "--";
    }


    private static String NumFormat(long i) {
        if (String.valueOf(i).length() < 2) {
            return "0" + i;
        } else {
            return String.valueOf(i);
        }
    }

}
