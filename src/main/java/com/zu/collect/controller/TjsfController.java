package com.zu.collect.controller;

import com.zu.collect.Command;
import com.zu.collect.model.Tjsf;
import com.zu.collect.service.TjsfService;
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
@RequestMapping("/tjsf")
public class TjsfController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String lotName = "天津十分";
    private String jsonName = "tjsf.json";

    @Value("${collect.tjsf.official}")
    private String tjsfOfficial;

    @Autowired
    Command command;

    // 采集地址
    @Autowired
    TjsfService tjsfService;

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
            html = command.html(tjsfOfficial, "", "");
            // 解析获取的json字符串
            if (!html.equals("404")) {
                html = html.replaceAll(" ", "");
                String regex = "table_add_one_tr\\(\"(.*?)\"\\)";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(html);
                String preDrawIssue;
                String preDrawCode;
                int id;
                int j = 0;
                String str;
                String[] strArr;
                while (m.find()) {
                    int i=1;
                    if (j >= 10) break;
                    str = m.group(i).replaceAll("\"", "");
                    strArr = str.split(",");
                    preDrawIssue = strArr[0].trim();
                    preDrawCode = strArr[1].trim();
                    id = this.insertTjsf(preDrawIssue, preDrawCode, "official");
                    if (id == 1) {
                        fileBuild = 1;
                    }
                    j++;
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
    private int insertTjsf(String preDrawIssue, String preDrawCode, String platform)
    {
        // 新号码入库
        int fileBuild = 0;
        // ID
        int id = Integer.valueOf(preDrawIssue.substring(2, preDrawIssue.length()));
        // 开奖号码
        String[] arr = new String[] {"official"};
        String[] openNumber;
        if (command.useArraysBinarySearch(arr, platform)) {
            openNumber = preDrawCode.split("\\|");
        } else {
            openNumber = null;
            logger.error(lotName + platform + "号码切割失败");
        }
        try {
            // 判断期号是否存在
            Tjsf rel = tjsfService.selectByPrimaryKey(id);
            if (rel == null) {
                Tjsf tjsf = new Tjsf();
                tjsf.setId(id);
                tjsf.setQishu(Long.valueOf(preDrawIssue));
                tjsf.setCreate_time(new Date());
                tjsf.setDatetime(new Date());
                tjsf.setState(0);
                tjsf.setPrev_text(platform);
                tjsf.setBall_1(Integer.valueOf(openNumber[0]));
                tjsf.setBall_2(Integer.valueOf(openNumber[1]));
                tjsf.setBall_3(Integer.valueOf(openNumber[2]));
                tjsf.setBall_4(Integer.valueOf(openNumber[3]));
                tjsf.setBall_5(Integer.valueOf(openNumber[4]));
                tjsf.setBall_6(Integer.valueOf(openNumber[5]));
                tjsf.setBall_7(Integer.valueOf(openNumber[6]));
                tjsf.setBall_8(Integer.valueOf(openNumber[7]));
                try {
                    tjsfService.addTjsf(tjsf);
                    logger.info(lotName + " - 期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode +"，更新成功");
                    fileBuild = 1;
                } catch (Exception e) {
                    logger.info(lotName + " - 期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode +"，插入失败");
                }
                tjsf = null;
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
        String json = command.tjsf();
        command.buildJson(lotName, jsonName, json);
    }
}
