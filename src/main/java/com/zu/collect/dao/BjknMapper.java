package com.zu.collect.dao;

import com.zu.collect.model.Bjkn;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BjknMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Bjkn record);

    int insertSelective(Bjkn record);

    Bjkn selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Bjkn record);

    int updateByPrimaryKey(Bjkn record);

    List<Bjkn> selectAllBjkn(Bjkn record);
}