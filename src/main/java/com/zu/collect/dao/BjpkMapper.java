package com.zu.collect.dao;

import com.zu.collect.model.Bjpk;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BjpkMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Bjpk record);

    int insertSelective(Bjpk record);

    Bjpk selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Bjpk record);

    int updateByPrimaryKey(Bjpk record);

    List<Bjpk> selectAllBjpk(Bjpk record);
}