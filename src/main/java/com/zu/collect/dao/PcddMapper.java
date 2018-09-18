package com.zu.collect.dao;

import com.zu.collect.model.Pcdd;

public interface PcddMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Pcdd record);

    int insertSelective(Pcdd record);

    Pcdd selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Pcdd record);

    int updateByPrimaryKey(Pcdd record);
}