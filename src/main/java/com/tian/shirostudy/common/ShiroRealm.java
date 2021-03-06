package com.tian.shirostudy.common;

import com.tian.shirostudy.dao.entity.Account;
import com.tian.shirostudy.service.AccountService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.ByteSource;
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

    public ShiroRealm(){
        super();
        // 设置加密器, 也可以不用, 加密器用MD5算法, 加密3次,(这里设置的加密会对登录的用户密码进行该规则加密, 再与数据库
        // 查询出来的结果进行对比.也就是适用于, 接口传的是明文, 数据库存的是密文)
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher("MD5");
        hashedCredentialsMatcher.setHashIterations(3);
        this.setCredentialsMatcher(hashedCredentialsMatcher);
    }

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
        // 用用户名做盐值(因为加了盐值, 所以用户的密码相同, 数据库存的也不一样, 而且非明文)
        ByteSource byteSource = ByteSource.Util.bytes(account.getUsername());
        return new SimpleAuthenticationInfo(account.getUsername(),account.getPassword(),byteSource,this.getName());
    }
}
