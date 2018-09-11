package com.zu.collect.service;

import com.zu.collect.model.Tj;

import java.util.List;

public interface TjService {

    int addTj(Tj tj);

    Tj selectByPrimaryKey(int id);

    List<Tj> selectAllTj(Tj tj);
}
