package com.hwf.oa.service;

import com.hwf.oa.dao.NoticeDao;
import com.hwf.oa.entity.Notice;
import com.hwf.oa.utils.MybatisUtils;

import java.util.List;

public class NoticeService {
    public List<Notice> getNoticeList(Long receiverId){
        return (List<Notice>) MybatisUtils.excuteQuery(sqlSession -> {
            NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class);
            return noticeDao.selectByReceiverId(receiverId);
        });
    }

    public void inset(Notice notice){
        MybatisUtils.excuteUpdate(sqlSession -> {
            NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class);
            noticeDao.insert(notice);
            return null;
        });
    }
}
