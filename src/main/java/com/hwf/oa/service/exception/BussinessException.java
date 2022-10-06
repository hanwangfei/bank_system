package com.hwf.oa.service.exception;

/**
 * 业务逻辑异常
 */
public class BussinessException extends RuntimeException{
    private String code;//异常编码
    private String message;  //异常的具体文本信息

    public BussinessException(String code,String msg){
        super(code+" : "+msg);
        this.code=code;
        this.message=msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
