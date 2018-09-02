package com.zu.collect.service.impl;

import com.zu.collect.dao.GdsfMapper;
import com.zu.collect.model.Gdsf;
import com.zu.collect.service.GdsfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GdsfServiceImpl implements GdsfService {

    @Autowired
    private GdsfMapper gdsfMapper;

    @Override
    public int addGdsf(Gdsf gdsf) {

        return gdsfMapper.insertSelective(gdsf);
    }

    @Override
    public Gdsf selectByPrimaryKey(int id) {

        return gdsfMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Gdsf> selectAllGdsf(Gdsf gdsf) {

        return gdsfMapper.selectAllGdsf(gdsf);
    }
}
