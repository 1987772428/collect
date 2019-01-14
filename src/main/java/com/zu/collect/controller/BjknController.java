package com.zu.collect.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zu.collect.Command;
import com.zu.collect.model.Bjkn;
import com.zu.collect.service.BjknService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

@RestController
@RequestMapping("/bjkn")
public class BjknController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String lotName = "北京快乐8";
    private String jsonName = "bjkn.json";
    final private String pcddName = "PC蛋蛋";
    final private String pcddJson = "pcdd.json";

    @Value("${collect.bjkn.168}")
    private String bjkn168;

    @Value("${collect.bjkn.newland}")
    private String newland;

    @Autowired
    private BjknService bjknService;

    @Autowired
    Command command;

//    @RequestMapping("/")
//    public String test()
//    {
//        return "ok";
//    }
//
////    @RequestBody()
//    @RequestMapping("/list")
//    public List<Bjkn> selectAllBjkn()
//    {
//        Bjkn bjkn = new Bjkn();
//        bjkn.setOffSet(0);
//        bjkn.setLimit(10);
//        return bjknService.selectAllBjkn(bjkn);
//    }

    /**
     * 采集开奖号码
     */
    @RequestMapping("/collect")
    public String collect()
    {
        // 是否有新号码
        int fileBuild = 0;

        // 系统时间
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String date = dateFormat.format(new Date());
//        System.out.println(date); 2018-15-09 21:15:13
//        System.out.println(new Date()); Wed Aug 22 21:15:13 CST 2018

        try{
            String html;
            html = command.html(bjkn168, "", "");

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
                    id = this.insertBjkn(preDrawIssue, preDrawCode, "s168");
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
        }catch (Exception e){
//            System.out.print("页面打开失败");
            e.printStackTrace();
        }

        return "ok";
    }


    /**
     * 采集开奖号码
     */
    @RequestMapping("/collectnewland")
    public String collectnewland()
    {
        // 是否有新号码
        int fileBuild = 0;

        try{
            String html;
            html = command.html(newland, "", "");

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
                    id = this.insertBjkn(preDrawIssue, preDrawCode, "newland");
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
    private int insertBjkn(String preDrawIssue, String preDrawCode, String platform)
    {
        // 新号码入库
        int fileBuild = 0;
        // ID
        int id = Integer.valueOf(preDrawIssue);
        // 开奖号码
        String[] arr = new String[] {"s168", "newland"};
        String[] openNumber;
        if (command.useArraysBinarySearch(arr, platform)) {
            openNumber = preDrawCode.split(",");
        } else {
            openNumber = null;
            logger.error(lotName + platform + "号码切割失败");
        }
        try {
            // 判断期号是否存在
            Bjkn rel = bjknService.selectByPrimaryKey(id);
            if (rel == null) {
                Bjkn bjkn = new Bjkn();
                bjkn.setId(Integer.valueOf(preDrawIssue));
                bjkn.setQishu(Integer.valueOf(preDrawIssue));
                bjkn.setCreate_time(new Date());
                bjkn.setDatetime(new Date());
                bjkn.setState(0);
                bjkn.setPrev_text(platform);
                bjkn.setBall_1(Integer.valueOf(openNumber[0]));
                bjkn.setBall_2(Integer.valueOf(openNumber[1]));
                bjkn.setBall_3(Integer.valueOf(openNumber[2]));
                bjkn.setBall_4(Integer.valueOf(openNumber[3]));
                bjkn.setBall_5(Integer.valueOf(openNumber[4]));
                bjkn.setBall_6(Integer.valueOf(openNumber[5]));
                bjkn.setBall_7(Integer.valueOf(openNumber[6]));
                bjkn.setBall_8(Integer.valueOf(openNumber[7]));
                bjkn.setBall_9(Integer.valueOf(openNumber[8]));
                bjkn.setBall_10(Integer.valueOf(openNumber[9]));
                bjkn.setBall_11(Integer.valueOf(openNumber[10]));
                bjkn.setBall_12(Integer.valueOf(openNumber[11]));
                bjkn.setBall_13(Integer.valueOf(openNumber[12]));
                bjkn.setBall_14(Integer.valueOf(openNumber[13]));
                bjkn.setBall_15(Integer.valueOf(openNumber[14]));
                bjkn.setBall_16(Integer.valueOf(openNumber[15]));
                bjkn.setBall_17(Integer.valueOf(openNumber[16]));
                bjkn.setBall_18(Integer.valueOf(openNumber[17]));
                bjkn.setBall_19(Integer.valueOf(openNumber[18]));
                bjkn.setBall_20(Integer.valueOf(openNumber[19]));
                try {
                    bjknService.addBjkn(bjkn);
                    logger.info(lotName + platform + " - 期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode +"，更新成功");
                    fileBuild = 1;
                } catch (Exception e) {
                    logger.info(lotName + platform + " - 期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode +"，插入失败");
                }
                bjkn = null;
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
        String json = command.bjkn();
        String[] jsons;
        jsons = json.split("\\|");
        command.buildJson(lotName, jsonName, jsons[0]);
        // 衍生：PC蛋蛋
        command.buildJson(pcddName, pcddJson, jsons[1]);
    }

}
