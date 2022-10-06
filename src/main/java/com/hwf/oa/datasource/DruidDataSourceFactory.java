package com.hwf.oa.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DruidDataSourceFactory extends UnpooledDataSourceFactory {

    public DruidDataSourceFactory(){
        this.dataSource = new DruidDataSource();  //将mybatis的数据源指向第三方阿里巴巴的druid连接池
    }

    @Override
    public DataSource getDataSource() {        //注意是否要重写getDataSource取决于具体的连接池产品，druid连接池由于要进行初始化检查，强制要求重写，但对诸如c3p0的产品则没有这个要求 是否重写取决
        try {
           ((DruidDataSource)this.dataSource).init();  //初始化druid数据源
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);

        }
        return this.dataSource;

    }

}
