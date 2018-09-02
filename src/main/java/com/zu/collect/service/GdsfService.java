package com.zu.collect.service;

import com.zu.collect.model.Gdsf;

import java.util.List;

public interface GdsfService {

    int addGdsf(Gdsf gdsf);

    Gdsf selectByPrimaryKey(int id);

    List<Gdsf> selectAllGdsf(Gdsf gdsf);
}
