package com.zu.collect.model;

import java.util.Date;

public class D3 {
    private Integer id;

    private Integer qishu;

    private Date create_time;

    private Date datetime;

    private Integer state;

    private String prev_text;

    private Integer ball_1;

    private Integer ball_2;

    private Integer ball_3;

    private Integer offSet;

    private Integer limit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQishu() {
        return qishu;
    }

    public void setQishu(Integer qishu) {
        this.qishu = qishu;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getPrev_text() {
        return prev_text;
    }

    public void setPrev_text(String prev_text) {
        this.prev_text = prev_text == null ? null : prev_text.trim();
    }

    public Integer getBall_1() {
        return ball_1;
    }

    public void setBall_1(Integer ball_1) {
        this.ball_1 = ball_1;
    }

    public Integer getBall_2() {
        return ball_2;
    }

    public void setBall_2(Integer ball_2) {
        this.ball_2 = ball_2;
    }

    public Integer getBall_3() {
        return ball_3;
    }

    public void setBall_3(Integer ball_3) {
        this.ball_3 = ball_3;
    }

    public void setOffSet(Integer offSet) { this.offSet = offSet; }

    public void setLimit(Integer limit) { this.limit = limit; }
}