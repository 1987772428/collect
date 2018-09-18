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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/cqsf")
public class CqsfController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String lotName = "重庆十分（幸运农场）";
    private String jsonName = "cqsf.json";

    @Value("${collect.cqsf.168}")
    private String cqsf168;

    @Value("${collect.cqsf.6909}")
    private String cqsf6909;

    @Value("${collect.cqsf.official2}")
    private String cqsfOfficial2;

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
            html = command.html(cqsf168, "", "");
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
                    id = this.insertCqsf(preDrawIssue, preDrawCode, "s168");
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
//                    System.out.println("生成北京快乐8json文件开始");
                    this.buildJson();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }

    /**
     * 6909采集
     * */
    @RequestMapping("/collect6909")
    public String collect6909()
    {
        // 是否有新号码，有则生成json文件
        int fileBuild = 0;

        // 获取号码
        try{
            String html;
            html = command.html(cqsf6909 + System.currentTimeMillis(), "", "");
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
                for (int i=0; i < num; i++) {
                    JSONObject data = JSONObject.parseObject(datalist.getString(i));
                    // 期号
                    preDrawIssue = data.getString("termNum");
                    preDrawIssue = preDrawIssue.substring(0, 8) + "0" + preDrawIssue.substring(8);
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
                    //
                    id = this.insertCqsf(preDrawIssue, preDrawCode, "s6909");
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
     * 官方采集
     * */
    @RequestMapping("/collect2")
    public String collect2()
    {
        // 是否有新号码，有则生成json文件
        int fileBuild = 0;

        // 获取号码
        try{
            String html;
            html = command.html(cqsfOfficial2, "", "");
            // 解析获取的json字符串
            if (!html.equals("404")) {
                html = html.replaceAll(" ", "");
                String regex = "varCon_BonusCode=\"(.*?)\";";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(html);
                String preDrawIssue;
                String preDrawCode;
                int id;
                String[] str;
                String[] strArr;
                while (m.find()) {
                    int i=1;
                    str = m.group(i).split(";");
                    int start = str.length - 1;
                    int size = str.length - 10;
                    for(int j=start; j>size; j--) {
                        strArr = str[j].split("=");
                        preDrawIssue = "20" + strArr[0].trim();
                        preDrawCode = strArr[1].trim();
                        id = this.insertCqsf(preDrawIssue, preDrawCode, "official");
                        if (id == 1) {
                            fileBuild = 1;
                        }
                    }
                }
                preDrawIssue = null;
                preDrawCode = null;
                str = null;
                strArr = null;
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
    private int insertCqsf(String preDrawIssue, String preDrawCode, String platform)
    {
        // 新号码入库
        int fileBuild = 0;
        // ID
        int id = Integer.valueOf(preDrawIssue.substring(2, preDrawIssue.length()));
        // 开奖号码
        String[] arr = new String[] {"official", "s6909", "s168"};
        String[] openNumber;
        if (command.useArraysBinarySearch(arr, platform)) {
            openNumber = preDrawCode.split(",");
        } else {
            openNumber = null;
            logger.error(lotName + platform + "号码切割失败");
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
                cqsf.setPrev_text(platform);
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
                    logger.info(lotName + platform + " - 期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode +"，更新成功");
                    fileBuild = 1;
                } catch (Exception e) {
                    logger.info(lotName + platform + " - 期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode +"，插入失败");
                }
                cqsf = null;
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
        String json = command.cqsf();
        command.buildJson(lotName, jsonName, json);
    }
}
