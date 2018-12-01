package com.itheima.util;

import java.util.ResourceBundle;

public class BeanFactoryUtils {
    private static ResourceBundle resourceBundle;
    static{
        resourceBundle = ResourceBundle.getBundle("beans");
    }
    public static Object getBean(String id){
        try {
            String beanUrl = resourceBundle.getString(id);
            return Class.forName(beanUrl).getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Object bean = BeanFactoryUtils.getBean("userService");
        System.out.println(bean);
    }
}
