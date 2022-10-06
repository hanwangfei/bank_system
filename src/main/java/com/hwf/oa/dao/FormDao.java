package com.hwf.oa.dao;

import com.hwf.oa.entity.Form;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FormDao {
    public void insert(Form form);

    public List<Map> selectToUserByParams(@Param("state") String State,@Param("user_id") Long userId);
    public List<Map> selectToOpByParams(@Param("state") String State,@Param("operator_name") String operatorName);

    public Form selectById(Long formId);
    public void update(Form form);
}
