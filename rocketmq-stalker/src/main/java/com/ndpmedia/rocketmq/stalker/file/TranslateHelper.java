package com.ndpmedia.rocketmq.stalker.file;

import com.alibaba.fastjson.JSON;
import com.ndpmedia.rocketmq.stalker.config.DataConfig;

import java.util.*;
import java.util.Map.Entry;


/**
 * Created by robert.xu on 2015/4/10.
 */
public class TranslateHelper {
    public static Map<String, Object> translateStringToMap(String line){
        String base = line.substring(
                line.indexOf(DataConfig.getProperties().getProperty("log.type")) + (DataConfig.getProperties()
                                                                                              .getProperty("log.type"))
                        .length());


        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = (Map<String, Object>) JSON.parse(base);
        }catch (Exception e){
            System.out.println(" this log is not right .");
        }
        return map;
    }

    public static String translateSQLFromMap(Map<String, Object> map){
        String sql = DataConfig.getProperties().getProperty(map.get("sql").toString());
        Set<Entry<String, Object>> set =map.entrySet();

        for (Entry<String, Object> entry:set){
            sql = sql.replaceAll("#" + entry.getKey(), entry.getValue() + "");
        }

        return sql;
    }
}
