package com.zu.collect.dao;

import com.zu.collect.model.Tjsf;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TjsfMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Tjsf record);

    int insertSelective(Tjsf record);

    Tjsf selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Tjsf record);

    int updateByPrimaryKey(Tjsf record);

    List<Tjsf> selectAllTjsf(Tjsf record);
}