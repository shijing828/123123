package com.kunbao.weixin.sdk.message.domain.received.event;

import com.kunbao.weixin.sdk.util.xml.XMLCDataAdapter;
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Created by lemon_bar on 15/7/6.
 */
@Getter
@ToString(callSuper = true)
@XmlRootElement(name = "xml")
public class WXReceivedScanEvent extends WXReceivedEvent {
    @XmlJavaTypeAdapter(XMLCDataAdapter.class)
    @XmlElement(name = "EventKey")
    private String eventKey;

    @XmlJavaTypeAdapter(XMLCDataAdapter.class)
    @XmlElement(name = "Ticket")
    private String ticket;
}
