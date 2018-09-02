package com.zu.collect.service.impl;

import com.zu.collect.dao.XyftMapper;
import com.zu.collect.model.Xyft;
import com.zu.collect.service.XyftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class XyftServiceImpl implements XyftService {

    @Autowired
    private XyftMapper xyftMapper;

    @Override
    public int addXyft(Xyft xyft) {

        return xyftMapper.insertSelective(xyft);
    }

    @Override
    public Xyft selectByPrimaryKey(int id) {

        return xyftMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Xyft> selectAllXyft(Xyft xyft) {

        return xyftMapper.selectAllXyft(xyft);
    }
}
