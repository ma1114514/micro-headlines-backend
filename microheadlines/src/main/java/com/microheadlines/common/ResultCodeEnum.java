package com.microheadlines.common;

public enum ResultCodeEnum {
    SUCCESS(200,"success"),
    USERNAME_ERROR(501,"usernameError"),
    PASSWORD_ERROR(502,"passwordError"),
    NOTLOGIN(503,"notLogin"),
    USERNAME_USED(504,"usernameUsed"),

    UNKNOWERROR(505,"unKnowError");




    private Integer code;
    private String message;
    private ResultCodeEnum(Integer code,String message){
        this.code=code;
        this.message=message;
    }
    public Integer getCode(){
        return code;
    }
    public String getMessage(){
        return message;
    }
}
