package com.zu.collect.service;

import com.zu.collect.model.D3;

import java.util.List;

public interface D3Service {

    int addD3(D3 d3);

    D3 selectByPrimaryKey(int id);

    List<D3> selectAllD3(D3 d3);
}
