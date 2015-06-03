package com.ndpmedia.rocketmq.babel;

import com.alibaba.rocketmq.client.log.ClientLogger;
import org.slf4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public final class Helper {

    private static final Logger LOGGER = ClientLogger.getLog();

    private static Properties properties = new Properties();

    static {
        InputStream inputStream = null;
        try {
            ClassLoader classLoader = ConsumerService.class.getClassLoader();
            inputStream = classLoader.getResourceAsStream("sample_rocketmq_client_setting.properties");
            if (null != inputStream) {
                properties.load(inputStream);
            } else {
                LOGGER.error("Unable to find configuration file: rocketmq_client_setting.properties");
                throw new FileNotFoundException("rocketmq_client_setting.properties not found in classpath");
            }
        } catch (IOException e) {
            LOGGER.error("IO Error", e);
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    //Ignore.
                }
            }
        }
    }


    public static Properties getConfig() {
        return properties;
    }


    public static com.alibaba.rocketmq.common.message.Message wrap(Message message) {
        com.alibaba.rocketmq.common.message.Message msg = new com.alibaba.rocketmq.common.message.Message();
        msg.setTopic(message.getTopic());
        msg.setBody(message.getData());
        msg.setFlag(message.getFlag());
        if (null != message.getProperties()) {
            for (Map.Entry<String, String> entry : message.getProperties().entrySet()) {
                msg.putUserProperty(entry.getKey(), entry.getValue());
            }
        }
        return msg;
    }
}
