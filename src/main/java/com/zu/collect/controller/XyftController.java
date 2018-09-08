package com.zu.collect.controller;

import com.zu.collect.Command;
import com.zu.collect.model.Xyft;
import com.zu.collect.service.XyftService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
@RequestMapping("/xyft")
public class XyftController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String lotName = "幸运飞艇";
    private String jsonName = "xyft.json";

    @Value("${collect.xyft.official}")
    private String xyftOfficial;

    @Value("${collect.xyft.cookies}")
    private String cookies;

    @Autowired
    Command command;

    // 采集地址
    @Autowired
    XyftService xyftService;

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
            CookieManager manager = new CookieManager();
            // 设置cookie策略，只接受与你对话服务器的cookie，而不接收Internet上其它服务器发送的cookie
            manager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
            CookieHandler.setDefault(manager);

            String html;
            html = command.html(xyftOfficial, cookies, "");
            if (!html.equals("404")) {
                html = html.replaceAll(" ", "");
                html = html.replaceAll("\"", "");

                // history
//                html = html.replaceAll("<spanclass=ball1>", "");
//                html = html.replaceAll("historyTableBg", "");
//                String regex = "<trclass=>(.*?)</tr>";
//                Pattern p = Pattern.compile(regex);
//                Matcher  m = p.matcher(html);
//                String regx = "<td>(.*?)</td><td>(.*?)</td><td>(.*?)</td>";

                // index
                html = html.replaceAll("stagesBackground", "");
                String regex = "<divclass=stagesResult>(.*?)</div>";
                Pattern p = Pattern.compile(regex);
                Matcher  m = p.matcher(html);
                String regx = "<fontclass=left>(.*?)</font><spanclass=right>(.*?)</span>";

                String preDrawIssue;
                String preDrawCode;
                int id;
                while (m.find()) {
                    int i=1;
                    Pattern q = Pattern.compile(regx);
                    Matcher n = q.matcher(m.group(i));
                    if (n.find()) {
                        // history
//                        preDrawIssue = n.group(2).trim();
//                        preDrawCode = n.group(3).trim();
//                        openNumber = preDrawCode.split("</span>");

                        // index
                        preDrawIssue = n.group(1).trim().replaceAll("&nbsp;&nbsp;", "");
                        preDrawCode = n.group(2).trim();
//                        openNumber = preDrawCode.split("&nbsp;");

                        id = this.insertXyft(preDrawIssue, preDrawCode, "official");
                        if (id == 1) {
                            fileBuild = 1;
                        }
                        preDrawIssue = null;
                        preDrawCode = null;
                    }
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
    private int insertXyft(String preDrawIssue, String preDrawCode, String platform)
    {
        // 新号码入库
        int fileBuild = 0;
        // ID
        int id = Integer.valueOf(preDrawIssue.substring(2, preDrawIssue.length()));
        // 开奖号码
        String[] arr = new String[] {"official"};
        String[] openNumber;
        if (command.useArraysBinarySearch(arr, platform)) {
            // history
//                openNumber = preDrawCode.split("</span>");
            // index
            openNumber = preDrawCode.split("&nbsp;");
        } else {
            openNumber = null;
            logger.error(lotName + platform + "号码切割失败");
        }
        try {
            // 判断期号是否存在
            Xyft rel = xyftService.selectByPrimaryKey(id);
            if (rel == null) {
                Xyft xyft = new Xyft();
                xyft.setId(id);
                xyft.setQishu(Long.valueOf(preDrawIssue));
                xyft.setCreate_time(new Date());
                xyft.setDatetime(new Date());
                xyft.setState(0);
                xyft.setPrev_text(platform);
                xyft.setBall_1(Integer.valueOf(openNumber[0]));
                xyft.setBall_2(Integer.valueOf(openNumber[1]));
                xyft.setBall_3(Integer.valueOf(openNumber[2]));
                xyft.setBall_4(Integer.valueOf(openNumber[3]));
                xyft.setBall_5(Integer.valueOf(openNumber[4]));
                xyft.setBall_6(Integer.valueOf(openNumber[5]));
                xyft.setBall_7(Integer.valueOf(openNumber[6]));
                xyft.setBall_8(Integer.valueOf(openNumber[7]));
                xyft.setBall_9(Integer.valueOf(openNumber[8]));
                xyft.setBall_10(Integer.valueOf(openNumber[9]));
                try {
                    xyftService.addXyft(xyft);
                    logger.info(lotName + " - 期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode +"，更新成功");
                    fileBuild = 1;
                } catch (Exception e) {
                    logger.info(lotName + " - 期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode +"，插入失败");
                }
                xyft = null;
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
        String json = command.xyft();
        command.buildJson(lotName, jsonName, json);
    }
}
