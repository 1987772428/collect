package com.zu.collect.service.impl;


import com.zu.collect.dao.BjknMapper;
import com.zu.collect.model.Bjkn;
import com.zu.collect.service.BjknService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BjknServiceImpl implements BjknService {

    @Autowired
    private BjknMapper bjknMapper;


    @Override
    public int addBjkn(Bjkn bjkn) {

        return bjknMapper.insertSelective(bjkn);
    }

    @Override
    public Bjkn selectByPrimaryKey(int id) {

        return bjknMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Bjkn> selectAllBjkn(Bjkn bjkn) {

        return bjknMapper.selectAllBjkn(bjkn);
    }

}
