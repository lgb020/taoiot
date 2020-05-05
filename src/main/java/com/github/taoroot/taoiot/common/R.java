package com.github.taoroot.taoiot.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * JSON OBJ Format
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private int result;

    @Getter
    @Setter
    private String msg;

    @Getter
    @Setter
    private T data;

    public static <T> R<T> ok() {
        return restResult(null, Const.SUCCESS, null);
    }

    public static <T> R<T> ok(String msg) {
        return restResult(null, Const.SUCCESS, msg);
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, Const.SUCCESS, null);
    }

    public static <T> R<T> ok(T data, String msg) {
        return restResult(data, Const.SUCCESS, msg);
    }

    public static <T> R<T> err() {
        return restResult(null, Const.FAIL, null);
    }

    public static <T> R<T> err(String msg) {
        return restResult(null, Const.FAIL, msg);
    }

    public static <T> R<T> err(T data) {
        return restResult(data, Const.FAIL, null);
    }

    public static <T> R<T> err(T data, String msg) {
        return restResult(data, Const.FAIL, msg);
    }

    private static <T> R<T> restResult(T data, int code, String msg) {
        R<T> apiResult = new R<>();
        apiResult.setResult(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

}