package cn.navclub.xt.server.enums;

public enum HttpRequestAPI {
    UNKNOWN("/unknown",APPAction.UNKNOWN),
    REQUEST_PUBLISH("/obs/publish",APPAction.REQUEST_PUBLISH),
    SSO_LOGIN("/sso/login",APPAction.USER_LOGIN);

    private final String url;
    private final APPAction action;

    HttpRequestAPI(String url, APPAction action) {
        this.url = url;
        this.action = action;
    }

    public String getUrl() {
        return url;
    }

    public APPAction getAction() {
        return action;
    }

    public String getSubPath(){
       var index =  this.url.indexOf("/",1);
       return this.url.substring(index);
    }

    public static HttpRequestAPI getApi(String path){
        for (HttpRequestAPI value : values()) {
            if (value.getUrl().equals(path)){
                return value;
            }
        }
        return UNKNOWN;
    }
}
