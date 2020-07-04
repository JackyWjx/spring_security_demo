package cn.wjx.spring_security_demo.auth.security;

import cn.wjx.spring_security_demo.work.util.HttpClientUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author wjx
 * @date 2020/7/3 11:14
 * @deprecated 自定义密码验证规则（方式二）
 */
public class CustomPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        String pwd = charSequence.toString();
        System.out.println("前端传过来的明文密码:" + pwd);
        //密码校验
        JSONObject result = HttpClientUtil.getPwdForNet("https://api.jizhibao.com.cn/Account/EncryptStr?keyword=" + pwd + "&&md5=1");
        String password = result.get("data").toString();

        System.out.println("加密后:" + password);
        return password;
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        String pwd = charSequence.toString();
        System.out.println("前端传过来的明文密码:" + pwd);
        //密码校验
        JSONObject result = HttpClientUtil.getPwdForNet("https://api.jizhibao.com.cn/Account/EncryptStr?keyword=" + pwd + "&&md5=1");
        String password = result.get("data").toString();
        //密码校验
        if((password).equals(s)){
            return true;
        }


        throw new DisabledException("--密码错误--");
    }

}
