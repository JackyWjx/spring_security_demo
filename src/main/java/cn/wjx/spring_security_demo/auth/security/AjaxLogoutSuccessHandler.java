package cn.wjx.spring_security_demo.auth.security;

import cn.wjx.spring_security_demo.auth.utils.DateUtil;
import cn.wjx.spring_security_demo.auth.utils.RedisUtil;
import cn.wjx.spring_security_demo.work.util.JZBResult;
import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: wjx
 * @date: 2020/06/24 15:00
 * @description: 登出成功
 */
@Component
@Slf4j
public class AjaxLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        String authHeader = httpServletRequest.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String authToken = authHeader.substring("Bearer ".length());
            //将token放入黑名单中
            redisUtil.hset("blacklist", authToken, DateUtil.getTime());
            log.info("用户登出成功！token：{}已加入redis黑名单",authToken);
        }
        httpServletResponse.getWriter().write(JSON.toJSONString(JZBResult.build(30014,"用户登出成功")));
    }

}
