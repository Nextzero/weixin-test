package nextzero.weixin.test.handle;

import com.alibaba.fastjson.JSON;
import nextzero.weixin.test.AppInfo;
import nextzero.weixin.test.utils.HttpClientUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Map;

public class AccessTokenHandler {
    private static Logger LOG = LogManager.getLogger(AppInfo.class);
    private static String accessToken;
    private static long expireTime = 0;

    public synchronized static String getAccessToken(){
        if(expireTime > System.currentTimeMillis()){
            return accessToken;
        }else{
            return refreshAccessToken();
        }
    }

    public synchronized static String refreshAccessToken(){
        try {
            String data = HttpClientUtils.get(String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", AppInfo.getAppId(), AppInfo.getAppsecret()));
            Map<String,Object> json = (Map)JSON.parse(data);
            if(json.containsKey("access_token") && json.containsKey("expires_in")){
                accessToken = json.get("access_token").toString();
                expireTime = System.currentTimeMillis() + Long.valueOf(json.get("expires_in").toString());
                LOG.info(accessToken);
                System.out.println(accessToken);
                return accessToken;
            }else{
                LOG.error(data);
            }
            return null;
        } catch (Exception e) {
            LOG.error("", e);
        }
        return null;
    }
}
