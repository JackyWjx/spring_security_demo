package cn.wjx.spring_security_demo.auth.security;

import cn.wjx.spring_security_demo.work.entity.UserInfo;
import cn.wjx.spring_security_demo.work.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wjx
 * @date 2020/7/2 15:48
 */
@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String ssoId) throws UsernameNotFoundException {
        UserInfo user = userService.getUserInfo(ssoId);
        System.out.println("User : "+user);
        if(user==null){
            System.out.println("User not found");
            throw new UsernameNotFoundException("Username not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUserid(), user.getPassword(),
                true, true, true, true, getGrantedAuthorities(1));
    }

    private List<GrantedAuthority> getGrantedAuthorities(int userid){
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        /*List<UserProfile> list = userProfileMapper.findUserProfileByUserid(userid);

        for(UserProfile userProfile : list){
            System.out.println("UserProfile : "+userProfile);
            authorities.add(new SimpleGrantedAuthority("ROLE_"+userProfile.getType()));
        }*/
        authorities.add(new SimpleGrantedAuthority("ROLE_AMIN"));
        System.out.print("authorities :"+authorities);
        return authorities;
    }
}