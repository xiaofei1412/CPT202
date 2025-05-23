package cn.tablego.project.springboot.common.model;

import org.springframework.http.HttpStatus;
import cn.hutool.core.exceptions.ExceptionUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 响应参数
 * 
 * @author bianj
 * @version 1.0.0 2025-04-23
 */
@ApiModel(description = "响应参数")
public class Result<T> extends OverrideBeanMethods {
    /** 版本号 */
    private static final long serialVersionUID = 8856956965424828815L;

    /** 请求成功的默认消息 */
    private static final String OK_MSG = "请求成功";

    /** 请求失败的默认消息 */
    private static final String FAILED_MSG = "请求失败";

    /** 请求是否成功的标识 */
    @ApiModelProperty(value = "请求是否成功的标识")
    private boolean flag = true;

    /** 响应成功或失败的编码 */
    @ApiModelProperty(value = "响应成功或失败的编码")
    private Integer code = HttpStatus.OK.value();

    /** 响应的提示消息 */
    @ApiModelProperty(value = "响应的提示消息")
    private String msg = OK_MSG;

    /** 响应的异常消息 */
    @ApiModelProperty(value = "响应的异常消息")
    private String exceptionMsg;

    /** 响应数据 */
    @ApiModelProperty(value = "响应数据")
    private T data;

    /**
     * 创建响应参数实例对象
     *
     * @return 响应参数
     */
    public static <T> Result<T> newInstance() {
        return new Result<>();
    }

    /**
     * 返回成功的响应参数
     *
     * @return 成功的响应参数
     */
    public static <T> Result<T> ok() {
        return buildOkResult();
    }

    /**
     * 返回成功的响应参数
     *
     * @param data 响应数据
     * @return 成功的响应参数
     */
    public static <T> Result<T> ok(T data) {
        return buildOkResult(data);
    }

    /**
     * 返回成功的响应参数
     *
     * @param data 响应数据
     * @param msg  成功消息
     * @return 成功的响应参数
     */
    public static <T> Result<T> ok(T data, String msg) {
        return buildOkResult(data, msg);
    }

    /**
     * 返回成功的响应参数
     *
     * @param msg 成功消息
     * @return 成功的响应参数
     */
    public static <T> Result<T> okMsg(String msg) {
        return buildOkResult(null, msg);
    }

    /**
     * 返回失败的响应参数
     *
     * @return 失败的响应参数
     */
    public static <T> Result<T> failed() {
        return buildFailedResult();
    }

    /**
     * 返回失败的响应参数
     *
     * @param data 响应数据
     * @return 失败的响应参数
     */
    public static <T> Result<T> failed(T data) {
        return buildFailedResult(data);
    }

    /**
     * 返回失败的响应参数
     *
     * @param msg 失败消息
     * @return 失败的响应参数
     */
    public static <T> Result<T> failedMsg(String msg) {
        return buildFailedResult(msg);
    }

    /**
     * 返回失败的响应参数
     *
     * @param data 响应数据
     * @param msg  失败消息
     * @return 失败的响应参数
     */
    public static <T> Result<T> failed(T data, String msg) {
        return buildFailedResult(data, msg);
    }

    /**
     * 返回异常的响应参数
     *
     * @param msg 错误消息
     * @return 异常的响应参数
     */
    public static <T> Result<T> error(String msg) {
        return buildErrorResult(msg);
    }

    /**
     * 返回异常的响应参数
     *
     * @param msg       错误消息
     * @param throwable 异常对象
     * @return 异常的响应参数
     */
    public static <T> Result<T> error(String msg, Throwable throwable) {
        return buildErrorResult(msg, throwable);
    }

    /**
     * 返回成功或失败的响应参数
     *
     * @param isOk 是否成功
     * @return 响应参数
     */
    public static <T> Result<T> okOrFailed(boolean isOk) {
        if (isOk) {
            return Result.ok();
        } else {
            return Result.failed();
        }
    }

    /**
     * 创建成功的响应参数
     *
     * @return 响应参数
     */
    private static <T> Result<T> buildOkResult() {
        Result<T> result = Result.newInstance();
        return result.setFlag(true).setCode(HttpStatus.OK.value());
    }

    /**
     * 创建成功的响应参数
     *
     * @param data 响应数据
     * @return 响应参数
     */
    private static <T> Result<T> buildOkResult(T data) {
        Result<T> result = Result.newInstance();
        return result.setFlag(true).setCode(HttpStatus.OK.value()).setData(data);
    }

    /**
     * 创建成功的响应参数
     *
     * @param data 响应数据
     * @param msg  成功消息
     * @return 响应参数
     */
    private static <T> Result<T> buildOkResult(T data, String msg) {
        Result<T> result = Result.newInstance();
        return result.setFlag(true).setCode(HttpStatus.OK.value()).setData(data).setMsg(msg);
    }

    /**
     * 创建失败的响应参数
     *
     * @return 响应参数
     */
    private static <T> Result<T> buildFailedResult() {
        Result<T> result = Result.newInstance();
        return result.setFlag(false).setMsg(FAILED_MSG).setCode(HttpStatus.PRECONDITION_FAILED.value());
    }

    /**
     * 创建失败的响应参数
     *
     * @param data 响应数据
     * @return 响应参数
     */
    private static <T> Result<T> buildFailedResult(T data) {
        Result<T> result = Result.newInstance();
        return result.setFlag(false).setMsg(FAILED_MSG).setCode(HttpStatus.PRECONDITION_FAILED.value()).setData(data);
    }

    /**
     * 创建失败的响应参数
     *
     * @param msg 失败消息
     * @return 响应参数
     */
    private static <T> Result<T> buildFailedResult(String msg) {
        Result<T> result = Result.newInstance();
        return result.setFlag(false).setCode(HttpStatus.PRECONDITION_FAILED.value()).setMsg(msg);
    }

    /**
     * 创建失败的响应参数
     *
     * @param data 响应数据
     * @param msg  失败消息
     * @return 响应参数
     */
    private static <T> Result<T> buildFailedResult(T data, String msg) {
        Result<T> result = Result.newInstance();
        return result.setFlag(false).setCode(HttpStatus.PRECONDITION_FAILED.value()).setData(data).setMsg(msg);
    }

    /**
     * 创建异常的响应参数
     *
     * @param msg 错误消息
     * @return 响应参数
     */
    private static <T> Result<T> buildErrorResult(String msg) {
        Result<T> result = Result.newInstance();
        return result.setFlag(false).setCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).setMsg(msg);
    }

    /**
     * 创建异常的响应参数
     *
     * @param msg       错误消息
     * @param throwable 异常对象
     * @return 响应参数
     */
    private static <T> Result<T> buildErrorResult(String msg, Throwable throwable) {
        Result<T> result = Result.newInstance();
        return result.setFlag(false).setCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).setMsg(msg).setExceptionMsg(throwable);
    }

    /**
     * 设置成功的响应参数
     */
    public void setOk() {
        setOk(null, OK_MSG);
    }

    /**
     * 设置成功的响应参数
     *
     * @param data 响应数据
     */
    public void setOk(T data) {
        setOk(data, OK_MSG);
    }

    /**
     * 设置成功的响应参数
     *
     * @param msg 成功消息
     */
    public void setOkMsg(String msg) {
        setOk(null, msg);
    }

    /**
     * 设置成功的响应参数
     *
     * @param data 响应数据
     * @param msg  成功消息
     */
    public void setOk(T data, String msg) {
        this.setFlag(true).setCode(HttpStatus.OK.value()).setData(data).setMsg(msg);
    }

    /**
     * 设置失败的响应参数
     */
    public void setFailed() {
        setFailed(null, FAILED_MSG);
    }

    /**
     * 设置失败的响应参数
     *
     * @param data 响应数据
     */
    public void setFailed(T data) {
        setFailed(data, FAILED_MSG);
    }

    /**
     * 设置失败的响应参数
     *
     * @param msg 成功消息
     */
    public void setFailedMsg(String msg) {
        setFailed(null, msg);
    }

    /**
     * 设置失败的响应参数
     *
     * @param data 响应数据
     * @param msg  成功消息
     */
    public void setFailed(T data, String msg) {
        this.setFlag(false).setCode(HttpStatus.PRECONDITION_FAILED.value()).setData(data).setMsg(msg);
    }

    /**
     * 设置异常信息
     *
     * @param throwable 异常对象
     */
    public Result<T> setExceptionMsg(Throwable throwable) {
        if (throwable != null) {
            this.exceptionMsg = ExceptionUtil.stacktraceToOneLineString(throwable);
        }
        return this;
    }

    /**
     * 获取请求是否成功的标识
     *
     * @return 请求是否成功的标识
     */
    public boolean getFlag() {
        return flag;
    }

    /**
     * 设置请求是否成功的标识
     *
     * @param flag 请求是否成功的标识
     * @return 响应参数
     */
    public Result<T> setFlag(boolean flag) {
        this.flag = flag;
        return this;
    }

    /**
     * 获取响应成功或错误的编码
     *
     * @return 响应成功或错误的编码
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 设置响应成功或错误的编码
     *
     * @param code 响应成功或错误的编码
     * @return 响应参数
     */
    public Result<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    /**
     * 获取响应的提示消息
     *
     * @return 响应的提示消息
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置响应的提示消息
     *
     * @param msg 响应的提示消息
     * @return 响应参数
     */
    public Result<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    /**
     * 获取响应的异常消息
     *
     * @return 响应的异常消息
     */
    public String getExceptionMsg() {
        return exceptionMsg;
    }

    /**
     * 设置响应的异常消息
     *
     * @param exceptionMsg 响应的异常消息
     * @return 响应参数
     */
    public Result<T> setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
        return this;
    }

    /**
     * 获取响应数据
     *
     * @return 响应数据
     */
    public T getData() {
        return data;
    }

    /**
     * 设置响应数据
     *
     * @param data 响应数据
     * @return 响应参数
     */
    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }
}