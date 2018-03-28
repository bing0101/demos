package com.bing.shiro.controller;

import com.bing.shiro.dto.AccessTokenReq;
import com.bing.shiro.dto.AccessTokenResp;
import com.bing.shiro.dto.AuthorizeReq;
import com.bing.shiro.dto.AuthorizeResp;
import com.bing.shiro.service.OAuth2Service;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * Created by bing on 2018/2/28.
 */
@Controller
@RequestMapping("/oauth2")
public class OAuth2Controller {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private OAuth2Service oAuth2Service;

    @RequestMapping(value = "/authorize", method = RequestMethod.GET)
    public String authorizationCode(AuthorizeReq req) {

        //1. 检查是否存在该client——若不存在该clientId，返回error="invalid_client", error_description="Bad client credentials"
        //2. 检查是否登录，跳转至登录页面，登录接口返回该获取授权码接口，登录接口应支持指定跳转至哪一个地址
        //3. 保存授权码、clientId，重定向uri映射
        //4. 跳转至重定向uri，并在地址上带上参数授权码和state


        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {//若没登录

        }

        //生成授权码
        OAuthIssuer oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
        try {
            String authorizationCode = oAuthIssuer.authorizationCode();
            resp.setSuccess(true);
            resp.setCode(authorizationCode);
            resp.setState(req.getState());

            String username = (String)subject.getPrincipal();
            oAuth2Service.addAuthorizationCode(username, authorizationCode);
        } catch (OAuthSystemException e) {
            logger.error("获取授权码异常", e);
            resp.setSuccess(false);
            resp.setErrorCode("sys_error");
            resp.setErrorMsg("系统异常");
        }

        return resp;
    }

    @RequestMapping(value = "/access_token", method = RequestMethod.POST)
    public AccessTokenResp accessToken(AccessTokenReq req) {

        //1. 校验client secret
        //2. 校验grant_type
        //3. 校验授权码（只能用一次）
        //4. 校验重定向uri（授权码、clientId、重定向uri应该一一对应）

        AccessTokenResp resp = new AccessTokenResp();

        if (StringUtils.isBlank(req.getRedirectUri())) {
            resp.setSuccess(false);
            resp.setErrorCode("redirect_uri_is_blank");
            resp.setErrorMsg("redirectUri由client提供");
        }

        if (oAuth2Service.getByClientId(req.getClientId()) == null) {//不存在该client
            resp.setSuccess(false);
            resp.setErrorCode(OAuthError.TokenResponse.INVALID_CLIENT);
            resp.setErrorMsg("错误的clientId");
        }

        if (!GrantType.AUTHORIZATION_CODE.toString().equals(req.getGrantType())) {
            resp.setSuccess(false);
            resp.setErrorCode(OAuthError.TokenResponse.UNSUPPORTED_GRANT_TYPE);
            resp.setErrorMsg("暂时只支持授权码模式");
        }

        if (!oAuth2Service.checkAuthorizationCode(req.getAuthorizationCode())) {
            resp.setSuccess(false);
            resp.setErrorCode(OAuthError.TokenResponse.INVALID_GRANT);
            resp.setErrorMsg("错误的授权码");
        }

        OAuthIssuer oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
        try {
            String accessToken = oAuthIssuer.accessToken();
            resp.setSuccess(true);
            resp.setAccessToken(accessToken);

            Subject subject = SecurityUtils.getSubject();
            String username = (String)subject.getPrincipal();
            oAuth2Service.addAccessToken(username, accessToken);
        } catch (OAuthSystemException e) {
            logger.error("获取授权码异常", e);
            resp.setSuccess(false);
            resp.setErrorCode("sys_error");
            resp.setErrorMsg("系统异常");
        }

        return resp;
    }

    @RequestMapping("/check_access_token")
    public void checkAccessToken(AccessTokenReq req) {
        //访问所有资源之前，需验证access_token是否正确
    }

    @RequestMapping("/confirm_access_token")
    public void confirmAccessToken(AccessTokenReq req) {
        //用户确认授权提交
    }

    @RequestMapping("/error")
    public void error(AccessTokenReq req) {
        //授权服务错误信息
    }

}
