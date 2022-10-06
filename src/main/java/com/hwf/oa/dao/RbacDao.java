package com.hwf.oa.dao;

import com.hwf.oa.entity.Node;
import com.hwf.oa.utils.MybatisUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public class RbacDao {
    public List<Node> selectNodeByUserId(Long userId){
        return (List) MybatisUtils.excuteQuery(sqlSession -> sqlSession.selectList("rbacmapper.selectNodeByUserId",userId));
    }

    public void insert(@Param("user_id")Long userId){
        MybatisUtils.excuteUpdate(sqlSession -> {
            sqlSession.insert("rbacmapper.insert",userId);
            return null;
        });

    }
}
