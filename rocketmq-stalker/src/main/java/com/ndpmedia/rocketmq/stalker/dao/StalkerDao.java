package com.ndpmedia.rocketmq.stalker.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by robert.xu on 2015/4/10.
 */
public class StalkerDao {

    @Autowired
    private static JdbcTemplate jdbcTemplate;

    public static int update(String sql){
        System.out.println(sql);
        return jdbcTemplate.update(sql);
    }

    public static JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public static void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        StalkerDao.jdbcTemplate = jdbcTemplate;
    }
}
