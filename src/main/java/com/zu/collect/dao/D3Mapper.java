package com.zu.collect.dao;

import com.zu.collect.model.D3;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface D3Mapper {
    int deleteByPrimaryKey(Integer id);

    int insert(D3 record);

    int insertSelective(D3 record);

    D3 selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(D3 record);

    int updateByPrimaryKey(D3 record);

    List<D3> selectAllD3(D3 record);
}