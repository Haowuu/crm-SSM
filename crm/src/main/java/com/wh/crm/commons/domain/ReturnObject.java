package com.wh.crm.commons.domain;

/**
 * @author wu
 * @createTime 2022/3/4  1:41
 * @description
 */
public class ReturnObject {

    private String code;//成功或失败的标记，1：成功，0：失败
    private String massage;//返回的提示信息
    private Object returnData;//其他数据

    public ReturnObject() {
    }

    public ReturnObject(String code, String massage, Object returnData) {
        this.code = code;
        this.massage = massage;
        this.returnData = returnData;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public Object getReturnData() {
        return returnData;
    }

    public void setReturnData(Object returnData) {
        this.returnData = returnData;
    }
}
