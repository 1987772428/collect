package com.zu.collect.dao;

import com.zu.collect.model.Shsf;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShsfMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shsf record);

    int insertSelective(Shsf record);

    Shsf selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shsf record);

    int updateByPrimaryKey(Shsf record);

    List<Shsf> selectAllShsf(Shsf record);
}