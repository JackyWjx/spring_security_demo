package cn.wjx.spring_security_demo.auth.config;



import cn.wjx.spring_security_demo.auth.filters.JwtAuthenticationTokenFilter;
import cn.wjx.spring_security_demo.auth.security.*;
import cn.wjx.spring_security_demo.work.service.SelfUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @author: wjx
 * @date: 2020/07/04 10:44
 * @description:统一拦截请求
 */
@Configuration
public class SpringSecurityConf extends WebSecurityConfigurerAdapter {

    @Resource
    @Qualifier("customUserDetailsService")
    CustomUserDetailsService customUserDetailsService;

    /**
     *  未登陆时返回 JSON 格式的数据给前端（否则为 html）
     */
    @Resource
    AjaxAuthenticationEntryPoint authenticationEntryPoint;

    /**
     * 登录成功返回的 JSON 格式数据给前端（否则为 html）
     */
    @Resource
    AjaxAuthenticationSuccessHandler authenticationSuccessHandler;

    /**
     * 登录失败返回的 JSON 格式数据给前端（否则为 html）
     */
    @Resource
    AjaxAuthenticationFailureHandler authenticationFailureHandler;

    /**
     * 注销成功返回的 JSON 格式数据给前端（否则为 登录时的 html）
     */
    @Resource
    AjaxLogoutSuccessHandler logoutSuccessHandler;

    /**
     * 无权访问返回的 JSON 格式数据给前端（否则为 403 html 页面）
     */
    @Resource
    AjaxAccessDeniedHandler accessDeniedHandler;

    /**
     * 自定义User验证
     */
    @Autowired
    SelfUserDetailsService selfUserDetailsService;
    /**
     * JWT 拦截器
     */
    @Resource
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Resource
    private MyAuthenticationProvider authProvider;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 去掉 CSRF
        http.csrf().disable()
                .cors().and() //跨域开启
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 使用 JWT，关闭token
                .and()
                .httpBasic().authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .authorizeRequests()//定义哪些URL需要被保护、哪些不需要被保护

                .anyRequest()//任何请求,登录后可以访问
                .access("@rbacauthorityservice.hasPermission(request,authentication)") // RBAC 动态 url 认证

                .and()
                .formLogin()  //开启登录, 定义当需要用户登录时候，转到的登录页面
//                .loginPage("/test/login.html")
//                .loginProcessingUrl("/login")
                .successHandler(authenticationSuccessHandler) // 登录成功
                .failureHandler(authenticationFailureHandler) // 登录失败
                .permitAll()

                .and()
                .logout()//默认注销行为为logout
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll();

        // 记住我
        http.rememberMe().rememberMeParameter("remember-me")
                .userDetailsService(selfUserDetailsService).tokenValiditySeconds(1000);

        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler); // 无权访问 JSON 格式的数据
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class); // JWT Filter

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //内存验证
        //auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser("wjx").password(new BCryptPasswordEncoder().encode("123")).roles("ADMIN");
        //自定义规则
        auth.userDetailsService(selfUserDetailsService).passwordEncoder(new CustomPasswordEncoder());
//        auth.authenticationProvider(authProvider);
    }

}
