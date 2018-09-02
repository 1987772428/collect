package com.zu.collect.service.impl;

import com.zu.collect.dao.BjpkMapper;
import com.zu.collect.model.Bjpk;
import com.zu.collect.service.BjpkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BjpkServiceImpl implements BjpkService {

    @Autowired
    private BjpkMapper bjpkMapper;


    @Override
    public int addBjpk(Bjpk bjpk) {

        return bjpkMapper.insertSelective(bjpk);
    }

    @Override
    public Bjpk selectByPrimaryKey(int id) {

        return bjpkMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Bjpk> selectAllBjpk(Bjpk bjpk) {

        return bjpkMapper.selectAllBjpk(bjpk);
    }
}
