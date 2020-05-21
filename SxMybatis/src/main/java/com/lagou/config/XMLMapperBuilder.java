package com.lagou.config;

import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

/**
 * 使用dom4j对配置文件进行解析
 * 封装结果数据到实体类中
 */
public class XMLMapperBuilder {

    //维护Configuration对象，将解析出来的数据封装到Configuration对象中
    private Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        //解析配置文件前，创建bean对象，存储数据
        this.configuration = configuration;
    }

    public void parse(InputStream resourceAsStream) throws DocumentException {


        //1. 解析sqlMappedConfig.xml配置文件
        Document document = new SAXReader().read(resourceAsStream);//alt+enter  自动throws异常
        Element rootElement = document.getRootElement();//获取根节点

        //获取namespace，后续结合id,组成statementid,存储时作为key
        String namespace = rootElement.attributeValue("namespace");

        List<Element> list = rootElement.selectNodes("//select");//获取所有的properties节点

        List<Element> list2 = rootElement.selectNodes("//update");//获取所有的properties节点

        List<Element> list3 = rootElement.selectNodes("//insert");//获取所有的properties节点

        List<Element> list4 = rootElement.selectNodes("//delete");//获取所有的properties节点

       list.addAll(list2);
       list.addAll(list3);
       list.addAll(list4);

        //遍历所有mappedStatement，封装结果数据
        for (Element element : list) {
            //解析
            String id = element.attributeValue("id");
            String resultType = element.attributeValue("resultType");
            String parameterType = element.attributeValue("paramterType");
            String sqlText = element.getTextTrim();

            //封装到pojo对象中
            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setId(id);
            mappedStatement.setParameterType(parameterType);
            mappedStatement.setResultType(resultType);
            mappedStatement.setSql(sqlText);

            //存储到configuration对象中
            System.out.println(namespace+"/"+mappedStatement);
            configuration.getMappedStatementMap().put(namespace+"."+id,mappedStatement);
        }

    }
}
