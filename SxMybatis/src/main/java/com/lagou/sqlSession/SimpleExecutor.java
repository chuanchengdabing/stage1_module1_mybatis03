package com.lagou.sqlSession;

import com.lagou.config.BoundSql;
import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;
import com.lagou.utils.GenericTokenParser;
import com.lagou.utils.ParameterMapping;
import com.lagou.utils.ParameterMappingTokenHandler;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleExecutor implements Executor {

    //真正实现sql语句的方法
    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, IntrospectionException, InvocationTargetException, InstantiationException {

        //1.注册数据库驱动
        Connection connection = configuration.getDataSource().getConnection();

        //2.获取sql
        /**
         * 1. 执行的sql是：？占位符
         *    现有的sql是:#{value}占位符，需要转换
         *
         *
         */
        //获取现有的sql语句
        String sql = mappedStatement.getSql();

        //编写转换函数,转换sql语句中的占位符，和占位符中的value值
        BoundSql boundSql = getBoundSql(sql);

        System.out.println("sql:"+ boundSql.getSqlText());


        //3.获取预处理对象
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());
        //4.设置参数(反射)
        String paramterType = mappedStatement.getParameterType();
        Class<?> paramtertypeClass = getClassType(paramterType);

        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();


            //反射
            Field declaredField = paramtertypeClass.getDeclaredField(content);
            //暴力访问
            declaredField.setAccessible(true);
            Object o = declaredField.get(params[0]);

            preparedStatement.setObject(i+1,o);
            System.out.println("补全参数："+content+"/"+o);

        }
        //5.执行sql
        ResultSet resultSet = preparedStatement.executeQuery();


        String resultType = mappedStatement.getResultType();
        Class<?> resultTypeClass = getClassType(resultType);

        ArrayList<Object> objects = new ArrayList<>();
        //6.封装结果数据（反射）

        while (resultSet.next()){
            Object o =resultTypeClass.newInstance();
            //元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {

                // 字段名
                String columnName = metaData.getColumnName(i);
                // 字段的值
                Object value = resultSet.getObject(columnName);

                //使用反射或者内省，根据数据库表和实体的对应关系，完成封装
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o,value);


            }
            objects.add(o);

        }
        return (List<E>) objects;
    }

    @Override
    public int update(Configuration configuration, MappedStatement mappedStatement, Object[] params) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException {

        //1.注册数据库驱动
        Connection connection = configuration.getDataSource().getConnection();

        //2.获取sql
        /**
         * 1. 执行的sql是：？占位符
         *    现有的sql是:#{value}占位符，需要转换
         *
         *
         */
        //获取现有的sql语句
        String sql = mappedStatement.getSql();

        //编写转换函数,转换sql语句中的占位符，和占位符中的value值
        BoundSql boundSql = getBoundSql(sql);

        System.out.println("sql:"+ boundSql.getSqlText());


        //3.获取预处理对象
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());
        //4.设置参数(反射)
        String paramterType = mappedStatement.getParameterType();
        Class<?> paramtertypeClass = getClassType(paramterType);

        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();


            //反射
            Field declaredField = paramtertypeClass.getDeclaredField(content);
            //暴力访问
            declaredField.setAccessible(true);
            Object o = declaredField.get(params[0]);

            preparedStatement.setObject(i+1,o);
            System.out.println("补全参数："+content+"/"+o);

        }
        //5.执行sql
        int  updateResult = preparedStatement.executeUpdate();

        return updateResult;
    }




    private Class<?> getClassType(String paramterType) throws ClassNotFoundException {
        if(paramterType!=null){
            Class<?> aClass = Class.forName(paramterType);
            return aClass;
        }
        return null;

    }

    /**
     * sql转换函数，转换占位符
     * @param sql
     * @return
     */
    private BoundSql getBoundSql(String sql) {
        //标记处理类：配置标记解析器来完成对占位符的解析处理工作
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        //解析出来的sql
        String parseSql = genericTokenParser.parse(sql);
        //将占位符#{value}中的value解析出来，封装到bean对象中
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        BoundSql boundSql  = new BoundSql(parseSql,parameterMappings);

        return boundSql;
    }
}
