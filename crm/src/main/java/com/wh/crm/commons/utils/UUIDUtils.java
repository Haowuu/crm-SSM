package com.wh.crm.commons.utils;

import java.util.UUID;

/**
 * @author wu
 * @createTime 2022/3/5  21:31
 * @description
 */
public class UUIDUtils {

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
