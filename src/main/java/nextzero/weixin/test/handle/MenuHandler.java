package nextzero.weixin.test.handle;

import nextzero.weixin.test.ErrorMsg;
import nextzero.weixin.test.utils.HttpClientUtils;

import java.io.BufferedReader;
import java.io.FileReader;

public class MenuHandler {

    public ErrorMsg createMenu() throws Exception {
        String url = String.format("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s", AccessTokenHandler.getAccessToken());
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


    public static void main(String[] args) throws Exception {
        MenuHandler handler = new MenuHandler();
        handler.createMenu();
    }
}
