package com.zu.collect;

import com.zu.collect.controller.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    BjknController bjknController;

    @Autowired
    BjpkController bjpkController;

    @Autowired
    XyftController xyftController;

    @Autowired
    CqsfController cqsfController;

    @Autowired
    CqController cqController;

    @Autowired
    GdsfController gdsfController;

    @Autowired
    ShsfController shsfController;

    @Autowired
    Gd11Controller gd11Controller;

    @Autowired
    GxsfController gxsfController;

    @Autowired
    D3Controller d3Controller;

    @Autowired
    P3Controller p3Controller;

    @Autowired
    TjController tjController;

    @Autowired
    TjsfController tjsfController;

    // @Scheduled(initialDelay=1000, fixedDelay=30000)
    @Scheduled(cron = "${scheduled.cron.bjkn}")
    public void scheduleTaskBjkn()
    {
        logger.info("开始采集北京快乐8168号码");
        bjknController.collect();
    }

    @Scheduled(cron = "${scheduled.cron.bjkn}")
    public void scheduleTaskBjkn6909()
    {
        logger.info("开始采集北京快乐86909号码");
        bjknController.collect6909();
    }

    @Scheduled(cron="${scheduled.cron.bjpk}")
    public void scheduleTaskBjpk()
    {
        logger.info("开始采集北京pk10168号码");
        bjpkController.collect();
    }

    @Scheduled(cron="${scheduled.cron.bjpk}")
    public void scheduleTaskBjpk6909()
    {
        logger.info("开始采集北京pk106909号码");
        bjpkController.collect6909();
    }

    @Scheduled(cron="${scheduled.cron.xyft}")
    public void scheduleTaskXyft()
    {
        logger.info("开始采集幸运飞艇号码");
        xyftController.collect();
    }

    @Scheduled(cron="${scheduled.cron.cqsf}")
    public void scheduleTaskCqsf()
    {
        logger.info("开始采集重庆十分（幸运农场）168号码");
        cqsfController.collect();
    }

    @Scheduled(cron="${scheduled.cron.cqsf}")
    public void scheduleTaskCqsf6909()
    {
        logger.info("开始采集重庆十分（幸运农场）6909号码");
        cqsfController.collect6909();
    }

    @Scheduled(cron="${scheduled.cron.cqsf}")
    public void scheduleTaskCqsf2()
    {
        logger.info("开始采集重庆十分（幸运农场）2号码");
        cqsfController.collect2();
    }

    @Scheduled(cron="${scheduled.cron.cq}")
    public void scheduleTaskCq()
    {
        logger.info("开始采集重庆时时彩168号码");
        cqController.collect();
    }

    @Scheduled(cron="${scheduled.cron.cq}")
    public void scheduleTaskCq6909()
    {
        logger.info("开始采集重庆时时彩6909号码");
        cqController.collect6909();
    }

    @Scheduled(cron="${scheduled.cron.cq}")
    public void scheduleTaskCqsina()
    {
        logger.info("开始采集重庆时时彩sina号码");
        cqController.collectsina();
    }

    @Scheduled(cron="${scheduled.cron.gdsf}")
    public void scheduleTaskGdsf()
    {
        logger.info("开始采集广东十分号码");
        gdsfController.collect();
    }

    @Scheduled(cron="${scheduled.cron.gdsf}")
    public void scheduleTaskGdsf6909()
    {
        logger.info("开始采集广东十分6909号码");
        gdsfController.collect6909();
    }

    @Scheduled(cron="${scheduled.cron.shsf}")
    public void scheduleTaskShsf()
    {
        logger.info("开始采集上海时时乐号码");
        shsfController.collect();
    }

    @Scheduled(cron="${scheduled.cron.gd11}")
    public void scheduleTaskGd11()
    {
        logger.info("开始采集广东11选5号码");
        gd11Controller.collect();
    }

    @Scheduled(cron="${scheduled.cron.gxsf}")
    public void scheduleTaskGxsf()
    {
        logger.info("开始采集广西十分号码");
        gxsfController.collect();
    }

    @Scheduled(cron="${scheduled.cron.d3}")
    public void scheduleTaskD3()
    {
        logger.info("开始采集福彩3D号码");
        d3Controller.collect();
    }

    @Scheduled(cron="${scheduled.cron.p3}")
    public void scheduleTaskP3()
    {
        logger.info("开始采集排列3号码");
        p3Controller.collect();
    }

    @Scheduled(cron="${scheduled.cron.tj}")
    public void scheduleTaskTj()
    {
        logger.info("开始采集天津时时彩号码");
        tjController.collect();
    }

    @Scheduled(cron="${scheduled.cron.tjsf}")
    public void scheduleTaskTjsf()
    {
        logger.info("开始采集天津十分号码");
        tjsfController.collect();
    }
}
