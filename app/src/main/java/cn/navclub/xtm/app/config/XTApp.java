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

    private static XTApp instance;

    public synchronized static XTApp getInstance() {
        if (instance == null) {
            instance = new XTApp();
        }
        return instance;
    }
}
