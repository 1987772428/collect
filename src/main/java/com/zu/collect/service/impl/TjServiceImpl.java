package com.zu.collect.service.impl;

import com.zu.collect.dao.TjMapper;
import com.zu.collect.model.Tj;
import com.zu.collect.service.TjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TjServiceImpl implements TjService {

    @Autowired
    private TjMapper tjMapper;

    @Override
    public int addTj(Tj tj) {

        return tjMapper.insertSelective(tj);
    }

    @Override
    public Tj selectByPrimaryKey(int id) {

        return tjMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Tj> selectAllTj(Tj tj) {

        return tjMapper.selectAllTj(tj);
    }
}
