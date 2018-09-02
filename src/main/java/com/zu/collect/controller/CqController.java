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
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
            html = command.html(cq168, "");

            // 解析获取的json字符串
            if (!html.equals("404")) {
                JSONObject number = JSONObject.parseObject(html.trim());
                JSONObject  result = JSONObject.parseObject(number.getString("result"));
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
                    id = this.insertCq(preDrawIssue, preDrawCode, "168");
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

//    @RequestMapping("/collect500")
    public String collectXML500()
    {
        // 是否有新号码，有则生成json文件
        int fileBuild = 0;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        // 获取号码
        try{
            String html;
            html = command.html(cq500 + dateFormat.format(new Date()) + ".xml?_A=OYWGEBUK" + System.currentTimeMillis(), "");
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
                        id = this.insertCq(preDrawIssue, preDrawCode, "500");
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
        String[] arr = new String[] {"168", "500"};
        String[] openNumber;
        if (command.useArraysBinarySearch(arr, platform)) {
            openNumber = preDrawCode.split(",");
        } else {
            logger.error(lotName + "号码切割失败");
            openNumber = preDrawCode.split("|");
        }
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
                    logger.info(lotName + " - 期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode +"，更新成功");
                    fileBuild = 1;
                } catch (Exception e) {
                    logger.info(lotName + " - 期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode +"，插入失败");
                }
                cq = null;
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
        String json = command.cq();
        command.buildJson(lotName, jsonName, json);
    }

}
