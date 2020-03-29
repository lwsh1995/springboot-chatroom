package com.springboot.chatroom.controller.common;

import com.springboot.chatroom.model.vo.ResponseJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局错误统一处理
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    private static final ResponseJson ERROR;

    static {
        ERROR=new ResponseJson(HttpStatus.INTERNAL_SERVER_ERROR)
                .setMsg("系统出错，请稍候再试");
    }

    /**
     * 默认异常提示
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseJson defaultErrorHandler(Exception e){
        log.error(e.getMessage(),e);
        return ERROR;
    }

    /**
     * 参数不合法异常提示
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseJson securityExceptionHandler(Exception e){
        return new ResponseJson(HttpStatus.INTERNAL_SERVER_ERROR)
                .setMsg(e.getMessage());
    }

    /**
     * 表单数据格式不正确异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseJson illegalParamExceptionHandler(MethodArgumentNotValidException e){
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        String tips="参数不合法";
        ResponseJson resp = new ResponseJson(HttpStatus.BAD_REQUEST);
        if (!errors.isEmpty()){
            List<Object> list = errors.stream()
                    .map(error -> error.getField() + error.getDefaultMessage())
                    .collect(Collectors.toList());
            resp.put("details",list);
        }
        resp.setMsg(tips);
        return resp;
    }

    /**
     * 表单数据缺失异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseJson servletRequestParameterExceptionHandler(MissingServletRequestParameterException e){
        return new ResponseJson(HttpStatus.BAD_REQUEST)
                .setMsg(e.getMessage());
    }

    /**
     * 请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseJson methodNotSupportedException(HttpRequestMethodNotSupportedException e){
        String supportedMethods = e.getSupportedHttpMethods().stream()
                .map(method -> method.toString())
                .collect(Collectors.joining("/"));
        String msg="请求方法不合法，请使用 "+supportedMethods;
        return new ResponseJson(HttpStatus.METHOD_NOT_ALLOWED).setMsg(msg);
    }

    /**
     * 数据绑定失败异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseJson validationBindException(BindException e){
        String errors = e.getFieldErrors().stream()
                .map(error -> error.getField() + error.getDefaultMessage())
                .collect(Collectors.joining(","));
        return new ResponseJson(HttpStatus.BAD_REQUEST).setMsg(errors);
    }


}
