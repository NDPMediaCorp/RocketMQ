package com.ndpmedia.rocketmq.stalker.file;

import com.alibaba.fastjson.JSON;
import com.ndpmedia.rocketmq.stalker.config.Constant;

import java.util.*;
import java.util.Map.Entry;

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
        StringBuffer sql = new StringBuffer(" INSERT INTO message_flow");

        Set<Entry<String, Object>> set = map.entrySet();

        getKeys(sql, set);

        sql.append(" values(");
        int index = 0;
        for (Entry<String, Object> entry : set) {
            if (0 != index++)
                sql.append(",");
            sql.append("'");
            sql.append(entry.getValue());
            sql.append("'");
        }
        sql.append(");");

        return sql.toString();
    }

    private static void getKeys(StringBuffer sql, Set<Entry<String, Object>> set){
        sql.append("(");
        int index = 0;
        for (Entry<String, Object> entry:set){
            if (0 != index++)
                sql.append(", ");
            sql.append(sqlKey.get(entry.getKey()));
        }

        sql.append(")");
    }

    private static Map<String, String> sqlKey = new HashMap<String, String>();

    static{
        sqlKey.put("Topic", "topic");
        sqlKey.put("Tags", "tags");
        sqlKey.put("MsgId", "msg_id");
        sqlKey.put("TracerId", "tracer_id");
        sqlKey.put("BornHost", "born_host");
        sqlKey.put("Status", "status");
        sqlKey.put("Source", "source");
        sqlKey.put("ProducerGroup", "producer_group");
        sqlKey.put("Broker", "broker");
        sqlKey.put("MessageQueue", "message_queue");
        sqlKey.put("OffSet", "offset");
        sqlKey.put("TimeStamp", "time_stamp");
        sqlKey.put("ConsumerGroup", "consumer_group");
        sqlKey.put("Client", "client");
        sqlKey.put("From", "ip_from");
        sqlKey.put("To", "ip_to");
    }
}
