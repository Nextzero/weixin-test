package nextzero.weixin.test.handle;

import javax.ws.rs.*;
import java.util.Date;

@Path("/ping")
public class PingHandler {

    @GET
    @Produces("application/json")
    public String ping(){
        return new Date().toString();
    }
}
