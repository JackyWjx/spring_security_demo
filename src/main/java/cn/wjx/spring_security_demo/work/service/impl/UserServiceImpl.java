package cn.wjx.spring_security_demo.work.service.impl;

import cn.wjx.spring_security_demo.work.dao.UserMappper;
import cn.wjx.spring_security_demo.work.entity.UserInfo;
import cn.wjx.spring_security_demo.work.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wjx
 * @date 2020/7/4 10:26
 * @description:
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMappper userMappper;

    @Override
    public UserInfo getUserInfo(String userName) {
        return userMappper.getUserInfo(userName);
    }
}
