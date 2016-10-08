package com.kunbao.weixin.sdk.message.domain.received.event;

import com.kunbao.weixin.sdk.util.xml.XMLCDataAdapter;
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Created by lemon_bar on 15/7/30.
 */
@Getter
@ToString(callSuper = true)
@XmlRootElement(name = "xml")
public class WXReceivedTemplateSendFinishEvent extends WXReceivedEvent {
    @XmlElement(name = "MsgID")
    private String msgId;

    @XmlJavaTypeAdapter(XMLCDataAdapter.class)
    @XmlElement(name = "Status")
    private String status;
}
