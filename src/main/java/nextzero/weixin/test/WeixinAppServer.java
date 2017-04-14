package nextzero.weixin.test;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class WeixinAppServer {
    public static Logger LOG = LogManager.getLogger(WeixinAppServer.class);

    public static String APPID;
    public static String APPSECRET;
    public static String TOKEN;
    public static String CONTEXTPATH;

    protected Server server;
    protected MessageDispatch dispath = new MessageDispatch();

    public void start() throws Exception{
        if(APPID == null || APPID.isEmpty()  || APPSECRET == null || APPSECRET.isEmpty() || TOKEN == null || TOKEN.isEmpty() || CONTEXTPATH == null || CONTEXTPATH.isEmpty()){
            throw new IllegalArgumentException();
        }

        server = new Server(80);
        server.setHandler(new WeixinAppHandler());
        server.start();
    }

    public void addHandle(Object handle) {
        dispath.addHandle(handle);
    }

    private class WeixinAppHandler extends AbstractHandler{
        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            if(!target.equals(CONTEXTPATH)){
                LOG.error("weixin platform request url error");
                return ;
            }

            try{
                if(request.getMethod().equalsIgnoreCase("GET")){
                    //接入认证
                    Map<String, String> parameters = readParameters(request);
                    //...
                    String echostr = parameters.get("echostr");
                    writeData(response, echostr);
                }else if(request.getMethod().equalsIgnoreCase("POST")){
                    //推送消息处理
                    byte[] data = readInputDataByPost(request);
                    String resp = dispath.dispatch(new String(data));
                    writeData(response, resp);
                }else{
                    LOG.error("error http method:" + request.getMethod());
                }
            }catch (Exception e){
                LOG.error(e);
            }
        }

        public Map<String,String> readParameters(HttpServletRequest request) throws UnsupportedEncodingException {
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

        public byte[] readInputDataByPost(HttpServletRequest request){
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

        public void writeData(HttpServletResponse response, String data){
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
}
