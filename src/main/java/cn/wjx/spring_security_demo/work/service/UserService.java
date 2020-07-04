package cn.wjx.spring_security_demo.work.service;

import cn.wjx.spring_security_demo.work.entity.UserInfo;

/**
 * @author wjx
 * @date 2020/7/4 10:26
 * @description: 用户业务层接口
 */
public interface UserService {

    /**
     * 根据用户名获取用户信息
     * @param userName
     * @return
     */
    UserInfo getUserInfo(String userName);
}
