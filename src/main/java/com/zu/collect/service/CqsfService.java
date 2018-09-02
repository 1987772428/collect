package com.zu.collect.service;

import com.zu.collect.model.Cqsf;

import java.util.List;

public interface CqsfService {

    int addCqsf(Cqsf cqsf);

    Cqsf selectByPrimaryKey(int id);

    List<Cqsf> selectAllCqsf(Cqsf cqsf);
}
