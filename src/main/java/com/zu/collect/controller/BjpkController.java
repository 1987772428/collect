package com.zu.collect.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zu.collect.Command;
import com.zu.collect.model.Bjpk;
import com.zu.collect.service.BjpkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Iterator;

@RestController
@RequestMapping("/bjpk")
public class BjpkController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String lotName = "北京PK10";
    private String jsonName = "bjpk.json";

    @Value("${collect.bjpk.168}")
    private String bjpk168;

    @Value("${collect.bjpk.caipiaokong}")
    private String bjpkcaipiaokong;

    @Autowired
    Command command;

    @Autowired
    BjpkService bjpkService;

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
            html = command.html(bjpk168, "", "");

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
                    // logger.info("期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode);
                    id = this.insertBjpk(preDrawIssue, preDrawCode, "s168");
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "ok";
    }

    /**
     * 彩票控采集号码
     * */
    @RequestMapping("/collectcpk")
    public String collectcaipiaokong()
    {
        // 是否有新号码，有则生成json文件
        int fileBuild = 0;

        // 获取号码
        try{
            String html;
            html = command.html(bjpkcaipiaokong, "", "");

            // 解析获取的json字符串
            if (!html.equals("404")) {
                JSONObject number = JSONObject.parseObject(html.trim());

                // 正式提取未知的key值
                Iterator<String> sIterator = number.keySet().iterator();
                // 循环并得到key列表
                String preDrawIssue;
                String preDrawCode;
                String value;
                JSONObject jsonvalue;
                int id;
                while (sIterator.hasNext()) {
                    // 获得key
                    preDrawIssue = sIterator.next();
                    // 获得key值对应的value
                    value = number.getString(preDrawIssue);
                    jsonvalue = JSON.parseObject(value);
                    preDrawCode = jsonvalue.getString("number");

                    //
                    id = this.insertBjpk(preDrawIssue, preDrawCode, "caipiaokong");
                    if (id == 1) {
                        fileBuild = 1;
                    }
                    preDrawIssue = null;
                    preDrawCode = null;
                }

                html = null;
                // 如果有新号码，生成json文件
                if (fileBuild == 1) {
                    this.buildJson();
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
    private int insertBjpk(String preDrawIssue, String preDrawCode, String platform)
    {
        // 新号码入库
        int fileBuild = 0;
        // ID
        int id = Integer.valueOf(preDrawIssue);
        // 开奖号码
        String[] arr = new String[] {"s168", "caipiaokong"};
        String[] openNumber;
        if (command.useArraysBinarySearch(arr, platform)) {
            openNumber = preDrawCode.split(",");
        } else {
            openNumber = null;
            logger.error(lotName + platform + "号码切割失败");
        }
        try {
            // 判断期号是否存在
            Bjpk rel = bjpkService.selectByPrimaryKey(id);
            if (rel == null) {
                Bjpk bjpk = new Bjpk();
                bjpk.setId(Integer.valueOf(preDrawIssue));
                bjpk.setQishu(Integer.valueOf(preDrawIssue));
                bjpk.setCreate_time(new Date());
                bjpk.setDatetime(new Date());
                bjpk.setState(0);
                bjpk.setPrev_text(platform);
                bjpk.setBall_1(Integer.valueOf(openNumber[0]));
                bjpk.setBall_2(Integer.valueOf(openNumber[1]));
                bjpk.setBall_3(Integer.valueOf(openNumber[2]));
                bjpk.setBall_4(Integer.valueOf(openNumber[3]));
                bjpk.setBall_5(Integer.valueOf(openNumber[4]));
                bjpk.setBall_6(Integer.valueOf(openNumber[5]));
                bjpk.setBall_7(Integer.valueOf(openNumber[6]));
                bjpk.setBall_8(Integer.valueOf(openNumber[7]));
                bjpk.setBall_9(Integer.valueOf(openNumber[8]));
                bjpk.setBall_10(Integer.valueOf(openNumber[9]));
                try {
                    bjpkService.addBjpk(bjpk);
                    logger.info(lotName + platform + " - 期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode +"，更新成功");
                    fileBuild = 1;
                } catch (Exception e) {
                    logger.info(lotName + platform + " - 期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode +"，插入失败");
                }
                bjpk = null;
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
        String json = command.bjpk();
        command.buildJson(lotName, jsonName, json);
    }
}
