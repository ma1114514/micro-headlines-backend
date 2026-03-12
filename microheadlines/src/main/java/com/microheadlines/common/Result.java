package com.microheadlines.common;
public class Result<T>{
    //code反映的是业务状态码，不是浏览器响应码
    //由企业约定俗成前后端共用遵守的约定
    private Integer code;
    //message是一个相应内容，用于解释相应状态码
    private String message;
    //data对象，以json字符串形式在前后端交流，用于携带相应信息
    private T data;

    private Result(){
    }
    public static <T> Result<T> build(T data){
        Result<T>result=new Result<T>();
        if(data !=null)
            result.setData(data);
        return result;
    }

    public static <T> Result<T> build(T body,Integer code,String message){
        Result<T>result=build(body);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> build(T body, com.microheadlines.common.ResultCodeEnum resultCodeEnum){
        Result<T>result=build(body);
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }
    public static <T>Result<T> ok(T data){
        Result<T>result=build(data);
        return build(data, com.microheadlines.common.ResultCodeEnum.SUCCESS);
    }
    public Result<T>message(String msg){
        this.setMessage(msg);
        return this;
    }
    public Result<T>code(Integer code){
        this.setCode(code);
        return this;
    }

    private void setMessage(String message) {
        this.message=message;
    }

    private void setCode(Integer code) {
        this.code=code;
    }

    public String getMessage() {
        return message;
    }
    public Integer getCode(){
        return code;
    }

    private void setData(T data) {
        this.data=data;
    }
    public T getData(){
        return data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
