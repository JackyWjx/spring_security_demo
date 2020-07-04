package cn.wjx.spring_security_demo.work.controller;

import cn.wjx.spring_security_demo.work.service.UserService;
import cn.wjx.spring_security_demo.work.util.JZBResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wjx
 * @date 2020/7/4 10:10
 * @description: 用户控制层
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/getUserInfo")
    public JZBResult getUserInfo(@Param("userName") String userName){
        try{
            return JZBResult.ok(userService.getUserInfo(userName));
        }catch (Exception e){
            e.printStackTrace();
            return JZBResult.build(30000,"系统错误",e.getMessage());
        }
    }
}
