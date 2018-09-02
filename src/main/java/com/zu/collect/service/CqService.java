package com.zu.collect.service;

import com.zu.collect.model.Cq;

import java.util.List;

public interface CqService {

    int addCq(Cq cq);

    Cq selectByPrimaryKey(int id);

    List<Cq> selectAllCq(Cq cq);
}
