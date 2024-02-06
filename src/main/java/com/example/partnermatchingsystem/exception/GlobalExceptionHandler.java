package com.example.partnermatchingsystem.exception;

import com.example.partnermatchingsystem.common.BaseResponse;
import com.example.partnermatchingsystem.common.ErrorCode;
import com.example.partnermatchingsystem.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * @author zyf
 * @Data 2024/2/4 - 22:27
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)//这个方法只捕获这个异常类里的异常
    public BaseResponse businessException(BusinessException e){
        log.error("businessException:"+e.getMessage(),e);
        return ResultUtils.error(e.getCode(),e.getMessage(),e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e){
        log.error("runtimeException",e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
    }
}
