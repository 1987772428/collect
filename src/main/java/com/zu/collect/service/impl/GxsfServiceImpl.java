package com.zu.collect.service.impl;

import com.zu.collect.dao.GxsfMapper;
import com.zu.collect.model.Gxsf;
import com.zu.collect.service.GxsfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GxsfServiceImpl implements GxsfService {

    @Autowired
    private GxsfMapper gxsfMapper;

    @Override
    public int addGxsf(Gxsf gxsf) {

        return gxsfMapper.insertSelective(gxsf);
    }

    @Override
    public Gxsf selectByPrimaryKey(int id) {

        return gxsfMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Gxsf> selectAllGxsf(Gxsf gxsf) {

        return gxsfMapper.selectAllGxsf(gxsf);
    }
}
