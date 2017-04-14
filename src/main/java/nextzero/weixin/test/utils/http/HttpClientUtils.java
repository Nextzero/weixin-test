package nextzero.weixin.test.utils.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpClientUtils {

    public static CloseableHttpClient createSSLClientDefault() throws Exception{
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy(){
            public boolean isTrusted(X509Certificate[] chain,String authType) throws CertificateException {
                return true;//信任所有
            }
        }).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
        return HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }

    public static String get(String url) throws Exception{
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;

        try{
            client = HttpClientUtils.createSSLClientDefault();
            HttpGet httpget = new HttpGet(url);
            response = client.execute(httpget);
            int httpStatus = response.getStatusLine().getStatusCode();
            if(HttpStatus.SC_OK == httpStatus){
                HttpEntity entitys = response.getEntity();
                return EntityUtils.toString(entitys);
            }else{
                throw new Exception("HTTP ERROR:" + httpStatus);
            }
        }finally{
            if(null != client){
                try {
                    client.close();
                } catch (IOException e) {
                }
            }
            if(null != response){
                try {
                    response.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static String post(String url, String data) throws Exception{
        HttpPost httpPost = new HttpPost(url);
        StringEntity se = new StringEntity(data);
        httpPost.setEntity(se);

        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;

        try{
            client = HttpClientUtils.createSSLClientDefault();
            response = client.execute(httpPost);
            int httpStatus = response.getStatusLine().getStatusCode();
            if(HttpStatus.SC_OK == httpStatus){
                HttpEntity entitys = response.getEntity();
                return EntityUtils.toString(entitys);
            }else{
                throw new Exception("HTTP ERROR:" + httpStatus);
            }
        }finally {
            if(null != client){
                try {
                    client.close();
                } catch (IOException e) {
                }
            }
            if(null != response){
                try {
                    response.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
