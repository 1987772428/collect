package com.zu.collect;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zu.collect.model.*;
import com.zu.collect.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
public class Command {

    @Autowired
    CreatFile creatFile;

    @Autowired
    BjpkService bjpkService;

    @Autowired
    BjknService bjknService;

    @Autowired
    XyftService xyftService;

    @Autowired
    CqsfService cqsfService;

    @Autowired
    CqService cqService;

    @Autowired
    GdsfService gdsfService;

    @Autowired
    ShsfService shsfService;

    @Autowired
    Gd11Service gd11Service;

    @Autowired
    GxsfService gxsfService;

    @Autowired
    D3Service d3Service;

    @Autowired
    P3Service p3Service;

    @Autowired
    TjService tjService;

    @Autowired
    TjsfService tjsfService;

    private List<?> list = new ArrayList<>();

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取url页面内容
     * @param urlInfo   String      目标连接地址
     * @param cookies   String      设置的cookies
     * @param referer   String      referer地址
     * @return html     String      目标内容
     * */
    public String html(String urlInfo, String cookies, String referer)
    {
        // Fiddler 抓包设置
        System.setProperty("http.proxyHost", "localhost");
        System.setProperty("http.proxyPort", "8888");
        System.setProperty("https.proxyHost", "localhost");
        System.setProperty("https.proxyPort", "8888");

        String html;
        try{
            // java.net.ProtocolException: Server redirected too many times (20)
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
            URL url = new URL(urlInfo);
            HttpURLConnection httpUrl = (HttpURLConnection)url.openConnection();
            httpUrl.setConnectTimeout(8000);
            httpUrl.setReadTimeout(8000);
            // 防止服务器默认的跳转
            // httpUrl.setInstanceFollowRedirects(false);
            // 设置Cookies
            if (!cookies.equals("")) {
                httpUrl.setRequestProperty("Cookie", cookies);
            }
            // 设置referer
            if (!referer.equals("")) {
                httpUrl.setRequestProperty("Referer", referer);
            }
//            httpUrl.setRequestProperty("Connection", "keep-alive");
//            httpUrl.setRequestProperty("Content-Type", "application/json; charset=utf-8");

//            String visitIP = "107.154.196.248:443";
//            httpUrl.setRequestProperty("X-Forwarded-For",visitIP);
//            httpUrl.setRequestProperty("Proxy-Client-IP",visitIP);
//            httpUrl.setRequestProperty("WL-Proxy-Client-IP",visitIP);
//            httpUrl.setRequestProperty("HTTP_CLIENT_IP",visitIP);
//            httpUrl.setRequestProperty("HTTP_X_FORWARDED_FOR",visitIP);
//            httpUrl.setRequestProperty("REMOTE_ADDR",visitIP);
//            httpUrl.setRequestProperty("Host", "www.cp098.com");

            // 防止报403错误。
            httpUrl.setRequestProperty("User-Agent", "Mozilla/31.0 (compatible; MSIE 10.0; Windows NT; DigExt)");
            // 返回码
            // int responsecode = httpUrl.getResponseCode();
            InputStream is = httpUrl.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            is.close();
            br.close();
            html = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error( urlInfo + "获取内容失败");
            html = "404";
        }
        return html;
    }

    /**
     * 判断元素是否存在数组中
     * @param arr           String[]    目标数组
     * @param targetValue   String      查找的字符串
     * @return boolean
     * */
    public boolean useArraysBinarySearch(String[] arr, String targetValue)
    {
        int a =  Arrays.binarySearch(arr, targetValue);
        if(a >= 0)
            return true;
        else
            return false;
    }

    /**
     * 生成json文件
     * @param lotName   String      彩种
     * @param jsonName  String      生成的json文件名称
     * @param json      String      生成的json内容
     * */
    public void buildJson(String lotName, String jsonName, String json)
    {
        logger.info("生成" + lotName + "json文件开始");
        // 生成json文件
        try {
            if (json.equals("null")) {
                logger.error("获取" + lotName + "JSON字符串失败");
            } else {
                Boolean file = creatFile.createFile(jsonName, json);
                if (file) {
                    logger.info("生成" + lotName + "json文件成功");
                } else {
                    logger.error("生成" + lotName + "json文件失败");
                }
                file = null;
            }
            json = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取北京快乐8历史开奖号码
    public String bjkn()
    {
        String string = "null";
        try {
            Bjkn bjkn = new Bjkn();
            bjkn.setOffSet(0);
            bjkn.setLimit(10);
            list = bjknService.selectAllBjkn(bjkn);
            // 生成json字符串
            string = json();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    // 获取北京PK10历史开奖号码
    public String bjpk()
    {
        String string = "null";
        try {
            Bjpk bjpk = new Bjpk();
            bjpk.setOffSet(0);
            bjpk.setLimit(10);
            list = bjpkService.selectAllBjpk(bjpk);
            // 生成json字符串
            string = json();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    // 获取幸运飞艇历史开奖号码
    public String xyft()
    {
        String string = "null";
        try {
            Xyft xyft = new Xyft();
            xyft.setOffSet(0);
            xyft.setLimit(10);
            list = xyftService.selectAllXyft(xyft);
            // 生成json字符串
            string = json();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    // 获取重庆十分（幸运农场）历史开奖号码
    public String cqsf()
    {
        String string = "null";
        try {
            Cqsf cqsf = new Cqsf();
            cqsf.setOffSet(0);
            cqsf.setLimit(10);
            list = cqsfService.selectAllCqsf(cqsf);
            // 生成json字符串
            string = json();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    // 获取重庆时时彩历史开奖号码
    public String cq()
    {
        String string = "null";
        try {
            Cq cq = new Cq();
            cq.setOffSet(0);
            cq.setLimit(10);
            list = cqService.selectAllCq(cq);
            // 生成json字符串
            string = json();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    // 获取广东十分历史开奖号码
    public String gdsf()
    {
        String string = "null";
        try {
            Gdsf gdsf = new Gdsf();
            gdsf.setOffSet(0);
            gdsf.setLimit(10);
            list = gdsfService.selectAllGdsf(gdsf);
            // 生成json字符串
            string = json();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    // 获取上海时时乐历史开奖号码
    public String shsf()
    {
        String string = "null";
        try {
            Shsf shsf = new Shsf();
            shsf.setOffSet(0);
            shsf.setLimit(10);
            list = shsfService.selectAllShsf(shsf);
            // 生成json字符串
            string = json();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    // 获取广东11选5历史开奖号码
    public String gd11()
    {
        String string = "null";
        try {
            Gd11 gd11 = new Gd11();
            gd11.setOffSet(0);
            gd11.setLimit(10);
            list = gd11Service.selectAllGd11(gd11);
            // 生成json字符串
            string = json();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    // 获取广西十分历史开奖号码
    public String gxsf()
    {
        String string = "null";
        try {
            Gxsf gxsf = new Gxsf();
            gxsf.setOffSet(0);
            gxsf.setLimit(10);
            list = gxsfService.selectAllGxsf(gxsf);
            // 生成json字符串
            string = json();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    // 获取福彩3D历史开奖号码
    public String d3()
    {
        String string = "null";
        try {
            D3 d3 = new D3();
            d3.setOffSet(0);
            d3.setLimit(10);
            list = d3Service.selectAllD3(d3);
            // 生成json字符串
            string = json();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    // 获取排列三历史开奖号码
    public String p3()
    {
        String string = "null";
        try {
            P3 p3 = new P3();
            p3.setOffSet(0);
            p3.setLimit(10);
            list = p3Service.selectAllP3(p3);
            // 生成json字符串
            string = json();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    // 获取天津时时彩历史开奖号码
    public String tj()
    {
        String string = "null";
        try {
            Tj tj = new Tj();
            tj.setOffSet(0);
            tj.setLimit(10);
            list = tjService.selectAllTj(tj);
            // 生成json字符串
            string = json();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    // 获取天津十分历史开奖号码
    public String tjsf()
    {
        String string = "null";
        try {
            Tjsf tjsf = new Tjsf();
            tjsf.setOffSet(0);
            tjsf.setLimit(10);
            list = tjsfService.selectAllTjsf(tjsf);
            // 生成json字符串
            string = json();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    // 生成json字符串
    private String json()
    {
        // 系统时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//        StringBuilder stringBuilder = new StringBuilder(); // 单线程
        StringBuffer stringBuffer = new StringBuffer();
        for (Object str : list) {
            JSONObject data = JSONObject.parseObject(JSON.toJSONString(str));
            String create_time = dateFormat.format(data.getDate("create_time"));
            data.put("create_time", create_time);
            String datetime = dateFormat.format(data.getDate("datetime"));
            data.put("datetime", datetime);
            stringBuffer.append(data);
            stringBuffer.append(",");
        }
        String json = stringBuffer.toString();
        json = json.substring(0, json.length() - 1);
        json = "[" + json + "]";
        list = null;
        return json;
    }
}
