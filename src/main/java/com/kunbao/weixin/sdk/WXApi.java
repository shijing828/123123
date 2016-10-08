package com.kunbao.weixin.sdk;

import com.kunbao.weixin.sdk.base.domain.constant.WXAppConstant;
import com.kunbao.weixin.sdk.base.exception.WXException;
import com.kunbao.weixin.sdk.datacube.article.domain.*;
import com.kunbao.weixin.sdk.datacube.message.domain.UpstreamMsgData;
import com.kunbao.weixin.sdk.datacube.message.domain.UpstreamMsgDistData;
import com.kunbao.weixin.sdk.datacube.message.domain.UpstreamMsgHourData;
import com.kunbao.weixin.sdk.datacube.user.domain.UserCumulateItem;
import com.kunbao.weixin.sdk.datacube.user.domain.UserSummaryItem;
import com.kunbao.weixin.sdk.datacube.wxinterface.domain.InterfaceData;
import com.kunbao.weixin.sdk.datacube.wxinterface.domain.InterfaceHourData;
import com.kunbao.weixin.sdk.jsapiticket.WXJsapiTicketController;
import com.kunbao.weixin.sdk.management.material.domain.MaterialPageableRequest;
import com.kunbao.weixin.sdk.management.material.domain.NewsList;
import com.kunbao.weixin.sdk.management.material.domain.NewsUpdater;
import com.kunbao.weixin.sdk.management.material.domain.constant.MediaType;
import com.kunbao.weixin.sdk.management.material.response.WXAddCommonMaterialResponse;
import com.kunbao.weixin.sdk.management.material.response.WXGetCommonMaterialListResponse;
import com.kunbao.weixin.sdk.management.material.response.WXGetMaterialCountResponse;
import com.kunbao.weixin.sdk.management.material.response.WXGetNewsMaterialListResponse;
import com.kunbao.weixin.sdk.management.menu.domain.Menu;
import com.kunbao.weixin.sdk.management.menu.response.WXMenuGetResponse;
import com.kunbao.weixin.sdk.management.menu.response.WXSelfMenuGetResponse;
import com.kunbao.weixin.sdk.management.oauth2.response.WXOAuthTokenGetResponse;
import com.kunbao.weixin.sdk.management.oauth2.response.WXOAuthUserInfoGetResponse;
import com.kunbao.weixin.sdk.management.user.domain.WXLang;
import com.kunbao.weixin.sdk.management.user.domain.WXUserGroup;
import com.kunbao.weixin.sdk.management.user.domain.WXUserList;
import com.kunbao.weixin.sdk.management.user.response.WXUserGetResponse;
import com.kunbao.weixin.sdk.management.user.response.WXUserInfoListResponse;
import com.kunbao.weixin.sdk.management.user.response.WXUserInfoResponse;
import com.kunbao.weixin.sdk.message.domain.base.WXMessageBase;
import com.kunbao.weixin.sdk.message.domain.send.json.metadata.MusicContent;
import com.kunbao.weixin.sdk.message.domain.send.json.metadata.NewsItemContent;
import com.kunbao.weixin.sdk.message.domain.send.json.metadata.VideoContent;
import com.kunbao.weixin.sdk.message.domain.send.xml.WXSendMusicMedia;
import com.kunbao.weixin.sdk.message.domain.send.xml.WXSendNewsItem;
import com.kunbao.weixin.sdk.message.domain.send.xml.WXSendVideoMedia;
import com.kunbao.weixin.sdk.message.domain.template.Industry;
import com.kunbao.weixin.sdk.message.domain.template.MessageInfo;
import com.kunbao.weixin.sdk.security.domain.WXJsConfig;
import com.kunbao.weixin.sdk.util.aes.AesException;

import java.util.Date;
import java.util.List;

/**
 * Created by lemon_bar on 15/7/19.
 */
public class WXApi {
    private WXServiceFactory factory;

    /**
     * 微信api接口
     *
     * @param appId          app id
     * @param appSecret      app secret
     * @param appToken       app token
     * @param encodingAESKey encoding aes key
     * @param domainName     domain name
     */
    public WXApi(String appId, String appSecret, String appToken, String encodingAESKey, String domainName) {
        WXAppConstant.init(appId, appSecret, appToken, encodingAESKey, domainName);
        factory = new WXServiceFactory();
    }

    /**
     * 构造函数
     *
     * @param appId          app id
     * @param appSecret      app secret
     * @param appToken       app token
     * @param encodingAESKey encoding aes key
     */
    public WXApi(String appId, String appSecret, String appToken, String encodingAESKey) {
        WXAppConstant.init(appId, appSecret, appToken, encodingAESKey);
        factory = new WXServiceFactory();
    }

    /**
     * 通过检验signature对请求进行校验
     *
     * @param signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @return 若返回true，请原样返回echostr参数内容，则接入生效，成为开发者成功，否则接入失败。
     */
    public boolean checkSignature(String signature, String timestamp, String nonce) {
        return factory.getWxSecurityService().checkSignature(signature, timestamp, nonce);
    }

    /**
     * 解密消息
     *
     * @param encryptType  加密类型，encrypt_type为aes时，表示aes加密（暂时只有raw和aes两种值)。
     * @param msgSignature 表示对消息体的签名
     * @param timestamp    时间戳
     * @param nonce        随机数
     * @param content      需要解密的消息
     * @return 解密后的消息
     * @throws AesException 异常信息
     */
    public String decryptContent(String encryptType, String msgSignature, String timestamp, String nonce, String content) throws AesException {
        return factory.getWxSecurityService().decryptContent(encryptType, msgSignature, timestamp, nonce, content);
    }

    /**
     * 加密消息
     *
     * @param encryptType 加密类型，encrypt_type为aes时，表示aes加密（暂时只有raw和aes两种值)。
     * @param timestamp   时间戳
     * @param nonce       随机数
     * @param content     需要加密的消息
     * @return 加密后的消息
     * @throws AesException 异常信息
     */
    public String encryptContent(String encryptType, String timestamp, String nonce, String content) throws AesException {
        return factory.getWxSecurityService().encryptContent(encryptType, timestamp, nonce, content);
    }

    /**
     * @param url 签名用的url必须是调用JS接口页面的完整URL。
     * @return 微信签名
     * @throws WXException 异常信息
     */
    public WXJsConfig constructWXJsConfig(String url) throws WXException {
        //签名生成规则如下：参与签名的字段包括noncestr（随机字符串）, 有效的jsapi_ticket, timestamp（时间戳）, url（当前网页的URL，不包含#及其后面部分);
        //对所有待签名参数按照字段名的ASCII 码从小到大排序（字典序）后，使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串string1;
        // 这里需要注意的是所有参数名均为小写字符。对string1作sha1加密，字段名和字段值都采用原始值，不进行URL 转义;
        WXJsConfig jsConfig = new WXJsConfig(url, WXJsapiTicketController.getTicket());
        return jsConfig;
    }

    /**
     * 获得微信服务器IP地址列表
     *
     * @return 微信服务器IP地址列表
     * @throws WXException 异常信息
     */
    public List<String> getCallbackIpList() throws WXException {
        return factory.getWxSecurityService().getCallbackIpList();
    }

    /**
     * 解析从微信推送过来的消息
     *
     * @param messageStr message string
     * @return 解析出来的消息类实例
     * @throws WXException 异常信息
     */
    public WXMessageBase consumeMessage(String messageStr) throws WXException {
        return factory.getWxMessageService().consumeMessage(messageStr);
    }

    /**
     * 产生需要被动回复的文本消息
     *
     * @param fromUser 公众号id
     * @param toUser   用户的open_id
     * @param content  回复的文本内容
     * @return xml内容
     * @throws WXException 异常信息
     */
    public String produceText(String fromUser, String toUser, String content) throws WXException {
        return factory.getWxMessageService().produceText(fromUser, toUser, content);
    }

    /**
     * 产生需要被动回复的图片消息
     *
     * @param fromUser 公众号id
     * @param toUser   用户的open_id
     * @param mediaId  图片的id
     * @return xml内容
     * @throws WXException 异常信息
     */
    public String produceImage(String fromUser, String toUser, String mediaId) throws WXException {
        return factory.getWxMessageService().produceImage(fromUser, toUser, mediaId);
    }

    /**
     * 产生需要被动回复的音频消息
     *
     * @param fromUser 公众号id
     * @param toUser   用户的open_id
     * @param mediaId  音频的id
     * @return xml内容
     * @throws WXException 异常信息
     */
    public String produceVoice(String fromUser, String toUser, String mediaId) throws WXException {
        return factory.getWxMessageService().produceVoice(fromUser, toUser, mediaId);
    }

    /**
     * 产生需要被动回复的视频消息
     *
     * @param fromUser   公众号id
     * @param toUser     用户的open_id
     * @param videoMedia 视频的id
     * @return xml内容
     * @throws WXException 异常信息
     */
    public String produceVideo(String fromUser, String toUser, WXSendVideoMedia videoMedia) throws WXException {
        return factory.getWxMessageService().produceVideo(fromUser, toUser, videoMedia);
    }

    /**
     * 产生需要被动回复的音乐消息
     *
     * @param fromUser   公众号id
     * @param toUser     用户的open_id
     * @param musicMedia 音乐资源
     * @return 音乐xml内容
     * @throws WXException 异常信息
     */
    public String produceMusic(String fromUser, String toUser, WXSendMusicMedia musicMedia) throws WXException {
        return factory.getWxMessageService().produceMusic(fromUser, toUser, musicMedia);
    }

    /**
     * 产生需要被动回复的图文消息
     *
     * @param fromUser  公众号id
     * @param toUser    用户的open_id
     * @param newsItems 图文资源(图文消息个数，限制为10条以内)
     * @return 图文xml内容
     * @throws WXException 异常信息
     */
    public String produceNews(String fromUser, String toUser, List<WXSendNewsItem> newsItems) throws WXException {
        return factory.getWxMessageService().produceNews(fromUser, toUser, newsItems);
    }

    /**
     * 发送文本内容的客服消息
     *
     * @param toUser  用户的open_id
     * @param content 文本内容
     * @return 是否发送成功
     * @throws WXException 异常信息
     */
    public boolean sendCustomText(String toUser, String content) throws WXException {
        return factory.getWxMessageService().sendCustomText(toUser, content);
    }

    /**
     * 发送图片的客服消息
     *
     * @param toUser  用户的open_id
     * @param mediaId 图片media id
     * @return 是否发送成功
     * @throws WXException 异常信息
     */
    public boolean sendCustomImage(String toUser, String mediaId) throws WXException {
        return factory.getWxMessageService().sendCustomImage(toUser, mediaId);
    }

    /**
     * 发送音频的客服消息
     *
     * @param toUser  用户的open_id
     * @param mediaId 音频media id
     * @return 是否发送成功
     * @throws WXException 异常信息
     */
    public boolean sendCustomVoice(String toUser, String mediaId) throws WXException {
        return factory.getWxMessageService().sendCustomVoice(toUser, mediaId);
    }

    /**
     * 发送视频的客服消息
     *
     * @param toUser       用户的open_id
     * @param videoContent 视频信息
     * @return 是否发送成功
     * @throws WXException 异常信息
     */
    public boolean sendCustomVideo(String toUser, VideoContent videoContent) throws WXException {
        return factory.getWxMessageService().sendCustomVideo(toUser, videoContent);
    }

    /**
     * 发送音乐的客服消息
     *
     * @param toUser       用户的open_id
     * @param musicContent 音乐信息
     * @return 是否发送成功
     * @throws WXException 异常信息
     */
    public boolean sendCustomMusic(String toUser, MusicContent musicContent) throws WXException {
        return factory.getWxMessageService().sendCustomMusic(toUser, musicContent);
    }

    /**
     * 发送图文的客服消息
     *
     * @param toUser      用户的open_id
     * @param newsContent 图文信息
     * @return 是否发送成功
     * @throws WXException 异常信息
     */
    public boolean sendCustomNews(String toUser, List<NewsItemContent> newsContent) throws WXException {
        return factory.getWxMessageService().sendCustomNews(toUser, newsContent);
    }

    /**
     * 新增临时素材
     *
     * @param type     素材类型
     * @param filePath 素材路径
     * @return 新增后，素材的链接
     * @throws WXException 异常信息
     */
    public String uploadTempMedia(MediaType type, String filePath) throws WXException {
        return factory.getWxMaterialService().uploadTempMedia(type, filePath);
    }

    /**
     * 新增图文永久素材
     *
     * @param newsList 图文列表
     * @return media Id
     * @throws WXException 异常信息
     */
    public String addNewsList(NewsList newsList) throws WXException {
        return factory.getWxMaterialService().addNewsList(newsList);
    }

    /**
     * 新增其它类型永久素材
     *
     * @param filePath 素材路径
     * @return 素材media id 和 url
     * @throws WXException 异常信息
     */
    public WXAddCommonMaterialResponse addCommonMaterial(String filePath) throws WXException {
        return factory.getWxMaterialService().addCommonMaterial(filePath);
    }

    /**
     * 删除永久素材
     *
     * @param mediaId media id
     * @return true代表成功
     * @throws WXException 异常信息
     */
    public boolean deleteMaterial(String mediaId) throws WXException {
        return factory.getWxMaterialService().deleteMaterial(mediaId);
    }

    /**
     * 根据mediaId，获取media url。
     *
     * @param mediaId media id
     * @return material temp url
     * @throws WXException 异常信息
     */
    public String getMaterialTempUrl(String mediaId) throws WXException {
        return factory.getWxMaterialService().getMediaUrl(mediaId);
    }

    /**
     * 读取微信media的bytes
     *
     * @param mediaId media id
     * @return media bytes
     * @throws WXException 异常信息
     */
    public byte[] getMaterialBytes(String mediaId) throws WXException {
        return factory.getWxMaterialService().getMediaBytes(mediaId);
    }

    /**
     * 更新永久图文素材
     *
     * @param newsUpdater 需要更新的内容
     * @return true代表成功
     * @throws WXException 异常信息
     */
    public boolean updateNewsItem(NewsUpdater newsUpdater) throws WXException {
        return factory.getWxMaterialService().updateNewsItem(newsUpdater);
    }

    /**
     * 获取素材总数
     *
     * @return 素材总数
     * @throws WXException 异常信息
     */
    public WXGetMaterialCountResponse getMaterialCount() throws WXException {
        return factory.getWxMaterialService().getCount();
    }

    /**
     * 获取普通素材列表
     *
     * @param pageableRequest 查询条件
     * @return 普通素材列表
     * @throws WXException 异常信息
     */
    public WXGetCommonMaterialListResponse getCommonMaterialList(MaterialPageableRequest pageableRequest) throws WXException {
        return factory.getWxMaterialService().getCommonMaterialList(pageableRequest);
    }

    /**
     * 获取图文素材列表
     *
     * @param pageableRequest 查询条件
     * @return 图文素材列表
     * @throws WXException 异常信息
     */
    public WXGetNewsMaterialListResponse getNewsMaterialList(MaterialPageableRequest pageableRequest) throws WXException {
        return factory.getWxMaterialService().getNewsMaterialList(pageableRequest);
    }

    /**
     * 创建用户分组
     *
     * @param groupName 分组名称
     * @return 新建分组信息
     * @throws WXException 异常信息
     */
    public WXUserGroup createUserGroup(String groupName) throws WXException {
        return factory.getWxUserService().createUserGroup(groupName);
    }

    /**
     * 获取分组列表
     *
     * @return 分组列表
     * @throws WXException 异常信息
     */
    public List<WXUserGroup> getUserGroup() throws WXException {
        return factory.getWxUserService().getUserGroup();
    }

    /**
     * 获取用户所在分组id
     *
     * @param openId 用户openid
     * @return 用户所在分组id
     * @throws WXException 异常信息
     */
    public int getUserInGroupId(String openId) throws WXException {
        return factory.getWxUserService().getUserInGroupId(openId);
    }

    /**
     * 更新分组名称
     *
     * @param groupId   分组id
     * @param groupName 分组名称
     * @return 是否成功
     * @throws WXException 异常信息
     */
    public boolean updateUserGroup(int groupId, String groupName) throws WXException {
        return factory.getWxUserService().updateUserGroup(groupId, groupName);
    }

    /**
     * 移动用户到某个分组
     *
     * @param openId  用户openid
     * @param groupId 分组id
     * @return 是否成功
     * @throws WXException 异常信息
     */
    public boolean moveUserToGroup(String openId, int groupId) throws WXException {
        return factory.getWxUserService().moveUserToGroup(openId, groupId);
    }

    /**
     * 批量移动用户
     *
     * @param openIdList 用户openid列表
     * @param groupId    分组id
     * @return 是否成功
     * @throws WXException 异常信息
     */
    public boolean moveBatchUserToGroup(List<String> openIdList, int groupId) throws WXException {
        return factory.getWxUserService().moveBatchUserToGroup(openIdList, groupId);
    }

    /**
     * 删除用户分组
     *
     * @param groupId 分组id
     * @return 是否成功
     * @throws WXException 异常信息
     */
    public boolean deleteUseGroup(int groupId) throws WXException {
        return factory.getWxUserService().deleteUseGroup(groupId);
    }

    /**
     * 设置用户备注名
     *
     * @param openId 用户openid
     * @param remark 备注名
     * @return 是否成功
     * @throws WXException 异常信息
     */
    public boolean remarkUser(String openId, String remark) throws WXException {
        return factory.getWxUserService().remarkUser(openId, remark);
    }

    /**
     * 获取用户列表
     *
     * @param nextOpenId 起始的openid
     * @return 用户列表
     * @throws WXException 异常信息
     */
    public WXUserGetResponse getUserList(String nextOpenId) throws WXException {
        return factory.getWxUserService().getUserList(nextOpenId);
    }

    /**
     * 获取用户信息
     *
     * @param openId 用户openid
     * @param lang   语言
     * @return 用户信息
     * @throws WXException 异常信息
     */
    public WXUserInfoResponse getUserInfo(String openId, WXLang lang) throws WXException {
        return factory.getWxUserService().getUserInfo(openId, lang);
    }

    /**
     * 批量获取用户信息
     *
     * @param userList 用户openid列表
     * @return 用户信息
     * @throws WXException 异常信息
     */
    public WXUserInfoListResponse getBatchUserInfo(WXUserList userList) throws WXException {
        return factory.getWxUserService().getBatchUserInfo(userList);
    }

    /**
     * 创建菜单
     *
     * @param menu 菜单
     * @return 是否成功
     * @throws WXException 异常信息
     */
    public boolean createMenu(Menu menu) throws WXException {
        return factory.getWxMenuService().createMenu(menu);
    }

    /**
     * 获取菜单
     *
     * @return 菜单
     * @throws WXException 异常信息
     */
    public WXMenuGetResponse getMenu() throws WXException {
        return factory.getWxMenuService().getMenu();
    }

    /**
     * 获取自定义菜单配置
     *
     * @return 自定义菜单配置
     * @throws WXException 异常信息
     */
    public WXSelfMenuGetResponse getSelfMenu() throws WXException {
        return factory.getWxMenuService().getSelfMenu();
    }

    /**
     * 删除菜单
     *
     * @return 是否成功
     * @throws WXException 异常信息
     */
    public boolean deleteMenu() throws WXException {
        return factory.getWxMenuService().deleteMenu();
    }

    /**
     * 把长链接转为短链接
     *
     * @param longUrl 长链接
     * @return 短链接
     * @throws WXException 异常信息
     */
    public String long2ShortUrl(String longUrl) throws WXException {
        return factory.getWxAccountService().long2ShortUrl(longUrl);
    }

    /**
     * 创建临时二维码
     *
     * @param expireSeconds 过期时间，单位秒，最大不超过604800（7天）
     * @param scenceId      场景id
     * @return 二维码链接
     * @throws WXException 异常信息
     */
    public String createTempQrcode(long expireSeconds, int scenceId) throws WXException {
        return factory.getWxAccountService().createTempQrcode(expireSeconds, scenceId);
    }

    /**
     * 创建带有场景id的永久二维码
     *
     * @param scenceId 场景id
     * @return 二维码链接
     * @throws WXException 异常信息
     */
    public String createLimitSceneQrCode(int scenceId) throws WXException {
        return factory.getWxAccountService().createLimitSceneQrCode(scenceId);
    }

    /**
     * 创建带有场景string的永久二维码
     *
     * @param scenceStr 场景的string值
     * @return 二维码链接
     * @throws WXException 异常信息
     */
    public String createLimitStrSceneQrCode(String scenceStr) throws WXException {
        return factory.getWxAccountService().createLimitStrSceneQrCode(scenceStr);
    }

    /**
     * 通过用户授权获取的auth code 拉取auth token
     *
     * @param authCode 微信回调获取的auth code
     * @return auth token
     * @throws WXException 异常信息
     */
    public WXOAuthTokenGetResponse getAuthToken(String authCode) throws WXException {
        return factory.getWxOAuthService().getOAuthAccessToken(authCode);
    }


    /**
     * 活的微信oauth url
     *
     * @param redirectUri 跳转的url
     * @param scope       scope取值
     * @param state       状态
     * @return oauth url
     */
    public String getWXOAuthUrl(String redirectUri, String scope, String state) {
        return factory.getWxOAuthService().wxAuthUrl(redirectUri, scope, state);
    }

    /**
     * 获取用户信息
     *
     * @param authCode code
     * @param lang     语言
     * @return 用户信息
     * @throws WXException 异常信息
     */
    public WXOAuthUserInfoGetResponse getAuthUserInfo(String authCode, String lang) throws WXException {
        return factory.getWxOAuthService().getAuthUserInfo(authCode, lang);
    }

    /**
     * 获取用户信息
     *
     * @param appId     app id
     * @param appSecret app secret
     * @param authCode  auth code
     * @param lang      语言
     * @return 用户信息
     * @throws WXException 异常信息
     */
    public WXOAuthUserInfoGetResponse getAuthUserInfo(String appId, String appSecret, String authCode, String lang) throws WXException {
        return factory.getWxOAuthService().getAuthUserInfo(appId, appSecret, authCode, lang);
    }

    /**
     * 获取用户信息
     *
     * @param accessToken access token
     * @param openId      open id
     * @param lang        语言
     * @return 用户信息
     * @throws WXException 异常信息
     */
    public WXOAuthUserInfoGetResponse getAuthUserInfo(String accessToken, String openId, String lang) throws WXException {
        return factory.getWxOAuthService().getAuthUserInfo(accessToken, openId, lang);
    }

    /**
     * 获取用户增减数据,最大时间跨度7天。
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 数据列表
     * @throws WXException 异常信息
     */
    public List<UserSummaryItem> getUserSummaryDataCube(Date start, Date end) throws WXException {
        return factory.getUserDataCubeService().getUserSummary(start, end);
    }

    /**
     * 获取累计用户数据,最大时间跨度7天。
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 数据列表
     * @throws WXException 异常信息
     */
    public List<UserCumulateItem> getUserCumulateDateCube(Date start, Date end) throws WXException {
        return factory.getUserDataCubeService().getUserCumulate(start, end);
    }

    /**
     * 获取图文群发每日数据,最大时间跨度1天。
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 数据列表
     * @throws WXException 异常信息
     */
    public List<ArticleData> getArticleSummaryDataCube(Date start, Date end) throws WXException {
        return factory.getArticleDataCubeService().getArticleSummary(start, end);
    }

    /**
     * 获取图文群发总数据,最大时间跨度1天。
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 数据列表
     * @throws WXException 异常信息
     */
    public List<ArticleTotalData> getArticleTotalDataCube(Date start, Date end) throws WXException {
        return factory.getArticleDataCubeService().getArticleTotal(start, end);
    }

    /**
     * 获取图文统计数据,最大时间跨度3天。
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 数据列表
     * @throws WXException 异常信息
     */
    public List<UserReadData> getArticleUserReadDataCube(Date start, Date end) throws WXException {
        return factory.getArticleDataCubeService().getArticleUserRead(start, end);
    }

    /**
     * 获取图文统计分时数据,最大时间跨度1天。
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 数据列表
     * @throws WXException 异常信息
     */
    public List<UserReadHourData> getArticleUserReadHourDataCube(Date start, Date end) throws WXException {
        return factory.getArticleDataCubeService().getArticleUserReadHour(start, end);
    }

    /**
     * 获取图文分享转发数据,最大时间跨度7天。
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 数据列表
     * @throws WXException 异常信息
     */
    public List<ArticleShareData> getArticleShareDataCube(Date start, Date end) throws WXException {
        return factory.getArticleDataCubeService().getArticleShare(start, end);
    }

    /**
     * 获取图文分享转发分时数据,最大时间跨度1天。
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 数据列表
     * @throws WXException 异常信息
     */
    public List<ArticleShareHourData> getArticleShareHourDataCube(Date start, Date end) throws WXException {
        return factory.getArticleDataCubeService().getArticleShareHour(start, end);
    }

    /**
     * 获取消息发送概况数据,最大时间跨度7天。
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 数据列表
     * @throws WXException 异常信息
     */
    public List<UpstreamMsgData> getUpstreamMsgDataCube(Date start, Date end) throws WXException {
        return factory.getMessageDataCubeService().getUpstreamMsg(start, end);
    }

    /**
     * 获取消息分送分时数据,最大时间跨度1天。
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 数据列表
     * @throws WXException 异常信息
     */
    public List<UpstreamMsgHourData> getUpstreamMsgHourDataCube(Date start, Date end) throws WXException {
        return factory.getMessageDataCubeService().getUpstreamMsgHour(start, end);
    }

    /**
     * 获取消息发送周数据,最大时间跨度30天。
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 数据列表
     * @throws WXException 异常信息
     */
    public List<UpstreamMsgData> getUpstreamMsgWeekDataCube(Date start, Date end) throws WXException {
        return factory.getMessageDataCubeService().getUpstreamMsgWeek(start, end);
    }

    /**
     * 获取消息发送月数据,最大时间跨度30天。
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 数据列表
     * @throws WXException 异常信息
     */
    public List<UpstreamMsgData> getUpstreamMsgMonthDataCube(Date start, Date end) throws WXException {
        return factory.getMessageDataCubeService().getUpstreamMsgMonth(start, end);
    }

    /**
     * 获取消息发送分布数据,最大时间跨度15天。
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 数据列表
     * @throws WXException 异常信息
     */
    public List<UpstreamMsgDistData> getUpstreamMsgDistDataCube(Date start, Date end) throws WXException {
        return factory.getMessageDataCubeService().getUpstreamMsgDist(start, end);
    }

    /**
     * 获取消息发送分布周数据,最大时间跨度30天。
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 数据列表
     * @throws WXException 异常信息
     */
    public List<UpstreamMsgDistData> getUpstreamMsgDistWeekDataCube(Date start, Date end) throws WXException {
        return factory.getMessageDataCubeService().getUpstreamMsgDistWeek(start, end);
    }

    /**
     * 获取消息发送分布月数据,最大时间跨度30天。
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 数据列表
     * @throws WXException 异常信息
     */
    public List<UpstreamMsgDistData> getUpstreamMsgDistMonthDataCube(Date start, Date end) throws WXException {
        return factory.getMessageDataCubeService().getUpstreamMsgDistMonth(start, end);
    }

    /**
     * 获取接口分析数据,最大时间跨度30天。
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 数据列表
     * @throws WXException 异常信息
     */
    public List<InterfaceData> getInterfaceSummaryDataCube(Date start, Date end) throws WXException {
        return factory.getInterfaceDataCubeService().getInterfaceSummary(start, end);
    }

    /**
     * 获取接口分析分时数据,最大时间跨度1天。
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 数据列表
     * @throws WXException 异常信息
     */
    public List<InterfaceHourData> getInterfaceHourSummaryDataCube(Date start, Date end) throws WXException {
        return factory.getInterfaceDataCubeService().getInterfaceHourSummary(start, end);
    }

    /**
     * 设置所属行业
     *
     * @param industry 公众号模板消息所属行业编号
     * @return 执行是否成功
     * @throws WXException 异常信息
     */
    public boolean setIndustryForTemplateMessage(Industry industry) throws WXException {
        return factory.getWxMessageService().setIndustryForTemplateMessage(industry);
    }

    /**
     * 获得模板ID
     *
     * @param shortId 模板库中模板的编号，有“TM**”和“OPENTMTM**”等形式
     * @return template id
     * @throws WXException 异常信息
     */
    public String getTemplateIdByShortId(String shortId) throws WXException {
        return factory.getWxMessageService().getTemplateIdByShortId(shortId);
    }

    /**
     * 发送模板消息
     *
     * @param messageInfo 模板消息内容
     * @return message id
     * @throws WXException 异常信息
     */
    public String sendTemplateMessage(MessageInfo messageInfo) throws WXException {
        return factory.getWxMessageService().sendTemplateMessage(messageInfo);
    }
}
