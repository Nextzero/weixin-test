package nextzero.weixin.test.menu;

import nextzero.weixin.test.AccessToken;
import nextzero.weixin.test.utils.http.HttpClientUtils;

import java.io.BufferedReader;
import java.io.FileReader;

public class MenuHandler {

    public String createMenu() throws Exception {
        String url = String.format("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s", AccessToken.getAccessToken());
        BufferedReader reader = new BufferedReader(new FileReader("menu-config"));
        try{
            StringBuilder builder = new StringBuilder();
            String line = null;
            while(null != (line = reader.readLine())){
                builder.append(line);
            }
            String response = HttpClientUtils.post(url, builder.toString());
            System.out.println(response);
        }catch (Exception e){
            reader.close();
        }
        return null;
    }
}
