package com.lagou.dao;

import com.lagou.pojo.Animal;
import org.dom4j.DocumentException;

import java.beans.IntrospectionException;
import java.beans.PropertyVetoException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public interface AnimalDao {

    public List<Animal> queryAll() throws PropertyVetoException, DocumentException, IllegalAccessException, IntrospectionException, InstantiationException, NoSuchFieldException, SQLException, InvocationTargetException, ClassNotFoundException;

    public Animal queryOneByIdAndName(Animal animal) throws PropertyVetoException, DocumentException, IllegalAccessException, IntrospectionException, InstantiationException, NoSuchFieldException, SQLException, InvocationTargetException, ClassNotFoundException;


    public int insertAnimal(Animal animal);

    public int deleteAnimal(Animal animal);

    int updateAnimal(Animal animal);
}
