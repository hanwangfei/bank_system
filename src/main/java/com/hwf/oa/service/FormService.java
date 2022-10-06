package com.hwf.oa.service;

import com.hwf.oa.dao.FormDao;
import com.hwf.oa.dao.NoticeDao;
import com.hwf.oa.dao.RbacDao;
import com.hwf.oa.dao.UserDao;
import com.hwf.oa.entity.Form;
import com.hwf.oa.entity.Node;
import com.hwf.oa.entity.Notice;
import com.hwf.oa.entity.User;
import com.hwf.oa.service.exception.BussinessException;
import com.hwf.oa.utils.MybatisUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class FormService {
    NoticeService noticeService = new NoticeService();
    UserService userService= new UserService();
    RbacDao rbacDao = new RbacDao();
    /**
     *创建贷款单
     * @param form 前端输入的贷款单数据
     * @return 持久化后的贷款单对象
     */
    public Form createForm(Form form){
        return (Form) MybatisUtils.excuteUpdate(sqlSession -> {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            FormDao formDao = sqlSession.getMapper(FormDao.class);
            User user = userDao.selectByUserId(form.getUserId());
            User operatorUser = userService.selectByUsername(form.getOperatorName());
            if(operatorUser==null || operatorUser.getState()==0){
                throw new BussinessException("J001","经办人账户不存在或异常");
            }
            List<Node> list = rbacDao.selectNodeByUserId(operatorUser.getUserId());  //查询该账户是否是管理员账户
            boolean flag = true;
            for(Node n:list){
                if(n.getNodeName().equals("管理模块")){
                    flag=false;
                    break;
                }
            }
            if(flag)
                throw new BussinessException("J002","该账户不具备审批贷款的权限，请重新输入");
            formDao.insert(form);
            Notice notice1=new Notice();
            Notice notice2=new Notice();
            notice1.setReciverId(user.getUserId());
            notice1.setContent("您的贷款申请已提交，申请单编号为:"+form.getFormId()+",请耐心等待审批");
            notice1.setCreateTime(new Date());
            noticeService.inset(notice1);
            notice2.setReciverId(operatorUser.getUserId());
            notice2.setContent("您有新的贷款申请，申请单编号为："+form.getFormId()+"请尽快审批");
            notice2.setCreateTime(new Date());
            noticeService.inset(notice2);
            return form;
        });

    }

    public List<Map> getUserFormList(String state,Long userId){
        return (List<Map>) MybatisUtils.excuteQuery(sqlSession -> {
            FormDao formDao = sqlSession.getMapper(FormDao.class);
            return formDao.selectToUserByParams(state,userId);
        });
    }

    public List<Map> getOpFormList(String state, Long opId){
        return (List<Map>) MybatisUtils.excuteQuery(sqlSession -> {
            FormDao formDao = sqlSession.getMapper(FormDao.class);
            return formDao.selectToOpByParams(state,userService.selectById(opId).getUsername());
        });
    }

    public void shjk(Long formId,Long operatorId,String result,String resultReason){
        MybatisUtils.excuteUpdate(sqlSession -> {
            //无论是否同意，当前表单审批流程结束
            FormDao formDao = sqlSession.getMapper(FormDao.class);
            Form form = formDao.selectById(formId);
            form.setState(result);
            form.setResult(resultReason);
            formDao.update(form);

            Notice notice1=new Notice();
            Notice notice2=new Notice();

            String strResult = result.equals("approved")?"同意":"拒绝";
            //发给审核人
            notice1.setReciverId(operatorId);
            notice1.setContent("您对贷款单:"+formId+"的贷款已审核完毕，您的结果是："+strResult+",您的审批意见是："+resultReason);
            notice1.setCreateTime(new Date());
            //发给借贷人
            notice2.setReciverId(form.getUserId());
            notice2.setContent("您的贷款单："+formId+"号的贷款申请已由"+form.getOperatorName()+"审核完毕"+",结果是:"+strResult+",审批意见是:"+resultReason);
            notice2.setCreateTime(new Date());

            noticeService.inset(notice1);
            noticeService.inset(notice2);

            if(result.equals("approved"))  //如果同意，存钱入账户
            userService.ck(userService.selectById(form.getUserId()),form.getCount().doubleValue(),true); //存入账户
            return null;
        });
    }

    public void hk(Long formId){
        MybatisUtils.excuteUpdate(sqlSession -> {
            FormDao formDao = sqlSession.getMapper(FormDao.class);
            Form form = formDao.selectById(formId);
            form.setHasReturn(1);
            formDao.update(form);
            Notice notice = new Notice();
            notice.setReciverId(form.getUserId());
            notice.setContent("您的贷款单为："+formId+"的贷款已经归还完毕，合作愉快");
            notice.setCreateTime(new Date());
            noticeService.inset(notice);
            return null;
        });
    }
}
