package com.zu.collect.service.impl;

import com.zu.collect.dao.TjsfMapper;
import com.zu.collect.model.Tjsf;
import com.zu.collect.service.TjsfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TjsfServiceImpl implements TjsfService {

    @Autowired
    private TjsfMapper tjsfMapper;

    @Override
    public int addTjsf(Tjsf tjsf) {

        return tjsfMapper.insertSelective(tjsf);
    }

    @Override
    public Tjsf selectByPrimaryKey(int id) {

        return tjsfMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Tjsf> selectAllTjsf(Tjsf tjsf) {

        return tjsfMapper.selectAllTjsf(tjsf);
    }
}
