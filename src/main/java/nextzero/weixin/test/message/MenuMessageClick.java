package nextzero.weixin.test.message;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//点击菜单拉取消息时的事件推送
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class MenuMessageClick{

    @XmlElement(name="ToUserName", required = true)
    protected String toUserName;

    @XmlElement(name = "FromUserName", required = true)
    protected String fromUserName;

    @XmlElement(name = "CreateTime", required = true)
    protected String createTime;

    @XmlElement(name = "MsgType", required = true)
    protected String msgType;

    @XmlElement(name = "Event", required = true)
    protected String event;

    @XmlElement(name = "EventKey", required = true)
    protected String eventKey;

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    @Override
    public String toString() {
        return "MenuMessageClick{" +
                "toUserName='" + toUserName + '\'' +
                ", fromUserName='" + fromUserName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", msgType='" + msgType + '\'' +
                ", event='" + event + '\'' +
                ", eventKey='" + eventKey + '\'' +
                '}';
    }
}
