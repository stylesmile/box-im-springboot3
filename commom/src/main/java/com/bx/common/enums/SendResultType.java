package com.bx.common.enums;


public enum SendResultType {

    SUCCESS(0,"发送成功"),
    FAIL(1,"发送失败");

    private int code;
    private String msg;

    // 构造方法
    SendResultType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
