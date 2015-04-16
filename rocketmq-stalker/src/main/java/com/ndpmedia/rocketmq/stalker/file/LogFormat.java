package com.ndpmedia.rocketmq.stalker.file;

import com.ndpmedia.rocketmq.stalker.config.DataConfig;
import com.ndpmedia.rocketmq.stalker.dao.StalkerDao;

import java.util.Map;

/**
 * Created by robert.xu on 2015/4/10.
 * log format just check log file
 */
public class LogFormat implements FileFormat {

    /**
     * appoint log must like XXXXlog.typeYYYY, and YYYY will translate to map.
     * @param log
     * @return
     */
    @Override
    public boolean check(String log) {
        String base = log.substring(
                log.indexOf(DataConfig.getProperties().getProperty("log.type")) + (DataConfig.getProperties()
                        .getProperty("log.type")).length());

        Map<String, Object> map = TranslateHelper.translateStringToMap(base);

        String sql = TranslateHelper.translateSQLFromMap(map);

        try {
            //记录SQL影响行数
            int i = StalkerDao.update(sql);
        } catch (Exception e) {
            System.out.println(" try to save file analysis result failed.");
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
