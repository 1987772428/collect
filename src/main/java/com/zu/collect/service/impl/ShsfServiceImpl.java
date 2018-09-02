package com.zu.collect.service.impl;

import com.zu.collect.dao.ShsfMapper;
import com.zu.collect.model.Shsf;
import com.zu.collect.service.ShsfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShsfServiceImpl implements ShsfService {

    @Autowired
    private ShsfMapper shsfMapper;

    @Override
    public int addShsf(Shsf shsf) {

        return shsfMapper.insertSelective(shsf);
    }

    @Override
    public Shsf selectByPrimaryKey(int id) {

        return shsfMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Shsf> selectAllShsf(Shsf shsf) {

        return shsfMapper.selectAllShsf(shsf);
    }
}
