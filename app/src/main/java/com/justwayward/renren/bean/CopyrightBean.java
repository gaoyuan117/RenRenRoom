package com.justwayward.renren.bean;

import java.util.List;

/**
 * Created by gaoyuan on 2018/3/13.
 */

public class CopyrightBean {

    /**
     * code : 200
     * message : 操作成功
     * data : ["1号书","人人集团","济宁集团","聚真宝"]
     */

    private int code;
    private String message;
    private List<String> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
