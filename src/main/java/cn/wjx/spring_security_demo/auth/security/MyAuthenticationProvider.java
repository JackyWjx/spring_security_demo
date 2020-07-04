package cn.wjx.spring_security_demo.auth.security;

import cn.wjx.spring_security_demo.work.util.HttpClientUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * @author wjx
 * @date 2020/7/1 11:33
 * @description 密码验证规则（方式一：实现AuthenticationProvider）
 */
@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private CustomUserDetailsService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String presentedPassword = (String)authentication.getCredentials();
        // 根据用户名获取用户信息
        UserDetails userDeatils = null;
        userDeatils=userService.loadUserByUsername(username);
        //密码校验
        JSONObject result = HttpClientUtil.getPwdForNet("https://api.jizhibao.com.cn/Account/EncryptStr?keyword=" + presentedPassword + "&&md5=1");
        String password = result.get("data").toString();
        if(!userDeatils.getPassword().equals(password)){
            //密码不同
            throw new DisabledException("Wrong password.");
        }
        return  new UsernamePasswordAuthenticationToken(username, presentedPassword, userDeatils.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}