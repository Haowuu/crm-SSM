package com.wh.crm.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wu
 * @createTime 2022/3/4  1:27
 * @description
 */
public class DateUtils {

    /*
    * 输出年-月-日 时-分-秒 的方法
    * */
    public static String formatDateTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = simpleDateFormat.format(date);
        return strDate;
    }

    /*
     * 输出年-月-日的方法
     * */
    public static String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = simpleDateFormat.format(date);
        return strDate;
    }

    /*
     * 输出时-分-秒 的方法
     * */
    public static String formatTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String strDate = simpleDateFormat.format(date);
        return strDate;
    }
}
