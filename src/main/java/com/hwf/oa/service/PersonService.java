package com.hwf.oa.service;

import com.hwf.oa.dao.PersonDao;
import com.hwf.oa.entity.Person;
import com.hwf.oa.utils.MybatisUtils;

public class PersonService {
    public Person selectByIdentityId(Long identityId){
        return (Person) MybatisUtils.excuteQuery(sqlSession -> {
            PersonDao personDao = sqlSession.getMapper(PersonDao.class);
            return personDao.selectByIdentityId(identityId);
        });
    }
}
