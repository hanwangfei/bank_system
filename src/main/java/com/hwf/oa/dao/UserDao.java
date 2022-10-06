package com.hwf.oa.dao;

import com.hwf.oa.entity.User;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface UserDao {
    User selectByUserId(@Param("user_id") Long userId);
    User selectByUsername(@Param("user_name") String userName);
    void updateAmount(@Param("count")Double count,@Param("user_id") Long userId);
    void updatePassword(@Param("password")String password,@Param("user_id")Long userId);
    void update(@Param("user")User user);

    void insert(@Param("user") User user);
    void del(@Param("user")User user);
}
