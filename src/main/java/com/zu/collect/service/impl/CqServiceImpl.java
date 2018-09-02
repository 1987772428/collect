package com.zu.collect.service.impl;

import com.zu.collect.dao.CqMapper;
import com.zu.collect.model.Cq;
import com.zu.collect.service.CqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CqServiceImpl implements CqService {

    @Autowired
    private CqMapper cqMapper;

    @Override
    public int addCq(Cq cq) {

        return cqMapper.insertSelective(cq);
    }

    @Override
    public Cq selectByPrimaryKey(int id) {

        return cqMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Cq> selectAllCq(Cq cq) {
        return cqMapper.selectAllCq(cq);
    }
}
