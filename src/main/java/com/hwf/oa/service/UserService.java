package com.hwf.oa.service;

import com.hwf.oa.dao.NoticeDao;
import com.hwf.oa.dao.RbacDao;
import com.hwf.oa.dao.UserDao;
import com.hwf.oa.entity.Node;
import com.hwf.oa.entity.Notice;
import com.hwf.oa.entity.User;
import com.hwf.oa.service.exception.BussinessException;
import com.hwf.oa.utils.MybatisUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {
    RbacDao rbacDao = new RbacDao();
    NoticeService noticeService = new NoticeService();
    PersonService personService = new PersonService();

    public User selectById(Long userId) {
        return (User) MybatisUtils.excuteQuery(sqlSession -> {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            return userDao.selectByUserId(userId);
        });
    }

    public User selectByUsername(String userName) {
        return (User) MybatisUtils.excuteQuery(sqlSession -> {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            return userDao.selectByUsername(userName);
        });
    }



    /**
     * 根据前台输入进行登录校验
     *
     * @param username 用户名
     * @param password 密码
     * @return 校验通过后，包含对应用户数据的User实体类
     */
    public User checkLogin(String username, String password) {
        User user = selectByUsername(username);
        if (user == null) {
            //用户不存在，抛出异常
            throw new BussinessException("L001", "用户名不存在");
        }else if (!password.equals(user.getPassword())) {
            throw new BussinessException("L002", "密码错误");
        }else if(user.getState()==0)
            throw new BussinessException("L003","账户异常");
        return user;
    }


    public List<Node> selectNodeByUserId(Long userId) {
        List<Node> nodeList = rbacDao.selectNodeByUserId(userId);
        return nodeList;

    }

    public void updateAmount(Double count, Long userId) {
        MybatisUtils.excuteUpdate(sqlSession -> {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            userDao.updateAmount(count, userId);
            return null;
        });
    }

    public void updatePassword(String password,Long userId){
        MybatisUtils.excuteUpdate(sqlSession -> {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            userDao.updatePassword(password,userId);
            return null;
        });

    }

    public void update(User user){
        MybatisUtils.excuteUpdate(sqlSession -> {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            userDao.update(user);
            return null;
        });
    }

    public void insert(User user){
        MybatisUtils.excuteUpdate(sqlSession -> {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            userDao.insert(user);
            return null;
        });
    }

    public void del(User user){
        MybatisUtils.excuteUpdate(sqlSession -> {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            userDao.del(user);
            return null;
        });
    }

    //存款
    public void ck(User user, Double count, boolean flag) {
        if (!flag) {
            //flag为false,代表要取款
            count = -1 * count;
        }
        if(user.getAmount().doubleValue()+count<0)
            throw new BussinessException("C001","余额不足");
        this.updateAmount(count, user.getUserId());
        Notice notice = new Notice();
        notice.setReciverId(user.getUserId());
        String content = null;
        BigDecimal amount = this.selectById(user.getUserId()).getAmount();
        user.setAmount(amount);
        if (flag)
            content = "尊敬的用户您好，您的账户已存款" + count + "元，当前余额为:" + user.getAmount() + "元";
        else content = "尊敬的用户您好，您的账户已取款" + (-1 * count) + "元，当前余额为:" + user.getAmount() + "元";
        notice.setContent(content);
        notice.setCreateTime(new Date());
        noticeService.inset(notice);
    }




    //转账
    public void transfer(User user, Long outId, Double count) {
        MybatisUtils.excuteUpdate(sqlSession -> {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            if (userDao.selectByUserId(user.getUserId()).getAmount().compareTo(BigDecimal.valueOf(count)) < 0) {
                //余额不足，无法转账
                throw new BussinessException("Z001", "余额不足，无法转账");
            } else if(userDao.selectByUserId(outId).getState()==0){  //状态为0,代表账户被挂失
              throw new BussinessException("Z003","转出账户异常");
            } else {
                userDao.updateAmount(-1.0 * count, user.getUserId());
                userDao.updateAmount(count, outId);
                return null;
            }
        });

        MybatisUtils.excuteQuery(sqlSession -> {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            BigDecimal amount = userDao.selectByUserId(user.getUserId()).getAmount();
            user.setAmount(amount);

            Notice notice1=new Notice();
            Notice notice2=new Notice();
            notice1.setReciverId(user.getUserId());
            notice1.setContent("尊敬的用户您好，您的账户已向" + userDao.selectByUserId(outId).getUsername() + "账户转账" + count + "元,当前余额为："+user.getAmount()+"元");
            notice1.setCreateTime(new Date());
            notice2.setReciverId(outId);
            notice2.setContent("尊敬的用户您好，您的账户已转入" + count + "元,当前余额为："+this.selectById(outId).getAmount()+"元");
            notice2.setCreateTime(new Date());
            noticeService.inset(notice1);
            noticeService.inset(notice2);
            return null;
        });

    }

    //改密码
    public void changePassword(User user,String password,String newPassword,String newPassword1){
        if(!user.getPassword().equals(password))
            throw new BussinessException("G001","原密码错误");
        else if(!newPassword.equals(newPassword1))
            throw new BussinessException("G002","密码不一致，请重新输入");
        else {
            this.updatePassword(newPassword,user.getUserId());
            Notice notice = new Notice();
            notice.setReciverId(user.getUserId());
            notice.setCreateTime(new Date());
            notice.setContent("您的密码已经修改，若非本人操作，请迅速联系工作人员");
            noticeService.inset(notice);
        }
    }

    //账号挂失
    public void zzgs(User admin,String userName,String password,boolean flag){
        User user = this.selectByUsername(userName);  //这里的用户是那个要修改休息的用户,admin是当前登录用户，也就是管理员账户
        if(user==null)
            throw new BussinessException("Z001","用户不存在");
        if(!user.getPassword().equals(password))
            throw new BussinessException("Z001","密码错误");

        String content=null;
        if(flag){
            //挂失
            user.setState(0);
            content="挂失";
        }
        else{
            //解挂
            user.setState(1);
            content="解挂";
        }
        this.update(user);
        //给工作人员发消息
        Notice notice1 = new Notice();
        notice1.setReciverId(admin.getUserId());
        notice1.setCreateTime(new Date());
        notice1.setContent("您已将账户："+user.getUsername()+content);
        noticeService.inset(notice1);
    }

    //开户
    public void kh(User user){
        //继续完善初始信息
        user.setAmount(new BigDecimal(0));
        user.setState(1);
        user.setSalt(0L);

        if(personService.selectByIdentityId(user.getIdentityId())==null)
            throw new BussinessException("K001","开户人信息错误");
        if(this.selectByUsername(user.getUsername())!=null) //已有相同的用户名
            throw new BussinessException("K002","用户名被占用");
        this.insert(user);  //一定要先执行这句添加再执行下面的，因为这句代码会将user的主键自动回填
        rbacDao.insert(user.getUserId());
    }

    //开户
    public void xh(User user){
        User resUser = this.selectByUsername(user.getUsername());
        if(resUser==null)
            throw new BussinessException("K004","账户不存在");
        if(personService.selectByIdentityId(user.getIdentityId())==null)
            throw new BussinessException("K001","销户人信息错误");
        if(!user.getPassword().equals(resUser.getPassword()))
            throw new BussinessException("K003","销户账户密码错误");

        this.del(resUser);
    }

}
