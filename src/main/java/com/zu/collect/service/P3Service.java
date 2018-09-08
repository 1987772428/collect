package com.zu.collect.service;

import com.zu.collect.model.P3;

import java.util.List;

public interface P3Service {

    int addP3(P3 p3);

    P3 selectByPrimaryKey(int id);

    List<P3> selectAllP3(P3 p3);
}
