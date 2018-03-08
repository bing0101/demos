package com.bing.shiro.service;

import com.bing.shiro.mapper.UserMapper;
import com.bing.shiro.mapper.UserRoleMapper;
import com.bing.shiro.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by bing on 2018/2/24.
 */
@Service
public class UserService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private PermissionService permissionService;

    public User getByUsername(String username) {
        UserExample example = new UserExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<User> users = userMapper.selectByExample(example);

        if (users.size() == 0) {
            return null;
        }

        if (users.size() > 1) {
            logger.error("username:{}包含多条记录！", username);
        }

        return users.get(0);
    }

    public List<UserRole> getUserRolesByUsername(String username) {
        User user = getByUsername(username);

        UserRoleExample example = new UserRoleExample();
        example.createCriteria().andUserIdEqualTo(user.getId());

        return userRoleMapper.selectByExample(example);
    }

    public Set<String> getUserRoleIdsByUsername(String username) {
        List<UserRole> userRoles = getUserRolesByUsername(username);
        Set<String> userRoleIds = new HashSet<>();
        userRoles.forEach(it -> userRoleIds.add(it.getRoleId().toString()));
        return userRoleIds;
    }

    public Set<String> getStringPermissionsByUsername(String username) {
        Set<String> stringPermissions = new HashSet<>();
        List<UserRole> userRoles = getUserRolesByUsername(username);
        userRoles.forEach(it -> {
            List<Permission> permissions = permissionService.getPermissionsByRoleId(it.getRoleId());
            permissions.forEach(it2 -> stringPermissions.add(it2.getPermission()));
        });

        return stringPermissions;
    }
}
