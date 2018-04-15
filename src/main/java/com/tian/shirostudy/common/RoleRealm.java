package com.tian.shirostudy.common;

import com.tian.shirostudy.dao.entity.Account;
import com.tian.shirostudy.dao.entity.Role;
import com.tian.shirostudy.service.AccountService;
import com.tian.shirostudy.service.RoleService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2018/4/15 0015.
 */
@Component("roleRealm")
public class RoleRealm extends AuthorizingRealm {
    @Autowired
    private RoleService roleService;
    @Autowired
    private AccountService accountService;

    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = null;
        //        // 3. 调用数据库方法, 是否存在指定用户
        Account account = accountService.queryByUsername(principalCollection.toString());
        if(account == null){
            return null;
        }
        List<Role> roleList = roleService.queryByAccountId(account.getId());
        Set<String> roles = new HashSet<String>();
        for(Role r: roleList){
            roles.add(r.getName());
        }
        info = new SimpleAuthorizationInfo(roles);
        return info;
    }

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 1. 将token转换成UsernamePasswordToken类型
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
        // 2. 获取用户名
        String username = token.getUsername();
        // 3. 调用数据库方法, 是否存在指定用户
        Account account = accountService.queryByUsername(username);
        // 4. 如果查询到, 封装结果返回
        // 5. 如果没有查到, 拋出一个异常
        if(account == null){
            throw new AuthenticationException();
        }
        // 用用户名做盐值(因为加了盐值, 所以用户的密码相同, 数据库存的也不一样, 而且非明文)
        ByteSource byteSource = ByteSource.Util.bytes(account.getUsername());
        return new SimpleAuthenticationInfo(account.getUsername(),account.getPassword(),byteSource,this.getName());
    }

}
