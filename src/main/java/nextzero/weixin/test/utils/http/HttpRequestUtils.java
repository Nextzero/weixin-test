package nextzero.weixin.test.utils.http;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestUtils {
    public static final Logger LOG = LogManager.getLogger(HttpRequestUtils.class);

    public static byte[] getInputDataByPost(HttpServletRequest request){
        int length = request.getContentLength();
        byte[] buf = new byte[length];
        ServletInputStream inputStream = null;
        try {
            int readLength = 0;
            int cur = 0;
            inputStream = request.getInputStream();
            while(-1 != (cur = inputStream.read(buf,readLength,length-readLength))){
                readLength += cur;
            }
        } catch (IOException e) {
            LOG.error("", e);
            return null;
        }
        return buf;
    }

    public static Map<String,String> getParameters(HttpServletRequest request) throws UnsupportedEncodingException {
        Map<String,String> ret = new HashMap<String, String>();
        String queryString = request.getQueryString();
        if(null != queryString){
            queryString = URLDecoder.decode(queryString, "UTF-8");
            String[] parameters = queryString.split("&");
            for(String param : parameters){
                String[] keyValue = param.split("=");
                if(2 == keyValue.length){
                    ret.put(keyValue[0],keyValue[1]);
                }
            }
        }
        return ret;
    }

    public void response(HttpServletResponse response, String data){
        try{
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Cache-Control", "no-cache");
            byte[] resultBytes = data.getBytes();
            response.setContentLength(resultBytes.length);
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(resultBytes, 0, resultBytes.length);
            outputStream.flush();
            outputStream.close();
        }catch(IOException e){
            LOG.error("", e);
        }
    }
}
