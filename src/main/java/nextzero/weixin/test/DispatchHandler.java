package nextzero.weixin.test;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.util.Date;

@Path("/dispatch")
public class DispatchHandler {

    @Context
    protected HttpServletRequest request;

    @GET
    @Produces("application/json")
    public String dispatch(@QueryParam("echostr") String echostr){
        System.out.println(request.getQueryString());
        return echostr;
    }
}
