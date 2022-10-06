package com.hwf.oa.dao;

import com.hwf.oa.entity.Notice;

import java.util.List;

public interface NoticeDao {
    public List<Notice> selectByReceiverId(Long receiverId);
    public void insert(Notice notice);


}
