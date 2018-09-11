package com.zu.collect.service;

import com.zu.collect.model.Tjsf;

import java.util.List;

public interface TjsfService {

    int addTjsf(Tjsf tjsf);

    Tjsf selectByPrimaryKey(int id);

    List<Tjsf> selectAllTjsf(Tjsf tjsf);
}
