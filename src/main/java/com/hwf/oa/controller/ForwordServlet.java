package com.hwf.oa.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 页面挑战servlet
 */

@WebServlet(name = "ForwardServlet",urlPatterns = "/forward/*")
public class ForwordServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        /**
         * /forward/form
         * /forward/a/b/c/form
         */
        String subUri = uri.substring(1);  //去除第一个斜杠，此时第二个斜杠在字符串中就是第一个斜杠
        String page = subUri.substring(subUri.indexOf("/"));
        req.getRequestDispatcher(page+".ftl").forward(req,resp);
    }
}
