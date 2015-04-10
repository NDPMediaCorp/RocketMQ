package com.ndpmedia.rocketmq.stalker.config;

import java.io.*;
import java.nio.file.FileSystemAlreadyExistsException;
import java.util.Properties;

/**
 * Created by robert.xu on 2015/4/9.
 */
public class DataConfig implements Constant {

    private static String dataPath = "";

    private static Properties properties = new Properties();

    static {
        init();
        dataPath = properties.getProperty(PROPERTIES_KEY_BASE_DATA_PATH, BASE_DATA_PATH);
    }

    private static void init(){
        try {
            InputStream inputStream = DataConfig.class.getClassLoader().getResourceAsStream("stalker.properties");

            properties.load(inputStream);

            String ex = System.getProperty("sql.config");

            if (null != ex){
                File f = new File(ex);
                properties.load(new FileInputStream(f));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getDataPath() {
        return dataPath;
    }

    public static void setDataPath(String dataPath) {
        DataConfig.dataPath = dataPath;
    }

    public static Properties getProperties() {
        return properties;
    }

    public static void setProperties(Properties properties) {
        DataConfig.properties = properties;
    }
}
