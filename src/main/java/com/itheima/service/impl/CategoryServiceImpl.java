package com.itheima.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.dao.CategoryDao;
import com.itheima.model.Category;
import com.itheima.service.CategoryService;
import com.itheima.util.BeanFactoryUtils;
import com.itheima.util.JedisUtils;
import redis.clients.jedis.Jedis;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao categorDao = (CategoryDao) BeanFactoryUtils.getBean("CategoryDao");
    /**
     * 调用service获取导航的信息
     *
     * @return
     */

    public String findAll() throws Exception {

        //在这我们进行,对数据库的优化,我们不能每次都访问数据库,我们应该把数据放在redis数据库中作为缓存
        //1.创建jedis连接池
        Jedis jedis = JedisUtils.getJedis();
        //2.从jedis中获取数据
        String cateJson = jedis.get("category");
        if (cateJson == null) {
            //如果没有查到在缓存中没有拿到数据,我们则进行查询数据库
            //调用数据库
            List<Category> listCategory = categorDao.findAll();
            //System.out.println(listCategory);
            //在这我们封装成json字符串
            ObjectMapper mapper = new ObjectMapper();
            cateJson = mapper.writeValueAsString(listCategory);
            //最后我们在把查到的数据放到redis缓冲中,便于后面直接从缓冲中拿
            jedis.set("category",cateJson);
            //最后关闭连接
            jedis.close();
        }

        return cateJson;
    }
}
