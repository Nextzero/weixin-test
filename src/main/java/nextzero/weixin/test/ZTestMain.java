package nextzero.weixin.test;

import nextzero.weixin.test.message.MenuMessageClick;

import java.io.FileInputStream;
import java.util.Properties;

public class ZTestMain {

    @Handler(msgType = "event", event = "click")
    public String onClick(MenuMessageClick event){
        System.out.println(event);
        return "";
    }

    public static void main(String[] args) {
        try {
            FileInputStream appInfoInputStream = new FileInputStream("app-info");
            Properties appInfo = new Properties();
            appInfo.load(appInfoInputStream);
            WeixinAppServer.APPID = appInfo.getProperty("appID",null);
            WeixinAppServer.APPSECRET = appInfo.getProperty("appsecret",null);
            WeixinAppServer.TOKEN = appInfo.getProperty("token", null);
            WeixinAppServer.CONTEXTPATH = "/weixin/dispatch";

            WeixinAppServer server = new WeixinAppServer();
            server.addHandle(new ZTestMain());
            server.start();

            while(true){
                Thread.sleep(10000);
            }
        } catch (Exception e) {
        }
    }
}
