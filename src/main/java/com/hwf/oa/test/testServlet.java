package com.hwf.oa.test;

import com.hwf.oa.utils.MybatisUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/test")
public class testServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("result", (String)MybatisUtils.excuteQuery(sqlSession -> sqlSession.selectOne("test.sample")));
        req.getRequestDispatcher("/test.ftl").forward(req,resp);

    }
}
