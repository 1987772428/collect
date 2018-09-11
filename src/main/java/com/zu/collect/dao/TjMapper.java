package com.zu.collect.dao;

import com.zu.collect.model.Tj;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TjMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Tj record);

    int insertSelective(Tj record);

    Tj selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Tj record);

    int updateByPrimaryKey(Tj record);

    List<Tj> selectAllTj(Tj record);
}