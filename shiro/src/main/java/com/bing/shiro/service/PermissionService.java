package com.bing.shiro.service;

import com.bing.shiro.mapper.PermissionMapper;
import com.bing.shiro.mapper.RolePermissionMapper;
import com.bing.shiro.model.Permission;
import com.bing.shiro.model.RolePermission;
import com.bing.shiro.model.RolePermissionExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bing on 2018/2/24.
 */
@Service
public class PermissionService {
    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private RolePermissionMapper rolePermissionMapper;

    public Permission getById(long id) {
        return permissionMapper.selectByPrimaryKey(id);
    }



    public List<Permission> getPermissionsByRoleId(long roleId) {
        List<Permission> permissions = new ArrayList<>();

        RolePermissionExample example = new RolePermissionExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        List<RolePermission> rolePermissions = rolePermissionMapper.selectByExample(example);
        rolePermissions.forEach(it -> {
            Permission permission = getById(it.getPermissionId());
            if (permission != null) {
                permissions.add(permission);
            } else {
                throw new RuntimeException("不存在该permission");
            }
        });

        return permissions;
    }
}
