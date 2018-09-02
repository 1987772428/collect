package com.zu.collect.dao;

import com.zu.collect.model.Xyft;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface XyftMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Xyft record);

    int insertSelective(Xyft record);

    Xyft selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Xyft record);

    int updateByPrimaryKey(Xyft record);

    List<Xyft> selectAllXyft(Xyft record);
}