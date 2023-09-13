package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

//自定义异常           统一异常处理
public class CustomException extends RuntimeException {
    private ResultCode resultCode;
    public CustomException(ResultCode resultCode){
        //错误代码+异常信息
        super("错误代码"+resultCode.code()+"异常信息:"+resultCode.message());
        this.resultCode = resultCode;
    }
    public ResultCode getResultCode(){
        return this.resultCode;
    }
}
