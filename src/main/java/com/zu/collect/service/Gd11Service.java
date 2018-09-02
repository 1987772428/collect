package com.zu.collect.service;

import com.zu.collect.model.Gd11;

import java.util.List;

public interface Gd11Service {

    int addGd11(Gd11 gd11);

    Gd11 selectByPrimaryKey(int id);

    List<Gd11> selectAllGd11(Gd11 gd11);
}
