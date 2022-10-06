package com.hwf.oa.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.function.Function;

public class MybatisUtils {
    private static SqlSessionFactory sqlSessionFactory = null;
    static {
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader("mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        }catch (IOException e){
            throw new ExceptionInInitializerError(e);
        }
    }


    /**
     * 进行数据的查询操作
     * @param func
     * @return
     */
    public static Object excuteQuery(Function<SqlSession,Object> func){  //传入一个函数式接口，传入参数为sqlSession，返回值为object;
        SqlSession sqlSession = sqlSessionFactory.openSession();
        System.out.println(sqlSession);
        try {
            Object obj = func.apply(sqlSession);  //func中有个apply代表执行代码，至于执行什么交由外界传入，此外这里确保了连接的关闭
            return obj;
        }finally {  //确保连接一定会关闭
            sqlSession.close();
        }
    }


    /**
     * 进行数据的写操作，对比查询就是增加了事务的控制
     * @param func   要执行的写操作代码块
     * @return  要返回的结果
     */
    public static Object excuteUpdate(Function<SqlSession,Object> func){  //传入一个函数式接口，传入参数为sqlSession，返回值为object;
        SqlSession sqlSession = sqlSessionFactory.openSession(false);
        System.out.println(sqlSession);
        try {
            Object obj = func.apply(sqlSession);  //func中有个apply代表执行代码，至于执行什么交由外界传入，此外这里确保了连接的关闭
            sqlSession.commit();
            return obj;
        }catch (RuntimeException e){
            sqlSession.rollback();
            throw e;
        }finally {  //确保连接一定会关闭
            sqlSession.close();
        }
    }



}
