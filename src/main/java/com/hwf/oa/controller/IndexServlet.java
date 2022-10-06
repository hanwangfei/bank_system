package com.hwf.oa.controller;


import com.hwf.oa.entity.Node;
import com.hwf.oa.entity.Person;
import com.hwf.oa.entity.User;
import com.hwf.oa.service.PersonService;
import com.hwf.oa.service.UserService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "IndexServlet", value = "/index")
public class IndexServlet extends HttpServlet {
    private UserService userService = new UserService();
    private PersonService personService = new PersonService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        //得到当前登录用户
        User user = (User) session.getAttribute("login_user");
        Person person = personService.selectByIdentityId(user.getIdentityId());

        //获取登录用户可用登录模块列表
        List<Node> nodeList = userService.selectNodeByUserId(user.getUserId());
        //放入请求属性
        request.setAttribute("node_list",nodeList);
        session.setAttribute("current_person",person);  //这里的employee如果只是为index.ftl服务，则放入请求也可以，但由于考虑到实际上用户信息会在多处使用到，故提示其生存周期，放入session中
        //请求派发至ftl进行展现
        request.getRequestDispatcher("/index.ftl").forward(request,response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
