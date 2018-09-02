package com.zu.collect.model;

import java.util.Date;

public class Xyft {
    private Integer id;

    private Long qishu;

    private Date create_time;

    private Date datetime;

    private Integer state;

    private String prev_text;

    private Integer ball_1;

    private Integer ball_2;

    private Integer ball_3;

    private Integer ball_4;

    private Integer ball_5;

    private Integer ball_6;

    private Integer ball_7;

    private Integer ball_8;

    private Integer ball_9;

    private Integer ball_10;

    private Integer offSet;

    private Integer limit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getQishu() {
        return qishu;
    }

    public void setQishu(Long qishu) {
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

    public Integer getBall_4() {
        return ball_4;
    }

    public void setBall_4(Integer ball_4) {
        this.ball_4 = ball_4;
    }

    public Integer getBall_5() {
        return ball_5;
    }

    public void setBall_5(Integer ball_5) {
        this.ball_5 = ball_5;
    }

    public Integer getBall_6() {
        return ball_6;
    }

    public void setBall_6(Integer ball_6) {
        this.ball_6 = ball_6;
    }

    public Integer getBall_7() {
        return ball_7;
    }

    public void setBall_7(Integer ball_7) {
        this.ball_7 = ball_7;
    }

    public Integer getBall_8() {
        return ball_8;
    }

    public void setBall_8(Integer ball_8) {
        this.ball_8 = ball_8;
    }

    public Integer getBall_9() {
        return ball_9;
    }

    public void setBall_9(Integer ball_9) {
        this.ball_9 = ball_9;
    }

    public Integer getBall_10() {
        return ball_10;
    }

    public void setBall_10(Integer ball_10) {
        this.ball_10 = ball_10;
    }

    public void setOffSet(Integer offSet) { this.offSet = offSet; }

    public void setLimit(Integer limit) { this.limit = limit; }
}