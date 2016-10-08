package com.kunbao.weixin.sdk;

import com.kunbao.weixin.sdk.datacube.article.WXArticleDataCubeService;
import com.kunbao.weixin.sdk.datacube.message.WXMessageDataCubeService;
import com.kunbao.weixin.sdk.datacube.user.WXUserDataCubeService;
import com.kunbao.weixin.sdk.datacube.wxinterface.WXInterfaceDataCubeService;
import com.kunbao.weixin.sdk.management.account.WXAccountService;
import com.kunbao.weixin.sdk.management.material.WXMaterialService;
import com.kunbao.weixin.sdk.management.menu.WXMenuService;
import com.kunbao.weixin.sdk.management.oauth2.WXOAuthService;
import com.kunbao.weixin.sdk.management.user.WXUserService;
import com.kunbao.weixin.sdk.message.WXMessageService;
import com.kunbao.weixin.sdk.security.WXSecurityService;

/**
 * Created by lemon_bar on 15/7/22.
 */
public class WXServiceFactory {
    private WXSecurityService wxSecurityService = null;

    public synchronized WXSecurityService getWxSecurityService() {
        if (wxSecurityService == null) {
            wxSecurityService = new WXSecurityService();
        }
        return wxSecurityService;
    }

    private WXMessageService wxMessageService = null;

    public synchronized WXMessageService getWxMessageService() {
        if (wxMessageService == null) {
            wxMessageService = new WXMessageService();
        }
        return wxMessageService;
    }

    private WXAccountService wxAccountService = null;

    public synchronized WXAccountService getWxAccountService() {
        if (wxAccountService == null) {
            wxAccountService = new WXAccountService();
        }
        return wxAccountService;
    }

    private WXMaterialService wxMaterialService = null;

    public synchronized WXMaterialService getWxMaterialService() {
        if (wxMaterialService == null) {
            wxMaterialService = new WXMaterialService();
        }
        return wxMaterialService;
    }

    private WXMenuService wxMenuService = null;

    public synchronized WXMenuService getWxMenuService() {
        if (wxMenuService == null) {
            wxMenuService = new WXMenuService();
        }
        return wxMenuService;
    }

    private WXUserService wxUserService = null;

    public synchronized WXUserService getWxUserService() {
        if (wxUserService == null) {
            wxUserService = new WXUserService();
        }
        return wxUserService;
    }

    private WXOAuthService wxOAuthService = null;

    public synchronized WXOAuthService getWxOAuthService() {
        if (wxOAuthService == null) {
            wxOAuthService = new WXOAuthService();
        }

        return wxOAuthService;

    }

    private WXUserDataCubeService userDataCubeService = null;

    public synchronized WXUserDataCubeService getUserDataCubeService() {
        if (userDataCubeService == null) {
            userDataCubeService = new WXUserDataCubeService();
        }

        return userDataCubeService;
    }

    private WXArticleDataCubeService articleDataCubeService = null;

    public synchronized WXArticleDataCubeService getArticleDataCubeService() {
        if (articleDataCubeService == null) {
            articleDataCubeService = new WXArticleDataCubeService();
        }

        return articleDataCubeService;
    }

    private WXMessageDataCubeService messageDataCubeService = null;

    public synchronized WXMessageDataCubeService getMessageDataCubeService() {
        if (messageDataCubeService == null) {
            messageDataCubeService = new WXMessageDataCubeService();
        }

        return messageDataCubeService;
    }

    private WXInterfaceDataCubeService interfaceDataCubeService = null;

    public synchronized WXInterfaceDataCubeService getInterfaceDataCubeService() {
        if (interfaceDataCubeService == null) {
            interfaceDataCubeService = new WXInterfaceDataCubeService();
        }
        return interfaceDataCubeService;
    }

}
