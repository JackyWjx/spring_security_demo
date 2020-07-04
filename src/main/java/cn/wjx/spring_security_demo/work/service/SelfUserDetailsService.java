package cn.wjx.spring_security_demo.work.service;


import cn.wjx.spring_security_demo.work.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: wjx
 * @date: 2020/06/24 15:00
 * @description: 用户认证、权限
 */
@Component
public class SelfUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //通过username查询用户
        UserInfo user = userService.getUserInfo(username);
        if(user == null){
            //仍需要细化处理
            throw new UsernameNotFoundException("该用户不存在");
        }

        Set authoritiesSet = new HashSet();
        // 模拟从数据库中获取用户角色
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");

        authoritiesSet.add(authority);
        user.setAuthorities(authoritiesSet);

        return user;
    }
}
