package com.zu.collect.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zu.collect.Command;
import com.zu.collect.model.Shsf;
import com.zu.collect.service.ShsfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/shsf")
public class ShsfController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String lotName = "上海时时乐";
    private String jsonName = "shsf.json";

    @Value("${collect.shsf.11kaijiang}")
    private String shsf11;

    @Value("${collect.shsf.359}")
    private String shsf359;

    @Value("${collect.shsf.caim8}")
    private String shsfcaim8;

    @Autowired
    Command command;

    // 采集地址
    @Autowired
    ShsfService shsfService;

    /**
     * 359采集号码
     * */
    @RequestMapping("/collect")
    public String collect()
    {
        // 是否有新号码，有则生成json文件
        int fileBuild = 0;

        // 获取号码
        try {
            String html;
            html = command.html(shsf359, "", "");
            // 解析获取的json字符串
            if (!html.equals("404")) {
                html = html.replaceAll(" ", "");
                html = html.replaceAll("-", "");
                html = html.replaceAll("\"", "");
                html = html.replaceAll("</span>", "");
                String regex = "<divclass=lotteryPublic_tableBlock>(.*?)</table></div>";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(html);
                String regx = "<tr>(.*?)</tr>";
                String reg = "<td><iclass=font_gray666>(.*?)</i>&nbsp;(.*?)<divclass=number_redAndBluedatatype=historyNumbers>(.*?)</div></td>";
                String preDrawIssue;
                String preDrawCode, number;
                String[] numberList;
                int id;
                int j = 0;
                if (m.find()) {
                    Pattern q = Pattern.compile(regx);
                    Matcher n = q.matcher(m.group(0));
                    while (n.find()) {
                        int i=1;
                        if (j >= 10) break;
                        Pattern r = Pattern.compile(reg);
                        Matcher o = r.matcher(n.group(i));
                        if (o.find()) {
                            preDrawIssue = o.group(1).trim();
                            number = o.group(3).trim();
                            numberList = number.split("<spanclass=font_6F8A97>");
                            preDrawCode = numberList[1] + "," + numberList[2] + "," + numberList[3];
                            id = this.insertShsf(preDrawIssue, preDrawCode, "shsf359");
                            if (id == 1) {
                                fileBuild = 1;
                            }
                            preDrawIssue = null;
                            preDrawCode = null;
                        }
                        j++;
                    }
                }

                html = null;
                number = null;
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
     * 11kaijiang采集号码
     * https://11kaijiang.com/caipiao/lottery/shssl/datasole?startdate=2018-12-28&enddate=2018-12-28
     * */
    @RequestMapping("/collect11")
    public String collect11()
    {
        // 是否有新号码，有则生成json文件
        int fileBuild = 0;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // 获取号码
        try {
            String html;
            html = command.html(shsf11 + "?startdate=" + dateFormat.format(new Date()) + "&enddate=" + dateFormat.format(new Date()), "", "");

            // 解析获取的json字符串
            if (!html.equals("404")) {

                JSONObject number = JSONObject.parseObject(html.trim());
                JSONArray datalist = JSONArray.parseArray(number.getString("list"));
                int num = datalist.size();
                if (num > 10) num = 10;
                String preDrawIssue;
                String preDrawCode;
                int id;
                for (int i=0; i < num; i++) {
                    JSONObject data = JSONObject.parseObject(datalist.getString(i));
                    // 期号
                    preDrawIssue = data.getString("c_t");

                    // 开奖号码
                    preDrawCode = data.getString("c_r");

                    //
                    id = this.insertShsf(preDrawIssue, preDrawCode, "kaijiang11");
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
     * caim8采集号码
     * */
    @RequestMapping("/collectcaim8")
    public String collectcaim8()
    {
        // 是否有新号码，有则生成json文件
        int fileBuild = 0;

        // 获取号码
        try {
            String html;
            html = command.html(shsfcaim8, "", "");
            // 解析获取的json字符串
            if (!html.equals("404")) {
                html = html.replaceAll(" ", "");
                html = html.replaceAll("\"", "");
                html = html.replaceAll("\t", "");
                html = html.replaceAll("</span>", "");
                String regex = "<tableid=lottery-history>(.*?)</table>";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(html);
                String regx = "<tr>(.*?)</tr>";
                String reg = "<td>(.*?)期</td><td>(.*?)</td><td>(.*?)</td>";
                String preDrawIssue;
                String preDrawCode, number;
                String[] numberList;
                int id;
                int j = 0;
                if (m.find()) {
                    Pattern q = Pattern.compile(regx);
                    Matcher n = q.matcher(m.group(0));
                    while (n.find()) {
                        int i=1;
                        if (j >= 10) break;
                        Pattern r = Pattern.compile(reg);
                        Matcher o = r.matcher(n.group(i));
                        if (o.find()) {
                            preDrawIssue = o.group(1).trim();
                            number = o.group(3).trim();
                            numberList = number.split("<spanclass=pointspoints-red>");
                            preDrawCode = numberList[1] + "," + numberList[2] + "," + numberList[3];
                            id = this.insertShsf(preDrawIssue, preDrawCode, "caim8");
                            if (id == 1) {
                                fileBuild = 1;
                            }
                            preDrawIssue = null;
                            preDrawCode = null;
                        }
                        j++;
                    }
                }
                html = null;
                number = null;
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
    private int insertShsf(String preDrawIssue, String preDrawCode, String platform)
    {
        // 新号码入库
        int fileBuild = 0;
        // ID
        int id = Integer.valueOf(preDrawIssue.substring(2, preDrawIssue.length()));
        // 开奖号码
        String[] arr = new String[] {"shsf359", "kaijiang11", "caim8"};
        String[] openNumber;
        if (command.useArraysBinarySearch(arr, platform)) {
            openNumber = preDrawCode.split(",");
        } else {
            openNumber = null;
            logger.error(lotName + platform + "号码切割失败");
        }
        try {
            // 判断期号是否存在
            Shsf rel = shsfService.selectByPrimaryKey(id);
            if (rel == null) {
                Shsf shsf = new Shsf();
                shsf.setId(id);
                shsf.setQishu(Long.valueOf(preDrawIssue));
                shsf.setCreate_time(new Date());
                shsf.setDatetime(new Date());
                shsf.setState(0);
                shsf.setPrev_text(platform);
                shsf.setBall_1(Integer.valueOf(openNumber[0]));
                shsf.setBall_2(Integer.valueOf(openNumber[1]));
                shsf.setBall_3(Integer.valueOf(openNumber[2]));
                try {
                    shsfService.addShsf(shsf);
                    logger.info(lotName + " - 期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode +"，更新成功");
                    fileBuild = 1;
                } catch (Exception e) {
                    logger.info(lotName + " - 期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode +"，插入失败");
                }
                shsf = null;
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
        String json = command.shsf();
        command.buildJson(lotName, jsonName, json);
    }
}
