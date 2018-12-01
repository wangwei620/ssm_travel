package com.itheima.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ResourceBundle;

public class JedisUtils {
    private static JedisPool pool = null;
    static{
        // 读取配置文件 读取properties文件
        ResourceBundle rb = ResourceBundle.getBundle("jedis");
        int maxTotal = Integer.parseInt(rb.getString("maxTotal"));
        int maxIdle = Integer.parseInt(rb.getString("maxIdle"));
        int port = Integer.parseInt(rb.getString("port"));
        String host = rb.getString("host");
        System.out.println(maxIdle+" - "+maxTotal+" - "+port+" - "+host);

        // 1.创建连接池配置信息对象
        JedisPoolConfig config = new JedisPoolConfig();
        // 设置最大连接数
        config.setMaxTotal(maxTotal);
        // 设置最大空闲数量
        config.setMaxIdle(maxIdle);
        // 2.创建连接池对象
        pool = new JedisPool(config, host, port);
    }

    // 提供获取连接的方法
    public static Jedis getJedis(){
        return pool.getResource();
    }
}
