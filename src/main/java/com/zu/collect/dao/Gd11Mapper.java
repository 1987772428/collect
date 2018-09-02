package com.zu.collect.dao;

import com.zu.collect.model.Gd11;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Gd11Mapper {
    int deleteByPrimaryKey(Long id);

    int insert(Gd11 record);

    int insertSelective(Gd11 record);

    Gd11 selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Gd11 record);

    int updateByPrimaryKey(Gd11 record);

    List<Gd11> selectAllGd11(Gd11 record);
}