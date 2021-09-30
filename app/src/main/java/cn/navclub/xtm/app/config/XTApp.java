package cn.navclub.xtm.app.config;

public class XTApp {
    /**
     * 当前机器识别码
     */
    private Integer robotCode;
    /**
     * 当前机器识别码
     */
    private String robotPw;
    /**
     * 服务器主机地址
     */
    private String host;
    /**
     * 服务器端口号
     */
    private Integer port;
    /**
     * 判断是否正在远程/接受远程中
     */
    private volatile boolean remoting;


    private XTApp(){

    }

    public Integer getRobotCode() {
        return robotCode;
    }

    public void setRobotCode(Integer robotCode) {
        this.robotCode = robotCode;
    }

    public String getRobotPw() {
        return robotPw;
    }

    public void setRobotPw(String robotPw) {
        this.robotPw = robotPw;
    }

    public String getHost() {
        return host;
    }

    public XTApp setHost(String host) {
        this.host = host;
        return this;
    }

    public boolean isRemoting() {
        return remoting;
    }

    public XTApp setRemoting(boolean remoting) {
        this.remoting = remoting;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public XTApp setPort(Integer port) {
        this.port = port;
        return this;
    }

    private static XTApp instance;

    public synchronized static XTApp getInstance() {
        if (instance == null) {
            instance = new XTApp();
        }
        return instance;
    }
}
