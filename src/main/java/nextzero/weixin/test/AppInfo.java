package nextzero.weixin.test;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.util.Properties;

public class AppInfo {
    private static Logger LOG = LogManager.getLogger(AppInfo.class);

    private static String appId = null;
    private static String appsecret = null;
    private static String token = null;

    static{
        try {
            FileInputStream appInfoInputStream = new FileInputStream("app-info");
            Properties appInfo = new Properties();
            appInfo.load(appInfoInputStream);
            appId = appInfo.getProperty("appID",null);
            appsecret = appInfo.getProperty("appsecret",null);
            token = appInfo.getProperty("token", null);
        } catch (Exception e) {
            LOG.error("", e);
        }
    }

    public static String getAppId(){
        return appId;
    }

    public static String getAppsecret(){
        return appsecret;
    }

    public static String getToken(){
        return token;
    }
}
