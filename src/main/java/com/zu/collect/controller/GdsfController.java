package com.zu.collect.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zu.collect.Command;
import com.zu.collect.model.Gdsf;
import com.zu.collect.service.GdsfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/gdsf")
public class GdsfController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String lotName = "广东十分";
    private String jsonName = "gdsf.json";

    @Value("${collect.gdsf.168}")
    private String gdsf168;

    @Value("${collect.gdsf.6909}")
    private String gdsf6909;

    @Autowired
    Command command;

    // 采集地址
    @Autowired
    GdsfService gdsfService;

    /**
     * 采集号码
     * */
    @RequestMapping("/collect")
    public String collect()
    {
        // 是否有新号码，有则生成json文件
        int fileBuild = 0;

        // 获取号码
        try {
            String html;
            html = command.html(gdsf168, "");

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
                for (int i = 0; i < num; i++) {
                    JSONObject data = JSONObject.parseObject(datalist.getString(i));
                    // 期号
                    preDrawIssue = data.getString("preDrawIssue");
                    // 开奖号码
                    preDrawCode = data.getString("preDrawCode");
                    // logger.info("期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode);
                    id = this.insertGdsf(preDrawIssue, preDrawCode, "168");
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
     * 采集号码6909
     * */
    @RequestMapping("/collect6909")
    public String collect6909()
    {
        // 是否有新号码，有则生成json文件
        int fileBuild = 0;

        // 获取号码
        try {
            String html;
            html = command.html(gdsf6909 + System.currentTimeMillis(), "");

            // 解析获取的json字符串
            if (!html.equals("404")) {
                JSONObject number = JSONObject.parseObject(html.trim());
                JSONArray datalist = JSONArray.parseArray(number.getString("rows"));
                int num = datalist.size();
                if (num > 10) num = 10;
                String preDrawIssue;
                String preDrawCode;
                String n1,n2,n3,n4,n5,n6,n7,n8;
                int id;
                for (int i = 0; i < num; i++) {
                    JSONObject data = JSONObject.parseObject(datalist.getString(i));
                    // 期号
                    preDrawIssue = data.getString("termNum");
                    // 开奖号码
                    n1 = data.getString("n1");
                    n2 = data.getString("n2");
                    n3 = data.getString("n3");
                    n4 = data.getString("n4");
                    n5 = data.getString("n5");
                    n6 = data.getString("n6");
                    n7 = data.getString("n7");
                    n8 = data.getString("n8");
                    preDrawCode = n1 + "," + n2 + "," + n3 + "," + n4 + "," + n5 + "," + n6 + "," + n7 + "," + n8;
                    id = this.insertGdsf(preDrawIssue, preDrawCode, "6909");
                    if (id == 1) {
                        fileBuild = 1;
                    }
                    data = null;
                    preDrawIssue = null;
                    preDrawCode = null;
                }
                html = null;
                number = null;
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
     * 更新数据
     * @param preDrawIssue  String          期号
     * @param preDrawCode   string          开奖号码
     * @param platform      String          采集来源
     * @return  fileBuild   int             1:需要生成json文件
     * */
    private int insertGdsf(String preDrawIssue, String preDrawCode, String platform)
    {
        // 新号码入库
        int fileBuild = 0;
        // ID
        int id = Integer.valueOf(preDrawIssue.substring(2, preDrawIssue.length()));
        // 开奖号码
        String[] arr = new String[] {"168", "6909"};
        String[] openNumber;
        if (command.useArraysBinarySearch(arr, platform)) {
            openNumber = preDrawCode.split(",");
        } else {
            logger.error(lotName + platform + "号码切割失败");
            openNumber = preDrawCode.split("|");
        }
        try {
            // 判断期号是否存在
            Gdsf rel = gdsfService.selectByPrimaryKey(id);
            if (rel == null) {
                Gdsf gdsf = new Gdsf();
                gdsf.setId(id);
                gdsf.setQishu(Long.valueOf(preDrawIssue));
                gdsf.setCreate_time(new Date());
                gdsf.setDatetime(new Date());
                gdsf.setState(0);
                gdsf.setPrev_text(platform);
                gdsf.setBall_1(Integer.valueOf(openNumber[0]));
                gdsf.setBall_2(Integer.valueOf(openNumber[1]));
                gdsf.setBall_3(Integer.valueOf(openNumber[2]));
                gdsf.setBall_4(Integer.valueOf(openNumber[3]));
                gdsf.setBall_5(Integer.valueOf(openNumber[4]));
                gdsf.setBall_6(Integer.valueOf(openNumber[5]));
                gdsf.setBall_7(Integer.valueOf(openNumber[6]));
                gdsf.setBall_8(Integer.valueOf(openNumber[7]));
                try {
                    gdsfService.addGdsf(gdsf);
                    logger.info(lotName + platform + " - 期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode +"，更新成功");
                    fileBuild = 1;
                } catch (Exception e) {
                    logger.info(lotName + platform + " - 期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode +"，插入失败");
                }
                gdsf = null;
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
        String json = command.gdsf();
        command.buildJson(lotName, jsonName, json);
    }
}
