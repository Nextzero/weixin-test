package nextzero.weixin.test;

import nextzero.weixin.test.utils.xml.XmlFormat;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MessageDispatch{
    public static Logger LOG = LogManager.getLogger(MessageDispatch.class);

    protected Map<String, HandlerEntry> handles = new HashMap<String, HandlerEntry>();
    protected XmlFormat format = new XmlFormat();

    public synchronized void addHandle(Object handle){
        for(Method method : handle.getClass().getMethods()){
            Handler annotation = method.getAnnotation(Handler.class);
            if(null == annotation){
                LOG.debug("no handler annotation:" + method.getName());
                continue;
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            if(parameterTypes.length != 1){
                LOG.debug("parmeter too much:" + method.getName());
                continue;
            }
            Class paramClass = parameterTypes[0];

            String key = annotation.msgType() + annotation.event();
            if(handles.containsKey(key)){
                LOG.warn("handle key 重复:" + key);
            }
            HandlerEntry entry = new HandlerEntry();
            entry.msgType = annotation.msgType();
            entry.event = annotation.event();
            entry.object = handle;
            entry.method = method;
            entry.paramClass = paramClass;
            entry.method.setAccessible(true);
            handles.put(key, entry);
        }
    }

    public String dispatch(String data){
        try {
            MessageHeader header = (MessageHeader)format.parse(data, MessageHeader.class);
            String key = header.msgType + header.event;
            if(!handles.containsKey(key)){
                LOG.error("not exist handle");
                return null;
            }
            HandlerEntry entry = handles.get(key);
            if(entry.paramClass.getName().endsWith(String.class.getName())){
                //不需要做参数转换
                Object response = entry.method.invoke(entry.object, data);
                if(response instanceof String){
                    return (String)response;
                }else{
                    return XmlFormat.format(response);
                }
            }else{
                //按照paramClass转换参数
                Object response = entry.method.invoke(entry.object, format.parseEx(data, entry.paramClass));
                if(response instanceof String){
                    return (String)response;
                }else{
                    return XmlFormat.format(response);
                }
            }
        } catch (Exception e) {
            LOG.error(e);
        }
        return null;
    }

    @XmlRootElement(name = "xml")
    @XmlAccessorType(XmlAccessType.FIELD)
    static public class MessageHeader{

        @XmlElement(name="ToUserName")
        public String toUserName;

        @XmlElement(name = "FromUserName")
        public String fromUserName;

        @XmlElement(name = "CreateTime")
        public String createTime;

        @XmlElement(name = "MsgType")
        public String msgType = "";

        @XmlElement(name = "Event")
        public String event = "";
    }

    class HandlerEntry{
        public String msgType;
        public String event;
        public Class paramClass;
        public Object object;
        public Method method;
    }
}
