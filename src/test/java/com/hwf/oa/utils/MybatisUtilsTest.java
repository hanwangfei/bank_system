package com.hwf.oa.utils;


import org.junit.Test;

public class MybatisUtilsTest{

    @Test
    public void testExcuteQuery() {
        String result = (String)MybatisUtils.excuteQuery(sqlSession -> sqlSession.selectOne("test.sample"));
        System.out.println(result);
    }

    public void testExcuteUpdate() {
    }
}