package com.tian.shirostudy.common;

import com.tian.shirostudy.dao.entity.Account;
import com.tian.shirostudy.service.AccountService;
import org.apache.shiro.authc.*;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 需要查询数据库, 并得到正确的数据
 * Created by Administrator on 2018/1/17 0017.
 */
@Component("jdbcRealm")
public class ShiroRealm extends AuthenticatingRealm {
    @Autowired
    private AccountService accountService;

    /**
     * 获取认证消息, 如果数据库中没有数据, 返回null, 如果得到正确的用户名和密码, 返回指定类型的对象.
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
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
        return new SimpleAuthenticationInfo(username,account.getPassword(),this.getName());
    }
}
