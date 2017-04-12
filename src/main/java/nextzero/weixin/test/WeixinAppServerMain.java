package nextzero.weixin.test;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class WeixinAppServerMain {

    public static void main(String[] args) {
        PropertyConfigurator.configure("log4j.properties");

        ServletHolder cxf = new ServletHolder(new CXFServlet());
        ServletContextHandler cxfContext = new ServletContextHandler();
        cxfContext.setContextPath("/");
        cxfContext.addServlet(cxf,  "/*");
        cxfContext.addEventListener(new ContextLoaderListener());
        cxfContext.setInitParameter("contextClass", AnnotationConfigWebApplicationContext.class.getName());
        cxfContext.setInitParameter("contextConfigLocation", WeixinAppBuilder.class.getName());
//        EnumSet<DispatcherType> dispatches = EnumSet.allOf(DispatcherType.class);
//        cxfContext.addFilter(new FilterHolder(new NoCacheFilter()), "/*", dispatches);

        Server jettyServer = new Server(80);
        jettyServer.setHandler(cxfContext);
        try {
            jettyServer.start();
            jettyServer.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
