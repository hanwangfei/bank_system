package com.hwf.oa.service;


import com.hwf.oa.entity.Node;
import com.hwf.oa.entity.User;
import org.junit.Test;

import java.util.List;

public class UserServiceTest{

    UserService userService = new UserService();
    @Test
    public void testSelectById(){
        User user = (User)userService.selectById((long) 1);
        System.out.println(user.getUsername());

    }

    @Test
    public void testSelectByUserName(){
        User user = (User) userService.selectByUsername("m1");
        System.out.println(user.getUserId());
    }

    @Test
    public void testCheckLogin1(){
        userService.checkLogin("123","test");
    }
    @Test
    public void testCheckLogin2(){
        userService.checkLogin("m1","1234");
    }
    @Test
    public void testCheckLogin3(){
        userService.checkLogin("m1","test");
    }


    @Test
    public void testSelectNodeList(){
        List<Node> list = userService.selectNodeByUserId(3L);
        //System.out.println(list);
        for(Node n:list){
            System.out.print(n.getNodeName()+" ");
        }
    }

    @Test
    public void updateAmount(){
        userService.updateAmount(-500.0, 1L);
    }


}