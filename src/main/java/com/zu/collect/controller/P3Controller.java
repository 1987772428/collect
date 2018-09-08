package com.zu.collect.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zu.collect.Command;
import com.zu.collect.model.P3;
import com.zu.collect.service.P3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/p3")
public class P3Controller {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String lotName = "排列三";
    private String jsonName = "p3.json";

    @Value("${collect.p3.168}")
    private String p3168;

    @Autowired
    private P3Service p3Service;

    @Autowired
    Command command;


    /**
     * 采集开奖号码
     */
    @RequestMapping("/collect")
    public String collect()
    {
        // 是否有新号码
        int fileBuild = 0;

        // 获取号码
        try{
            String html;
            html = command.html(p3168, "", "");
            // 解析获取的json字符串
            if (!html.equals("404")) {
                JSONObject number = JSONObject.parseObject(html.trim());
                JSONObject result = JSONObject.parseObject(number.getString("result"));
                JSONArray datalist = JSONArray.parseArray(result.getString("data"));
                int num = datalist.size();
                if (num > 10) num = 10;
                String preDrawIssue;
                String preDrawCode;
                int id;
                for (int i=0; i < num; i++) {
                    JSONObject data = JSONObject.parseObject(datalist.getString(i));
                    // 期号
                    preDrawIssue = data.getString("preDrawIssue");
                    // 开奖号码
                    preDrawCode = data.getString("preDrawCode");
//                    logger.info("期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode);
                    id = this.insertP3(preDrawIssue, preDrawCode, "s168");
                    if (id == 1) {
                        fileBuild = 1;
                    }
                    data = null;
                    preDrawIssue = null;
                    preDrawCode = null;
                }
                html = null;
                number = null;
                result = null;
                datalist = null;
                // 如果有新号码，生成json文件
                if (fileBuild == 1) {
                    this.buildJson();
                }
            }
        }catch (Exception e){
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
    private int insertP3(String preDrawIssue, String preDrawCode, String platform)
    {
        // 新号码入库
        int fileBuild = 0;
        // ID
        int id = Integer.valueOf(preDrawIssue);
        // 开奖号码
        String[] arr = new String[] {"s168"};
        String[] openNumber;
        if (command.useArraysBinarySearch(arr, platform)) {
            openNumber = preDrawCode.split(",");
        } else {
            openNumber = null;
            logger.error(lotName + platform + "号码切割失败");
        }
        try {
            // 判断期号是否存在
            P3 rel = p3Service.selectByPrimaryKey(id);
            if (rel == null) {
                P3 p3 = new P3();
                p3.setId(id);
                p3.setQishu(Integer.valueOf(preDrawIssue));
                p3.setCreate_time(new Date());
                p3.setDatetime(new Date());
                p3.setState(0);
                p3.setPrev_text(platform);
                p3.setBall_1(Integer.valueOf(openNumber[0]));
                p3.setBall_2(Integer.valueOf(openNumber[1]));
                p3.setBall_3(Integer.valueOf(openNumber[2]));
                try {
                    p3Service.addP3(p3);
                    logger.info(lotName + platform + " - 期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode +"，更新成功");
                    fileBuild = 1;
                } catch (Exception e) {
                    logger.info(lotName + platform + " - 期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode +"，插入失败");
                }
                p3 = null;
            }
            rel = null;
        } catch (Exception e) {
            logger.error(lotName + platform + " - 期号：" + id + "核对失败，号码：" + preDrawCode);
            e.printStackTrace();
        }
        return fileBuild;
    }

    // 生成json文件
    private void buildJson()
    {
        String json = command.p3();
        command.buildJson(lotName, jsonName, json);
    }
}
