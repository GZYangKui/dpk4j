package cn.navclub.xt.server.api;

public class RestResponse {
    public static final int OK = 200;
    public static final int INFO = 300;
    public static final int ERROR = 500;
    public static final int FORBID = 403;

    public static <T> CommonResult<T> rest(int code, T data, String message) {
        var result = new CommonResult<T>();
        result.setCode(OK);
        result.setData(data);
        result.setMessage(message);
        return result;
    }

    public static <T> CommonResult<T> success(T data, String message) {
        return rest(OK, data, message);
    }

    public static <T> CommonResult<T> success(T data) {
        return success(data, "操作成功");
    }

    public static <T> CommonResult<T> success() {
        return success(null, "操作成功");
    }

    public static <T> CommonResult<T> error(String message) {
        return rest(ERROR, null, message);
    }

    public static <T> CommonResult<T> fail(String message) {
        return rest(INFO, null, message);
    }
}
