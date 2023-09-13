package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.Response;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/*
* 统一的异常捕获类
* */
@ControllerAdvice  //控制器增强
public class ExceptionCatch {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);
@ExceptionHandler(CustomException.class)            //注解中的类异常被捕获到就会调用下面的方法
    public ResponseResult customException(CustomException customException){
    //记录日志
    LOGGER.error("catch exception:{}",customException.getMessage());
    ResultCode resultCode = customException.getResultCode();
    ResponseResult responseResult = new ResponseResult(resultCode);
//    return new ResponseResult(resultCode);
    return responseResult;
}
}
