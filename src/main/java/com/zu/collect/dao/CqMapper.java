package com.zu.collect.dao;

import com.zu.collect.model.Cq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CqMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Cq record);

    int insertSelective(Cq record);

    Cq selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cq record);

    int updateByPrimaryKey(Cq record);

    List<Cq> selectAllCq(Cq record);
}