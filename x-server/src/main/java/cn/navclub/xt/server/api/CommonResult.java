package cn.navclub.xt.server.api;

import cn.navclub.xt.server.util.StringUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

public class CommonResult<T> {
    /**
     * 响应状态码
     */
    private int code;
    /**
     * 响应消息
     */
    private String message;
    /**
     * 响应数据
     */
    private T data;
    /**
     * 记录当前泛型类型,仅仅用户本地序列化向用户输出
     */
    @JsonIgnore
    private String clazzName;

    public CommonResult() {

    }

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

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public String getClazzName() {
        final String clazzName;
        if (StringUtil.isEmpty(this.clazzName)) {
            final Class<?> clazz;
            if (this.data == null) {
                clazz = Void.class;
            } else {
                clazz = this.data.getClass();
            }
            clazzName = clazz.getName();
        } else {
            clazzName = this.clazzName;
        }
        return clazzName;
    }

    /**
     * 将该对象序列化为json对象
     */
    public Buffer toBuffer() {
        return JsonObject.mapFrom(this).toBuffer();
    }
}
