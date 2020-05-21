package com.lagou.sqlSession;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public interface SqlSession {

    //查询一个
    public <T> T selectOne(String statementid,Object...params) throws SQLException, IllegalAccessException, IntrospectionException, InstantiationException, ClassNotFoundException, InvocationTargetException, NoSuchFieldException;


    //查询所有
    public <E> List<E> selectList(String statementid, Object...params) throws SQLException, IllegalAccessException, IntrospectionException, InstantiationException, NoSuchFieldException, InvocationTargetException, ClassNotFoundException;

    int insert(String statementid,Object ...params) throws ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException;

    int update(String statementid,Object ...params) throws ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException;

    int delete(String statementid,Object ...params) throws ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException;

    //获取动态代理
    public <T> T getMapper(Class<?> cls);

}
