package com.zu.collect.service.impl;

import com.zu.collect.dao.CqsfMapper;
import com.zu.collect.model.Cqsf;
import com.zu.collect.service.CqsfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CqsfServiceImpl implements CqsfService {

    @Autowired
    private CqsfMapper cqsfMapper;

    @Override
    public int addCqsf(Cqsf cqsf) {

        return cqsfMapper.insertSelective(cqsf);
    }

    @Override
    public Cqsf selectByPrimaryKey(int id) {

        return cqsfMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Cqsf> selectAllCqsf(Cqsf cqsf) {

        return cqsfMapper.selectAllCqsf(cqsf);
    }
}
