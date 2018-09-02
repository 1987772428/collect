package com.zu.collect.dao;

import com.zu.collect.model.Gxsf;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GxsfMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Gxsf record);

    int insertSelective(Gxsf record);

    Gxsf selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Gxsf record);

    int updateByPrimaryKey(Gxsf record);

    List<Gxsf> selectAllGxsf(Gxsf record);
}