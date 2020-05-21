package com.lagou.config;

import com.lagou.io.Resources;
import com.lagou.pojo.Configuration;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * 使用dom4j对配置文件进行解析
 * 封装结果数据到实体类中
 */
public class XMLConfigBuilder {
    //维护Configuration对象
    private Configuration configuration;



    public XMLConfigBuilder() {
        //解析配置文件前，创建bean对象，存储数据
        this.configuration = new Configuration();
    }

    /**
     * 解析配置文件，封装到实体对象中
     * @param in
     * @return
     */
    public Configuration parseConfig(InputStream in) throws DocumentException, PropertyVetoException {
        //1. 解析sqlMappedConfig.xml配置文件
        Document document = new SAXReader().read(in);//alt+enter  自动throws异常
        Element rootElement = document.getRootElement();//获取根节点
        List<Element> list = rootElement.selectNodes("//property");//获取所有的properties节点

        Properties properties = new Properties();//存储封装结果数据
        for (Element element: list) {
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            System.out.println(name+"..........:"+value);
            properties.setProperty(name,value);
        }
        //创建连接池对象，将配置文件中解析出来的数据
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setDriverClass(properties.getProperty("driverClass"));
        comboPooledDataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
        comboPooledDataSource.setUser(properties.getProperty("username"));
        comboPooledDataSource.setPassword(properties.getProperty("password"));
        //设置(将解析配置文件解析出来的数据，创建出连接池对象，封装到pojo对象上Configuration)

        configuration.setDataSource(comboPooledDataSource);

        //2. 解析mapped.xml配置文件
        //从sqlMappedConfig.xml配置文件中获取mapper.xml配置文件的路径
        List<Element> mapperList = rootElement.selectNodes("//mapper");
        //解析mapper.xml映射文件
        for (Element element : mapperList) {
            //获取属性resource值，sql映射文件的路径
            String mapperPath = element.attributeValue("resource");
            //使用Resource工具类，加载配置文件到内存
            InputStream resourceAsStream = Resources.getResourceAsStream(mapperPath);
            //使用dom4j解析，传递configuration的目的是封装结果数据
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(configuration);

            //解析mapper配置文件，封装到MappedStatemet对象，存储到Configuration对象中
            xmlMapperBuilder.parse(resourceAsStream);
        }

        return configuration;
    }
}
