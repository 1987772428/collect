package com.zu.collect.service;

import com.zu.collect.model.Bjkn;

import java.util.List;

public interface BjknService {

    int addBjkn(Bjkn bjkn);

    Bjkn selectByPrimaryKey(int id);

//    List<Bjkn> selectAllBjkn(int pageNum, int pageSize);
    List<Bjkn> selectAllBjkn(Bjkn bjkn);
}
