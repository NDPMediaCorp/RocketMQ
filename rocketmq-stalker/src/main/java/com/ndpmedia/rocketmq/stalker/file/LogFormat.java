package com.ndpmedia.rocketmq.stalker.file;

import com.ndpmedia.rocketmq.stalker.config.DataConfig;
import com.ndpmedia.rocketmq.stalker.dao.StalkerDao;

import java.util.Map;

/**
 * Created by robert.xu on 2015/4/10.
 */
public class LogFormat implements FileFormat {
    @Override
    public boolean check(String log) {
        Map<String, Object> map = TranslateHelper.translateStringToMap(log);
        if (null == DataConfig.getProperties().getProperty(map.get("sql") + "")) return false;

        String sql = TranslateHelper.translateSQLFromMap(map);

        try{
            int i = StalkerDao.update(sql);
            System.out.println(i);
        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }
}
