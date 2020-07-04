package cn.wjx.spring_security_demo.auth.security;

import cn.wjx.spring_security_demo.auth.utils.AccessAddressUtil;
import cn.wjx.spring_security_demo.auth.utils.JwtTokenUtil;
import cn.wjx.spring_security_demo.auth.utils.RedisUtil;
import cn.wjx.spring_security_demo.work.entity.UserInfo;
import cn.wjx.spring_security_demo.work.util.JZBResult;
import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: wjx
 * @date: 2020/06/24 15:00
 * @description: 用户登录成功时返回给前端的数据
 */
@Component
@Slf4j
public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Value("${token.expirationSeconds}")
    private int expirationSeconds;

    @Value("${token.validTime}")
    private int validTime;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        //获取请求的ip地址
        String ip = AccessAddressUtil.getIpAddress(httpServletRequest);
        Map<String,Object> map = new HashMap<>();
        map.put("ip",ip);

//        User userDetails = (User) authentication.getPrincipal();
        String userName = "";
        if(authentication.getPrincipal() instanceof  User){
            userName = ((User) authentication.getPrincipal()).getUsername();
        }else if(authentication.getPrincipal() instanceof  UserInfo){
            userName = ((UserInfo) authentication.getPrincipal()).getUsername();
        }else {
            userName = (String) authentication.getPrincipal();
        }

        String jwtToken = JwtTokenUtil.generateToken(userName, expirationSeconds, map);

        //刷新时间
        Integer expire = validTime*24*60*60*1000;
        //获取请求的ip地址
        String currentIp = AccessAddressUtil.getIpAddress(httpServletRequest);
        redisUtil.setTokenRefresh(jwtToken,userName,currentIp);
        log.info("用户{}登录成功，信息已保存至redis",userName);

        httpServletResponse.getWriter().write(JSON.toJSONString(JZBResult.build(30012,"用户登录成功",jwtToken)));
    }
}
