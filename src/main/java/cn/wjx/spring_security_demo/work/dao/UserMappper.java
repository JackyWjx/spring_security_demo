package cn.wjx.spring_security_demo.work.dao;

import cn.wjx.spring_security_demo.work.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wjx
 * @date 2020/7/4 10:28
 * @description:
 */
@Mapper
public interface UserMappper {

    /**
     * 根据用户名获取用户信息
     * @param userName
     * @return
     */
    UserInfo getUserInfo(String userName);
}
