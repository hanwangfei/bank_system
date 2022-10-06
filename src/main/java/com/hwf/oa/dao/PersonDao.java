package com.hwf.oa.dao;

import com.hwf.oa.entity.Person;

public interface PersonDao {
    public Person selectByIdentityId(Long id);
}
