package com.lagou.test;

import com.lagou.dao.AnimalDao;
import com.lagou.io.Resources;
import com.lagou.pojo.Animal;
import com.lagou.sqlSession.SqlSession;
import com.lagou.sqlSession.SqlSessionFactory;
import com.lagou.sqlSession.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;

import java.beans.IntrospectionException;
import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class Test {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, InvocationTargetException, IntrospectionException, SQLException, PropertyVetoException, NoSuchFieldException, ClassNotFoundException, DocumentException {
        InputStream resourceAsStream = Resources.getResourceAsStream("SqlMapConfig.xml");
        System.out.println("is:"+resourceAsStream);
        //3.获取会话工厂对象sqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);

        //获取会话对象
        SqlSession session = sqlSessionFactory.openSession();
        AnimalDao dao = session.getMapper(AnimalDao.class);
        Animal animal = new Animal();
        animal.setId(2);
        animal.setName("zhangan2");
        Animal animal1 = dao.queryOneByIdAndName(animal);
        System.out.println(animal1);
        System.out.println("over");
        System.out.println("----------------------------------------------");

        animal.setId(3);
        /*animal.setName("lisi");
        int i = dao.insertAnimal(animal);
        System.out.println("添加结果："+i);*/
        /*int i = dao.deleteAnimal(animal);
        System.out.println("删除结果："+i);*/

        animal.setId(1);
        animal.setName("lisi666");
        int i = dao.updateAnimal(animal);
        System.out.println("添加结果："+i);
    }
}
