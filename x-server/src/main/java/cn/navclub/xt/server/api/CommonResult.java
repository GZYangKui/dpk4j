package cn.navclub.xt.server.api;

import io.vertx.core.json.JsonObject;

public class CommonResult<T> {
    /**
     * 响应状态码
     */
    private int code;
    /**
     * 响应消息
     * */
    private String message;
    /**
     * 响应数据
     */
    private T data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     *
     * 将该对象序列化为json对象
     *
     */
    public JsonObject toJson(){
        return JsonObject.mapFrom(this);
    }
}
