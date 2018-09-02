package com.zu.collect.dao;

import com.zu.collect.model.Cqsf;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CqsfMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cqsf record);

    int insertSelective(Cqsf record);

    Cqsf selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cqsf record);

    int updateByPrimaryKey(Cqsf record);

    List<Cqsf> selectAllCqsf(Cqsf record);
}