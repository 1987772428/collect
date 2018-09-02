package com.zu.collect.service;

import com.zu.collect.model.Bjpk;

import java.util.List;

public interface BjpkService {

    int addBjpk(Bjpk bjpk);

    Bjpk selectByPrimaryKey(int id);

    List<Bjpk> selectAllBjpk(Bjpk bjpk);
}
