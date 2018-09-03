package com.zu.collect.controller;

import com.zu.collect.Command;
import com.zu.collect.model.Gd11;
import com.zu.collect.service.Gd11Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/gd11")
public class Gd11Controller {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String lotName = "广东11选5";
    private String jsonName = "gd11.json";

    @Value("${collect.gd11.official}")
    private String gd11Official;

    @Autowired
    Command command;

    // 采集地址
    @Autowired
    Gd11Service gd11Service;

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
            html = command.html(gd11Official, "");
            // 解析获取的json字符串
            if (!html.equals("404")) {
                html = html.replaceAll(" ", "");
                html = html.replaceAll("\"", "");
                html = html.replaceAll("\\t", "");
//                html = html.replaceAll("(\\r\\n|\\r|\\n|\\n\\r)", "");
                String regex = "<tr>(.*?)</tr>";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(html);
                String regx = "<tdheight=20align=centerbgcolor=#FFFFFF>(.*?)</td><tdheight=20width=154align=centerbgcolor=#FFFF99><spanstyle=width:140px><strong>(.*?)</strong>";
                String preDrawIssue;
                String preDrawCode;
                int id;
                // 匹配元素存入数组，倒序获取
                List<String> list = new ArrayList<>();
                while (m.find()) {
                    int i=1;
                    list.add(m.group(i));
                }
                // 数组下标对应实际位置减1
                int length = list.size() - 1;
                // 每次获取10条数据
                int maxSize = length - 10;
                for (int j = length; j >= maxSize; j--) {
                    System.out.println(list.get(j));
                    Pattern q = Pattern.compile(regx);
                    Matcher n = q.matcher(list.get(j));
                    if (n.find()) {
                        preDrawIssue = n.group(1).trim();
                        preDrawCode = n.group(2).trim();
                        id = this.insertGd11(preDrawIssue, preDrawCode, "official");
                        if (id == 1) {
                            fileBuild = 1;
                        }
                        preDrawIssue = null;
                        preDrawCode = null;
                    }
                }
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
    private int insertGd11(String preDrawIssue, String preDrawCode, String platform)
    {
        // 新号码入库
        int fileBuild = 0;
        // ID
        int id = 0;
        // 期号
        String issue = "0";
        // 开奖号码
        String[] arr = new String[] {"official"};
        String[] openNumber;
        if (command.useArraysBinarySearch(arr, platform)) {
            openNumber = preDrawCode.split("，");
            id = Integer.valueOf(preDrawIssue);
            issue = "20" + preDrawIssue;
        } else {
            logger.error(lotName + "号码切割失败");
            openNumber = preDrawCode.split("|");
        }
        try {
            // 判断期号是否存在
            Gd11 rel = gd11Service.selectByPrimaryKey(id);
            if (rel == null) {
                Gd11 gd11 = new Gd11();
                gd11.setId(id);
                gd11.setQishu(Long.valueOf(issue));
                gd11.setCreate_time(new Date());
                gd11.setDatetime(new Date());
                gd11.setState(0);
                gd11.setPrev_text("official");
                gd11.setBall_1(Integer.valueOf(openNumber[0]));
                gd11.setBall_2(Integer.valueOf(openNumber[1]));
                gd11.setBall_3(Integer.valueOf(openNumber[2]));
                gd11.setBall_4(Integer.valueOf(openNumber[3]));
                gd11.setBall_5(Integer.valueOf(openNumber[4]));
                try {
                    gd11Service.addGd11(gd11);
                    logger.info(lotName + " - 期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode +"，更新成功");
                    fileBuild = 1;
                } catch (Exception e) {
                    logger.info(lotName + " - 期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode +"，插入失败");
                }
                gd11 = null;
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
        String json = command.gd11();
        command.buildJson(lotName, jsonName, json);
    }
}
