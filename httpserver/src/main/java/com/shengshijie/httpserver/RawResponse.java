
package com.shengshijie.httpserver;

import com.google.gson.GsonBuilder;

public final class RawResponse<T> {

    private int code;
    private String message;
    private T data;


    public RawResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public String toJSONString(){
        return new GsonBuilder().create().toJson(this);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public static <T> RawResponse<T> ok(T data) {
        return new RawResponse<>(200, "ok", data);
    }

    public static RawResponse<Void> ok() {
        return new RawResponse(200, "ok", null);
    }

    public static <T> RawResponse<T> ok(String msg, T data) {
        return new RawResponse<>(200, msg, data);
    }

    public static RawResponse fail(String msg) {
        return new RawResponse<String>(500, msg, null);
    }
}
