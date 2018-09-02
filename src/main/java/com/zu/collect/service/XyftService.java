package com.zu.collect.service;

import com.zu.collect.model.Xyft;

import java.util.List;

public interface XyftService {

    int addXyft(Xyft xyft);

    Xyft selectByPrimaryKey(int id);

    List<Xyft> selectAllXyft(Xyft xyft);
}
