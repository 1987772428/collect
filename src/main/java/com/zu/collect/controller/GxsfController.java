package com.zu.collect.controller;

import com.zu.collect.Command;
import com.zu.collect.model.Gxsf;
import com.zu.collect.service.GxsfService;
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

import java.util.Date;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/gxsf")
public class GxsfController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String lotName = "广西十分";
    private String jsonName = "gxsf.json";

    @Value("${collect.gxsf.official}")
    private String gxsfOfficial;

    @Autowired
    Command command;

    // 采集地址
    @Autowired
    GxsfService gxsfService;

    @RequestMapping("/collect")
    public String collect()
    {
        // 是否有新号码，有则生成json文件
        int fileBuild = 0;
        // 获取号码
        try{
            String html;
            html = command.html(gxsfOfficial+ "?timestamp=" + System.currentTimeMillis(), "", "http://www.gxcaipiao.com.cn/klsfnt.html");
            System.out.println(html);
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
                        id = this.insertGxsf(preDrawIssue, preDrawCode, "official");
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
    private int insertGxsf(String preDrawIssue, String preDrawCode, String platform)
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
            openNumber = null;
            logger.error(lotName + platform + "号码切割失败");
        }
        try {
            // 判断期号是否存在
            Gxsf rel = gxsfService.selectByPrimaryKey(id);
            if (rel == null) {
                //
                Gxsf gxsf = new Gxsf();
                gxsf.setId(id);
                gxsf.setQishu(Long.valueOf(preDrawIssue));
                gxsf.setCreate_time(new Date());
                gxsf.setDatetime(new Date());
                gxsf.setState(0);
                gxsf.setPrev_text(platform);
                gxsf.setBall_1(Integer.valueOf(openNumber[0]));
                gxsf.setBall_2(Integer.valueOf(openNumber[1]));
                gxsf.setBall_3(Integer.valueOf(openNumber[2]));
                gxsf.setBall_4(Integer.valueOf(openNumber[3]));
                gxsf.setBall_5(Integer.valueOf(openNumber[4]));
                try {
                    gxsfService.addGxsf(gxsf);
                    logger.info(lotName + " - 期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode +"，更新成功");
                    fileBuild = 1;
                } catch (Exception e) {
                    logger.info(lotName + " - 期号：" + preDrawIssue + ", 开奖号码：" + preDrawCode +"，插入失败");
                }
                gxsf = null;
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
        String json = command.gxsf();
        command.buildJson(lotName, jsonName, json);
    }
}
