package com.ndpmedia.rocketmq.stalker.file;

import com.alibaba.fastjson.JSON;
import com.ndpmedia.rocketmq.stalker.config.DataConfig;

import java.util.*;
import java.util.Map.Entry;

/**
 * Created by robert.xu on 2015/4/10.
 * simple translate:from one type to another
 */
public class TranslateHelper {

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
            map = (Map<String, Object>) JSON.parse(line);
        } catch (Exception e) {
            System.out.println(" this log is not format ." + line);
        }
        return map;
    }

    public static String translateSQLFromMap(Map<String, Object> map) {
        if (null == DataConfig.getProperties().getProperty(map.get("sql") + ""))
            return "";

        String sql = DataConfig.getProperties().getProperty(map.get("sql").toString());
        Set<Entry<String, Object>> set = map.entrySet();

        for (Entry<String, Object> entry : set) {
            sql = sql.replaceAll("#" + entry.getKey(), entry.getValue() + "");
        }

        return sql;
    }
}
