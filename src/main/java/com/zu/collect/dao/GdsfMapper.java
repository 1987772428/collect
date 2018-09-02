package com.zu.collect.dao;

import com.zu.collect.model.Gdsf;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GdsfMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Gdsf record);

    int insertSelective(Gdsf record);

    Gdsf selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Gdsf record);

    int updateByPrimaryKey(Gdsf record);

    List<Gdsf> selectAllGdsf(Gdsf record);
}