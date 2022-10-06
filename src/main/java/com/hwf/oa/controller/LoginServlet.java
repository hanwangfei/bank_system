package com.hwf.oa.controller;

import com.alibaba.fastjson.JSON;
import com.hwf.oa.entity.User;
import com.hwf.oa.service.UserService;
import com.hwf.oa.service.exception.BussinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LoginServlet", value = "/check_login")
public class LoginServlet extends HttpServlet {
    private UserService userService = new UserService();
    Logger logger = LoggerFactory.getLogger(LoginServlet.class);  //日志输出组件
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        //接收用户输入
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        Map<String,Object> result = new HashMap<>();
        //调用业务逻辑
        try {
            User user = userService.checkLogin(username,password);
            result.put("code",0);  //编码为0，登录成功
            result.put("message","success");
            result.put("redirect_url","/index");  //登录成功，放入跳转的url地址
            req.getSession().setAttribute("login_user",user);  //将用户登录信息存放到当前会话窗口中
        }catch (BussinessException ex){
            logger.error(ex.getMessage(),ex);
            result.put("code",ex.getCode());
            result.put("message",ex.getMessage());
        }catch (Exception ex){
            logger.error(ex.getMessage(),ex);
            result.put("code",ex.getClass().getSimpleName());
            result.put("message",ex.getMessage());
        }
        //返回json结果
        String json = JSON.toJSONString(result);
        resp.getWriter().println(json);

    }
}
