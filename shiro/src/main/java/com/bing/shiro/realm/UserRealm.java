package com.bing.shiro.realm;

import com.bing.shiro.model.User;
import com.bing.shiro.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;

/**
 * Created by bing on 2018/2/24.
 */
public class UserRealm extends AuthorizingRealm {
    @Resource
    private UserService userService;

    /**
     * 认证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;

        User user = userService.getByUsername(usernamePasswordToken.getUsername());
        if (user == null) {
            return null;
        }

        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), getName());

        return simpleAuthenticationInfo;
    }

    /**
     * 授权
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (!SecurityUtils.getSubject().isAuthenticated()) {
            doClearCache(principals);
            SecurityUtils.getSubject().logout();
            return null;
        }

        String username = principals.getPrimaryPrincipal().toString();
        if (StringUtils.isNotBlank(username)) {
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

            simpleAuthorizationInfo.addRoles(userService.getUserRoleIdsByUsername(username));
            simpleAuthorizationInfo.addStringPermissions(userService.getStringPermissionsByUsername(username));
            return simpleAuthorizationInfo;
        }

        return null;
    }

}
