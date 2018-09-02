package com.zu.collect.service;

import com.zu.collect.model.Shsf;

import java.util.List;

public interface ShsfService {

    int addShsf(Shsf shsf);

    Shsf selectByPrimaryKey(int id);

    List<Shsf> selectAllShsf(Shsf shsf);
}
