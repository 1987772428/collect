package com.zu.collect.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zu.collect.Command;
import com.zu.collect.model.Cqsf;
import com.zu.collect.service.CqsfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/cqsf")
public class CqsfController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String lotName = "重庆十分（幸运农场）";
    private String jsonName = "cqsf.json";

    @Value("${collect.cqsf.official}")
    private String cqsfOfficial;

    @Autowired
    Command command;

    // 采集地址
    @Autowired
    CqsfService cqsfService;

    /**
     * 采集号码
     * */
    @RequestMapping("/collect")
    public String collect()
    {
        // 是否有新号码，有则生成json文件
        int fileBuild = 0;

        // 获取号码
        try{
            String html;
            html = command.html(cqsfOfficial + "&t=" + System.currentTimeMillis() + "&g_tk=&_=" + (System.currentTimeMillis() + 1), "");
            if (!html.equals("404")) {
                JSONObject number = JSONObject.parseObject(html.trim());
                if (number.getString("errCode").equals("0")) {
                    JSONObject result = JSONObject.parseObject(number.getString("data"));
                    JSONArray datalist = JSONArray.parseArray(result.getString("issueList"));
                    int num = datalist.size();
                    if (num > 10) num = 10;
                    String preDrawIssue;
                    String preDrawCode;
                    int id;
                    for (int i=0; i < num; i++) {
                        JSONObject data = JSONObject.parseObject(datalist.getString(i));
                        // 期号
                        preDrawIssue = data.getString("issueNo");
                        // 开奖号码
                        preDrawCode = data.getString("drawResult");
                        id = this.insertCqsf(preDrawIssue, preDrawCode, "official");
                        if (id == 1) {
                            fileBuild = 1;
                        }
                        data = null;
                        preDrawIssue = null;
                        preDrawCode = null;
                    }
                    html = null;
                    // 如果有新号码，生成json文件
                    if (fileBuild == 1) {
                        this.buildJson();
                    }
                } else {
                    logger.error(lotName + "解析数据异常");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }

    /**
     * 更新数据
     * @param preDrawIssue  String          期号
     * @param preDrawCode   string          开奖号码
     * @param platform      String          采集来源
     * @return  fileBuild   int             1:需要生成json文件
     * */
    private int insertCqsf(String preDrawIssue, String preDrawCode, String platform)
    {
        // 新号码入库
        int fileBuild = 0;
        // ID
        int id = Integer.valueOf(preDrawIssue.substring(2, preDrawIssue.length()));
        // 开奖号码
        String[] arr = new String[] {"official"};
        String[] openNumber;
        if (command.useArraysBinarySearch(arr, platform)) {
            openNumber = preDrawCode.split(",");
        } else {
            logger.error(lotName + "号码切割失败");
            openNumber = preDrawCode.split("|");
        }
        try {
            // 判断期号是否存在
            Cqsf rel = cqsfService.selectByPrimaryKey(id);
            if (rel == null) {
                Cqsf cqsf = new Cqsf();
                cqsf.setId(id);
                cqsf.setQishu(Long.valueOf(preDrawIssue));
                cqsf.setCreate_time(new Date());
                cqsf.setDatetime(new Date());
                cqsf.setState(0);
                cqsf.setPrev_text("official");
                cqsf.setBall_1(Integer.valueOf(openNumber[0]));
                cqsf.setBall_2(Integer.valueOf(openNumber[1]));
                cqsf.setBall_3(Integer.valueOf(openNumber[2]));
                cqsf.setBall_4(Integer.valueOf(openNumber[3]));
                cqsf.setBall_5(Integer.valueOf(openNumber[4]));
                cqsf.setBall_6(Integer.valueOf(openNumber[5]));
                cqsf.setBall_7(Integer.valueOf(openNumber[6]));
                cqsf.setBall_8(Integer.valueOf(openNumber[7]));
                try {
                    cqsfService.addCqsf(cqsf);
                    logger.info(lotName + " - 期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode +"，更新成功");
                    fileBuild = 1;
                } catch (Exception e) {
                    logger.info(lotName + " - 期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode +"，插入失败");
                }
                cqsf = null;
            }
            rel = null;
        } catch (Exception e) {
            logger.error(lotName + " - 期号：" + id + "核对失败，号码：" + preDrawCode);
            e.printStackTrace();
        }
        return fileBuild;
    }

    // 生成json文件
    private void buildJson()
    {
        String json = command.cqsf();
        command.buildJson(lotName, jsonName, json);
    }
}
