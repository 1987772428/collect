package com.zu.collect.controller;

import com.zu.collect.Command;
import com.zu.collect.model.Shsf;
import com.zu.collect.service.ShsfService;
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
@RequestMapping("/shsf")
public class ShsfController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String lotName = "上海时时乐";
    private String jsonName = "shsf.json";

    @Value("${collect.shsf.official}")
    private String shsfOfficial;

    @Autowired
    Command command;

    // 采集地址
    @Autowired
    ShsfService shsfService;

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
            html = command.html(shsfOfficial, "");
            // 解析获取的json字符串
            if (!html.equals("404")) {
                html = html.replaceAll(" ", "");
                String regex = "employee.push\\((.*?)\\)";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(html);
                String regx = "id':'(.*?)','c':'(.*?)'";
//                employee.push({'id':'2018090208','c':'2,3,9','type':'三不同号','t':'2018-09-02','h':'14','kd':'7','dxb':'1:2','job':'2:1','zhb':'2:1'})
                String preDrawIssue;
                String preDrawCode;
                int id;
                int j = 0;
                while (m.find()) {
                    int i=1;
                    if (j >= 10) break;
                    Pattern q = Pattern.compile(regx);
                    Matcher n = q.matcher(m.group(i));
                    if (n.find()) {
                        preDrawIssue = n.group(1).trim();
                        preDrawCode = n.group(2).trim();
                        id = this.insertShsf(preDrawIssue, preDrawCode, "official");
                        if (id == 1) {
                            fileBuild = 1;
                        }
                        preDrawIssue = null;
                        preDrawCode = null;
                    }
                    j++;
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
    private int insertShsf(String preDrawIssue, String preDrawCode, String platform)
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
            Shsf rel = shsfService.selectByPrimaryKey(id);
            if (rel == null) {
                Shsf shsf = new Shsf();
                shsf.setId(id);
                shsf.setQishu(Long.valueOf(preDrawIssue));
                shsf.setCreate_time(new Date());
                shsf.setDatetime(new Date());
                shsf.setState(0);
                shsf.setPrev_text("official");
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
