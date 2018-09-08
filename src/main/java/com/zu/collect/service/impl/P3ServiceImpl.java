package com.zu.collect.service.impl;

import com.zu.collect.dao.P3Mapper;
import com.zu.collect.model.P3;
import com.zu.collect.service.P3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class P3ServiceImpl implements P3Service {

    @Autowired
    private P3Mapper p3Mapper;

    @Override
    public int addP3(P3 p3) {

        return p3Mapper.insertSelective(p3);
    }

    @Override
    public P3 selectByPrimaryKey(int id) {

        return p3Mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<P3> selectAllP3(P3 p3) {

        return p3Mapper.selectAllP3(p3);
    }
}
