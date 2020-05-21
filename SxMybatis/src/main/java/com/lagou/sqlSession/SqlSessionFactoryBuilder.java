package com.lagou.sqlSession;

import com.lagou.config.XMLConfigBuilder;
import com.lagou.pojo.Configuration;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

/**
 * 解析配置文件
 */
public class SqlSessionFactoryBuilder {

    /**
     * 获取SqlSessionFactory对象
     *
     *
     * @param in 流对象
     * @return  sqlSessionFactory对象
     */
    public SqlSessionFactory build(InputStream in) throws DocumentException, PropertyVetoException {
        //1.使用dom4j解析配置文件，封装到Configuration对象中
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        //解析两个配置文件，封装到pojo实体对象中
        Configuration configuration= xmlConfigBuilder.parseConfig(in);

        //2.创建sqlSessionFactory对象
        DefaultSqlSessionFactory defaultSqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        return defaultSqlSessionFactory;
    }

}
