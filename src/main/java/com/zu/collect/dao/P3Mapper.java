package com.zu.collect.dao;

import com.zu.collect.model.P3;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface P3Mapper {
    int deleteByPrimaryKey(Integer id);

    int insert(P3 record);

    int insertSelective(P3 record);

    P3 selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(P3 record);

    int updateByPrimaryKey(P3 record);

    List<P3> selectAllP3(P3 record);
}