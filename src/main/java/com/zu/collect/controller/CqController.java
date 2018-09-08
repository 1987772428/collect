package com.zu.collect.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zu.collect.Command;
import com.zu.collect.model.Cq;
import com.zu.collect.service.CqService;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/cq")
public class CqController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String lotName = "重庆时时彩";
    private String jsonName = "cq.json";

    @Value("${collect.cq.500}")
    private String cq500;

    @Value("${collect.cq.168}")
    private String cq168;

    @Value("${collect.cq.6909}")
    private String cq6909;

    @Value("${collect.cq.sina}")
    private String cqsina;

    @Autowired
    Command command;

    // 采集地址
    @Autowired
    CqService cqService;

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
            html = command.html(cq168, "", "");

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
                    //
                    id = this.insertCq(preDrawIssue, preDrawCode, "s168");
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

    @RequestMapping("/collect500")
    public String collectXML500()
    {
        // 是否有新号码，有则生成json文件
        int fileBuild = 0;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        // 获取号码
        try{
            String html;
            html = command.html(cq500 + dateFormat.format(new Date()) + ".xml?_A=OYWGEBUK" + System.currentTimeMillis(), "", "");
            if (!html.equals("404")) {

                // 解析xml文件
                try {
                    // 通过reader对象的read方法加载books.xml文件,获取docuemnt对象。
                    Document document = DocumentHelper.parseText(html);
                    // 通过document对象获取根节点xml
                    Element xml = document.getRootElement();
                    // 通过element对象的elementIterator方法获取迭代器
                    Iterator it = xml.elementIterator();
                    // 遍历迭代器，获取根节点中的信息
                    String preDrawIssue;
                    String preDrawCode;
                    int id;
                    int i = 0;
                    while (it.hasNext()) {
                        if (i >= 10) break;
                        i++;
                        Element data = (Element) it.next();
                        // 获取data的属性名以及 属性值
                        List<Attribute> dataAttrs = data.attributes();
                        // 期号
                        preDrawIssue = dataAttrs.get(0).getValue();
                        // 开奖号码
                        preDrawCode = dataAttrs.get(1).getValue();
                        //
                        id = this.insertCq(preDrawIssue, preDrawCode, "s500");
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
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(lotName + "解析数据异常");
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
            html = command.html(cq6909 + System.currentTimeMillis(), "", "");
//            {"qq":"93058680","rows":[{"id":"95226","termNum":"20180903046","lotteryTime":"2018-09-03 13:40:00","n1":1,"n2":5,"n3":5,"n4":6,"n5":0},{"id":"95225","termNum":"20180903045","lotteryTime":"2018-09-03 13:30:00","n1":4,"n2":3,"n3":5,"n4":0,"n5":6}],"success":true}
            // 解析获取的json字符串
            if (!html.equals("404")) {
                JSONObject number = JSONObject.parseObject(html.trim());
                JSONArray datalist = JSONArray.parseArray(number.getString("rows"));
                int num = datalist.size();
                if (num > 10) num = 10;
                String preDrawIssue;
                String preDrawCode;
                String n1,n2,n3,n4,n5;
                int id;
                for (int i=0; i < num; i++) {
                    JSONObject data = JSONObject.parseObject(datalist.getString(i));
                    // 期号
                    preDrawIssue = data.getString("termNum");
                    // 开奖号码
                    n1 = data.getString("n1");
                    n2 = data.getString("n2");
                    n3 = data.getString("n3");
                    n4 = data.getString("n4");
                    n5 = data.getString("n5");
                    preDrawCode = n1 + "," + n2 + "," + n3 + "," + n4 + "," + n5;
                    //
                    id = this.insertCq(preDrawIssue, preDrawCode, "s6909");
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
     * sina采集
     * */
    @RequestMapping("/collectsina")
    public String collectsina()
    {
        // 是否有新号码，有则生成json文件
        int fileBuild = 0;

        // 获取号码
        try{
            String html;
            html = command.html(cqsina + System.currentTimeMillis(), "", "");
            // 正则匹配html内容
            if (!html.equals("404")) {
                html = html.replaceAll(" ", "");
                html = html.replaceAll("\"", "");
                html = html.replaceAll("-", "");
                html = html.replaceAll("期", "");
                html = html.replaceAll("class=bg", "");
                html = html.replaceAll("\\t", "");

                String regex = "<tr>(.*?)</tr>";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(html);
                String regx = "<td>(.*?)</td><tdclass=tdright>[\\s\\S]*<tdstyle=color:red>(.*?)</td>";

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
                        id = this.insertCq(preDrawIssue, preDrawCode, "sina");
                        if (id == 1) {
                            fileBuild = 1;
                        }
                        preDrawIssue = null;
                        preDrawCode = null;
                    }
                    j++;
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
    private int insertCq(String preDrawIssue, String preDrawCode, String platform)
    {
        // 新号码入库
        int fileBuild = 0;
        // ID
        int id = Integer.valueOf(preDrawIssue.substring(2, preDrawIssue.length()));
        // 开奖号码
        String[] arr = new String[] {"s168", "s500", "s6909"};
        String[] arr2 = new String[] {"sina"};
        String[] openNumber;
        if (command.useArraysBinarySearch(arr, platform)) {
            openNumber = preDrawCode.split(",");
        } else if (command.useArraysBinarySearch(arr2, platform)) {
            openNumber = preDrawCode.split("\\|");
        } else {
            openNumber = null;
            logger.error(lotName + platform + "号码切割失败");
        }
        System.out.println(Arrays.toString(openNumber));
        try {
            // 判断期号是否存在
            Cq rel = cqService.selectByPrimaryKey(id);
            if (rel == null) {
                //
                Cq cq = new Cq();
                cq.setId(id);
                cq.setQishu(Long.valueOf(preDrawIssue));
                cq.setCreate_time(new Date());
                cq.setDatetime(new Date());
                cq.setState(0);
                cq.setPrev_text(platform);
                cq.setBall_1(Integer.valueOf(openNumber[0]));
                cq.setBall_2(Integer.valueOf(openNumber[1]));
                cq.setBall_3(Integer.valueOf(openNumber[2]));
                cq.setBall_4(Integer.valueOf(openNumber[3]));
                cq.setBall_5(Integer.valueOf(openNumber[4]));
                try {
                    cqService.addCq(cq);
                    logger.info(lotName + platform + " - 期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode +"，更新成功");
                    fileBuild = 1;
                } catch (Exception e) {
                    logger.info(lotName + platform + " - 期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode +"，插入失败");
                }
                cq = null;
            }
            openNumber = null;
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
        String json = command.cq();
        command.buildJson(lotName, jsonName, json);
    }

}
