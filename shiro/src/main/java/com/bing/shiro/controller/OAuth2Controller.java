package com.bing.shiro.controller;

import com.bing.shiro.dto.AccessTokenReq;
import com.bing.shiro.dto.AccessTokenResp;
import com.bing.shiro.dto.AuthorizationCodeReq;
import com.bing.shiro.dto.AuthorizationCodeResp;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by bing on 2018/2/28.
 */
@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private OAuth2Service oAuth2Service;

    @RequestMapping("/authorizationCode")
    public AuthorizationCodeResp authorizationCode(AuthorizationCodeReq req) {
        AuthorizationCodeResp resp = new AuthorizationCodeResp();

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

        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {//若没登录
            resp.setSuccess(false);
            resp.setErrorCode("unlogin");
            resp.setErrorMsg("未登录");
            resp.setData("http://localhost:8080/shiro/login");
        }

        if (!ResponseType.CODE.toString().equals(req.getResponseType())) {//暂时只支持code
            resp.setSuccess(false);
            resp.setErrorCode("invalid_response_type");
            resp.setErrorMsg("暂时只支持code");
        }

        //生成授权码
        OAuthIssuer oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
        try {
            String authorizationCode = oAuthIssuer.authorizationCode();
            resp.setSuccess(true);
            resp.setAuthorizationCode(authorizationCode);
            resp.setRedirectUri(req.getRedirectUri());
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

    @RequestMapping("/accessToken")
    public AccessTokenResp accessToken(AccessTokenReq req) {
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

}
