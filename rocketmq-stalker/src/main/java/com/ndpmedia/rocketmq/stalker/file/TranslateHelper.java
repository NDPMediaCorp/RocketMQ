package com.ndpmedia.rocketmq.stalker.file;

import com.alibaba.fastjson.JSON;
import com.ndpmedia.rocketmq.stalker.config.Constant;
import com.ndpmedia.rocketmq.stalker.config.DataConfig;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;

/**
 * Created by robert.xu on 2015/4/10.
 * simple translate:from one type to another
 */
public class TranslateHelper implements Constant{

    /**
     * if source string have simple format like ｛“”：“”，“”：“”｝。 try to translate it to map
     * @param line
     * @return
     */
    public static Map<String, Object> translateStringToMap(String line) {

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            //simple translate:input log must  standard format
            //noinspection unchecked
            if (!line.trim().startsWith("{"))
                line = "{" + line;
            if (!line.trim().endsWith("}"))
                line = line + "}";
            map = (Map<String, Object>) JSON.parse(line);
        } catch (Exception e) {
            System.out.println(" this log is not format ." + line);
        }
        return map;
    }

    public static String translateSQLFromMap(Map<String, Object> map) {
        String sql = DataConfig.getProperties().getProperty("sql.producer.insert");

        Set<Entry<String, Object>> set = map.entrySet();

        for (Entry<String, Object> entry : set) {
            String re = Matcher.quoteReplacement(entry.getValue() + "");
            sql = sql.replaceAll("#" + entry.getKey(), re);
        }

        return sql;
    }
}
