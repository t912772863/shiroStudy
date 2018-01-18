package com.tian.shirostudy;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2018/1/16 0016.
 */
public class HelloWord {
     private static final Logger logger = LoggerFactory.getLogger(HelloWord.class);

    public static void main(String[] args) {
        /**
         * 1. 获取安全管理器
         * 2. 获取用户
         * 3. 用户登录验证
         * 4. 权限管理
         * 5. 角色管理
         * 6. session: 用户登录到用户退出,作用域
         */

        // 获取安全管理器
        Factory<SecurityManager> factory= new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        // 需要设置安全管理器
        SecurityUtils.setSecurityManager(securityManager);
        // 获取subject对象, 即将登录的用户
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();

        session.setAttribute("name", "田雄");
        String name = (String)session.getAttribute("name");
        if(name != null){
            logger.info("shiro已经帮我们从session中获取到了name的值: "+name);
        }

        if(currentUser.isAuthenticated() == false){
            // usernamepasswordtoken
            UsernamePasswordToken token = new UsernamePasswordToken("lonestarr","vespa");
            token.setRememberMe(true);
            try{
                currentUser.login(token);
                logger.info("用户名密码正确, 登录成功");
            }catch (UnknownAccountException e){
                logger.error("帐户不存在", e);
            }catch (IncorrectCredentialsException e){
                logger.error("密码错误", e);
            }catch (LockedAccountException e){
                logger.error("帐号已登录", e);
            }catch (AuthenticationException e){
                logger.error("帐号认证异常", e);
            }

        }


        // 用户角色相关api
        if(currentUser.hasRole("goodguy")){
            logger.info("当前用户有goodguy角色");
        }else{
            logger.info("当前用户不具有goodguy角色");
        }


        // 用户是否有指定权限
        if(currentUser.isPermitted("winnebago:drive:eagle5")){
            logger.info("当前用户有winnebago:drive:eagle5权限");
        }else {
            logger.info("当前用户没有winnebago:drive:eagle5这个权限");
        }

        // 退出登录
        currentUser.logout();

    }
}
