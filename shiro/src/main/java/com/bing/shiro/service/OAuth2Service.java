package com.bing.shiro.service;

import com.bing.shiro.mapper.Oauth2ClientMapper;
import com.bing.shiro.model.Oauth2Client;
import com.bing.shiro.model.Oauth2ClientExample;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by bing on 2018/2/28.
 */
@Component
public class OAuth2Service {
    @Resource
    private Oauth2ClientMapper oauth2ClientMapper;

    public Oauth2Client getByClientId(String clientId) {
        Oauth2ClientExample example = new Oauth2ClientExample();
        example.createCriteria().andClientIdEqualTo(clientId);

        List<Oauth2Client> clients = oauth2ClientMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(clients)) {
            return null;
        }
        return clients.get(0);
    }

    /**
     * 10分钟有效期
     * @param username
     * @param authorizationCode
     */
    public void addAuthorizationCode(String username, String authorizationCode) {

    }

    public void addAccessToken(String username, String authorizationCode) {

    }

    public boolean checkAuthorizationCode(String authorizationCode) {
        return true;
    }

}
