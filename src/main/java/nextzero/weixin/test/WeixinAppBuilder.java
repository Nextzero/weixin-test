package nextzero.weixin.test;

import nextzero.weixin.test.handle.PingHandler;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ext.RuntimeDelegate;
import java.util.Arrays;

@Configuration
public class WeixinAppBuilder {
    @Bean( destroyMethod = "shutdown")
    public SpringBus cxf() {
        return new SpringBus();
    }

    @Bean
    public Server jaxRsServer() {
        JAXRSServerFactoryBean factory = RuntimeDelegate.getInstance().createEndpoint(
                jaxRsApiApplication(), JAXRSServerFactoryBean.class );

        factory.setServiceBeans(Arrays.<Object>asList(getPingHandler()));
        factory.setServiceBeans(Arrays.<Object>asList(getDispatchHandler()));
        factory.setAddress("" + factory.getAddress());
        factory.setProviders(Arrays.<Object>asList(jsonProvider()));

        return factory.create();
    }

    @Bean
    public PingHandler getPingHandler(){
        return new PingHandler();
    }

    @Bean
    public DispatchHandler getDispatchHandler(){
        return new DispatchHandler();
    }

    @Bean
    public WeixinApp jaxRsApiApplication() {
        return new WeixinApp();
    }

    @Bean
    public JacksonJsonProvider jsonProvider() {
        return new JacksonJsonProvider();
    }
}
