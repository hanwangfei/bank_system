package com.hwf.oa.controller;

import com.alibaba.fastjson.JSON;
import com.hwf.oa.entity.Form;

import com.hwf.oa.entity.Notice;
import com.hwf.oa.entity.User;
import com.hwf.oa.service.FormService;
import com.hwf.oa.service.NoticeService;
import com.hwf.oa.service.UserService;
import com.hwf.oa.service.exception.BussinessException;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name ="WorkServlet",urlPatterns = "/work/*")
public class WorkServlet extends HttpServlet {

    UserService userService = new UserService();
    NoticeService noticeService = new NoticeService();
    FormService formService = new FormService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        //http://localhost/leave/ck
        String uri = req.getRequestURI();
        String methodName = uri.substring(uri.lastIndexOf("/")+1);

        if(methodName.equals("ck")){
            this.ck(req,resp,true);
        } else if(methodName.equals("qk")){
            this.ck(req,resp,false);  //存取款的唯一区别就是将count由正数改为负数
        }else if(methodName.equals("cx")){
            this.cx(req,resp);
        }else if(methodName.equals("zz")){
            this.zz(req,resp);
        }else if(methodName.equals("gm")){
            this.gm(req,resp);
        }else if(methodName.equals("jk")){
            this.jk(req,resp);
        }else if(methodName.equals("oplist")){
            this.getOpFormList(req,resp);
        }else if(methodName.equals("shjk")){
            this.shjk(req,resp);
        }else if(methodName.equals("hk")){
            this.hk(req,resp);
        }else if(methodName.equals("userList")){
            this.getUserFormList(req,resp);
        }else if(methodName.equals("zhgs")){
            this.zhgs(req,resp,true);
        }else if(methodName.equals("zhjg")){
            this.zhgs(req,resp,false);  //这里其实与账号挂失是相同的逻辑，只是一个将state改为0，一个改为1
        }else if(methodName.equals("kh")){
            this.kh(req,resp);
        }else if(methodName.equals("xh")){
            this.xh(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req,resp);
    }



    private void ck(HttpServletRequest req,HttpServletResponse resp,boolean flag) throws IOException {
        User user = (User) req.getSession().getAttribute("login_user");
        double count = Double.parseDouble(req.getParameter("count"));

        Map result = new HashMap();
        try {
            userService.ck(user,count,flag);
            result.put("code",0);
            result.put("message","success");
        }catch (Exception e){
            result.put("code",e.getClass().getSimpleName());
            result.put("message",e.getMessage());
        }
        String json = JSON.toJSONString(result);
        resp.getWriter().println(json);
    }

    private void cx(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("login_user");
        Map result = new HashMap();
        BigDecimal amount = userService.selectById(user.getUserId()).getAmount();
        user.setAmount(amount);
        result.put("count",user.getAmount()+"元");
        result.put("code",0);
        result.put("message","success");
        Notice notice = new Notice();
        notice.setReciverId(user.getUserId());
        notice.setContent("尊敬的用户您好，您当前账户余额为："+user.getAmount()+"元");
        notice.setCreateTime(new Date());
        noticeService.inset(notice);

        String json = JSON.toJSONString(result);
        resp.getWriter().println(json);
    }

    private void zz(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        User user =(User) req.getSession().getAttribute("login_user");
        String outName= req.getParameter("outName");
        double count = Double.parseDouble(req.getParameter("count"));
        Map result = new HashMap();

        try {
            if(userService.selectByUsername(outName)==null)
                throw new BussinessException("Z002","转出账户不存在");
            userService.transfer(user, userService.selectByUsername(outName).getUserId(),count);
            result.put("code",0);
            result.put("message","success");
        }catch (BussinessException e){
            result.put("code",e.getCode());
            result.put("message",e.getMessage());
        }catch (Exception e){
            result.put("code",e.getClass().getSimpleName());
            result.put("message",e.getMessage());
        }

        //返回json结果
        String json = JSON.toJSONString(result);
        resp.getWriter().println(json);
    }

    private void gm(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        User user =(User) req.getSession().getAttribute("login_user");
        String password=req.getParameter("password");
        String newPassword=req.getParameter("newPassword");
        String newPassword1=req.getParameter("newPassword1");
        Map result = new HashMap();
        try {
            userService.changePassword(user,password,newPassword,newPassword1);
            result.put("code",0);
            result.put("message","success");
        }catch (BussinessException e){
            result.put("code",e.getCode());
            result.put("message",e.getMessage());

        }catch (Exception e){
            result.put("code",e.getClass().getSimpleName());
            result.put("message",e.getMessage());

        }
        //返回json结果
        String json = JSON.toJSONString(result);
        resp.getWriter().println(json);

    }

    private void jk(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        User user =(User) req.getSession().getAttribute("login_user");
        String formType = req.getParameter("formType");
        String strStartTime = req.getParameter("startTime");
        String strEndTime = req.getParameter("endTime");
        String reason = req.getParameter("reason");
        String operatorName=req.getParameter("operatorName");
        BigDecimal count = new BigDecimal(req.getParameter("count"));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH");
        Map result = new HashMap();
        try {
            Form form = new Form();
            form.setUserId(user.getUserId());
            form.setFormType(Integer.valueOf(formType));
            form.setOperatorName(operatorName);
            form.setCreateTime(new Date());
            form.setStartTime(sdf.parse(strStartTime));
            form.setEndTime(sdf.parse(strEndTime));
            form.setReason(reason);
            form.setState("process");
            form.setHasReturn(0);
            form.setCount(count);
            formService.createForm(form);

            result.put("code",0);
            result.put("message","success");

        }catch (Exception e){

            result.put("code",e.getClass().getSimpleName());
            result.put("message",e.getMessage());

        }
        //组织响应数据
        String json = JSON.toJSONString(result);
        resp.getWriter().println(json);

    }

    private void getOpFormList(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("login_user");
        List<Map> formList = formService.getOpFormList("process",user.getUserId());
        Map result = new HashMap();
        result.put("code",0);
        result.put("msg","");
        result.put("count",formList.size());
        result.put("data",formList);

        String json = JSON.toJSONString(result);
        resp.getWriter().println(json);

    }

    private void getUserFormList(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("login_user");
        List<Map> formList = formService.getUserFormList("approved",user.getUserId());
        Map result = new HashMap();
        result.put("code",0);
        result.put("msg","");
        result.put("count",formList.size());
        result.put("data",formList);

        String json = JSON.toJSONString(result);
        resp.getWriter().println(json);
    }

    //审核贷款业务
    private void shjk(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String formId = request.getParameter("formId");
        String result = request.getParameter("result");
        String resultReason = request.getParameter("resultReason");
        User user = (User)request.getSession().getAttribute("login_user");

        Map mpResult = new HashMap();
        try {
            formService.shjk(Long.parseLong(formId),user.getUserId(),result,resultReason);
            mpResult.put("code","0");
            mpResult.put("message","success");
        }catch (Exception e){
            mpResult.put("code",e.getClass().getSimpleName());
            mpResult.put("message",e.getMessage());
        }
        String json = JSON.toJSONString(mpResult);
        response.getWriter().println(json);

    }
    //用户还款
    private void hk(HttpServletRequest request,HttpServletResponse response) throws IOException {
        User user = (User)request.getSession().getAttribute("login_user");
        String formId = request.getParameter("formId");
        Map mpResult = new HashMap();
        try {
            formService.hk(Long.valueOf(formId));
            mpResult.put("code","0");
            mpResult.put("message","success");


        }catch (Exception e){
            mpResult.put("code",e.getClass().getSimpleName());
            mpResult.put("message",e.getMessage());

        }

        String json = JSON.toJSONString(mpResult);
        response.getWriter().println(json);
    }

    private void zhgs(HttpServletRequest req,HttpServletResponse resp,boolean flag) throws IOException {
        User user = (User)req.getSession().getAttribute("login_user");
        String userName = req.getParameter("userName");
        String password=req.getParameter("password");
        Map mpResult = new HashMap();
        try {
            userService.zzgs(user,userName,password,flag);

            mpResult.put("code","0");
            mpResult.put("message","success");


        }catch (Exception e){
            mpResult.put("code",e.getClass().getSimpleName());
            mpResult.put("message",e.getMessage());

        }

        String json = JSON.toJSONString(mpResult);
        resp.getWriter().println(json);


    }


    private void kh(HttpServletRequest req,HttpServletResponse resp) throws IOException {

        String userName = req.getParameter("userName");
        String password=req.getParameter("password");
        String identityId=req.getParameter("identityId");
        User user = new User();
        user.setUsername(userName);
        user.setPassword(password);
        user.setIdentityId(Long.valueOf(identityId));
        Map mpResult = new HashMap();
        try {
            userService.kh(user);

            mpResult.put("code","0");
            mpResult.put("message","success");


        }catch (Exception e){
            mpResult.put("code",e.getClass().getSimpleName());
            mpResult.put("message",e.getMessage());

        }

        String json = JSON.toJSONString(mpResult);
        resp.getWriter().println(json);

    }

    //销户
    private void xh(HttpServletRequest req,HttpServletResponse resp) throws IOException {

        String userName = req.getParameter("userName");
        String password=req.getParameter("password");
        String identityId=req.getParameter("identityId");
        User user = new User();
        user.setUsername(userName);
        user.setPassword(password);
        user.setIdentityId(Long.valueOf(identityId));
        Map mpResult = new HashMap();
        try {
            userService.xh(user);

            mpResult.put("code","0");
            mpResult.put("message","success");


        }catch (Exception e){
            mpResult.put("code",e.getClass().getSimpleName());
            mpResult.put("message",e.getMessage());

        }

        String json = JSON.toJSONString(mpResult);
        resp.getWriter().println(json);

    }



}
