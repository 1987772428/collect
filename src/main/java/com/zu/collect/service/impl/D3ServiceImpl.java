package com.zu.collect.service.impl;

import com.zu.collect.dao.D3Mapper;
import com.zu.collect.model.D3;
import com.zu.collect.service.D3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class D3ServiceImpl implements D3Service {

    @Autowired
    private D3Mapper d3Mapper;

    @Override
    public int addD3(D3 d3) {

        return d3Mapper.insertSelective(d3);
    }

    @Override
    public D3 selectByPrimaryKey(int id) {

        return d3Mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<D3> selectAllD3(D3 d3) {

        return d3Mapper.selectAllD3(d3);
    }
}
