package com.kunbao.weixin.sdk.management.menu.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by lemon_bar on 15/7/7.
 */
public class ButtonRes {
    @JsonProperty("name")
    protected String name;
    @JsonProperty("type")
    protected String type;
    @JsonProperty("key")
    private String key;
    @JsonProperty("url")
    private String url;
    @JsonProperty("sub_button")
//    @Setter
    private SubButtons subButtons;
//    private List<ButtonRes> subButtons;
}
