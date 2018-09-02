package com.zu.collect.service;

import com.zu.collect.model.Gxsf;

import java.util.List;

public interface GxsfService {

    int addGxsf(Gxsf gxsf);

    Gxsf selectByPrimaryKey(int id);

    List<Gxsf> selectAllGxsf(Gxsf gxsf);
}
