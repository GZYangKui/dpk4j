package cn.navclub.xt.server.util;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class RequestDataUtil {
    /**
     *
     * 将请求表单数据映射为json数据
     *
     */
    public static JsonObject formToJson(RoutingContext rct) {
        var form = new JsonObject();
        var str = rct.getBodyAsString();
        if (!StringUtil.isEmpty(str)) {
            var a1 = str.split("&");

            for (String s : a1) {
                var index = s.indexOf("=");
                if (index <= 0 || index == s.length() - 1) {
                    continue;
                }
                var key = s.substring(0, index);
                var value = s.substring(index + 1);
                form.put(key, value);
            }
        }
        return form;
    }
}
