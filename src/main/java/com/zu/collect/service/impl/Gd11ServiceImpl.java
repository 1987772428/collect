package com.zu.collect.service.impl;

import com.zu.collect.dao.Gd11Mapper;
import com.zu.collect.model.Gd11;
import com.zu.collect.service.Gd11Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Gd11ServiceImpl implements Gd11Service {

    @Autowired
    private Gd11Mapper gd11Mapper;

    @Override
    public int addGd11(Gd11 gd11) {

        return gd11Mapper.insertSelective(gd11);
    }

    @Override
    public Gd11 selectByPrimaryKey(int id) {

        return gd11Mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Gd11> selectAllGd11(Gd11 gd11) {

        return gd11Mapper.selectAllGd11(gd11);
    }
}
