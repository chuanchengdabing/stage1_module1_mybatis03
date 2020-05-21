package com.lagou.sqlSession;

import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;

import java.beans.IntrospectionException;
import java.lang.reflect.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DefaultSqlSession implements SqlSession {
    private Configuration configuration;


    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> T selectOne(String statementid, Object... params) throws SQLException, IllegalAccessException, IntrospectionException, InstantiationException, ClassNotFoundException, InvocationTargetException, NoSuchFieldException {
        List<Object> objects = selectList(statementid,params);
        System.out.println("size:"+objects.size());
        if(objects.size()==1){
            return (T) objects.get(0);
        }else {
            throw  new RuntimeException("查询结果为空或者查询结果过多！");
        }

    }

    //真正的查询方法，提取到Excetor类中实现
    @Override
    public <E> List<E> selectList(String statementid, Object... params) throws SQLException, IllegalAccessException, IntrospectionException, InstantiationException, NoSuchFieldException, InvocationTargetException, ClassNotFoundException {
        //将要完成的操作提取的Executor现实类SimpleExecutor中的query方法中实现
        Executor executor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementid);
        System.out.println("statementid:"+statementid);
        System.out.println("mappedStatement:"+mappedStatement);
        return executor.query(configuration,mappedStatement,params);
    }

    @Override
    public int insert(String statementid, Object... params) throws ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException {
        Executor executor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementid);
        System.out.println("statementid:"+statementid);
        System.out.println("mappedStatement:"+mappedStatement);
        return executor.update(configuration,mappedStatement,params);
    }

    @Override
    public int update(String statementid, Object... params) throws ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException {
        Executor executor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementid);
        System.out.println("statementid:"+statementid);
        System.out.println("mappedStatement:"+mappedStatement);
        return executor.update(configuration,mappedStatement,params);
    }
    @Override
    public int delete(String statementid, Object... params) throws ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException {
        Executor executor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementid);
        System.out.println("statementid:"+statementid);
        System.out.println("mappedStatement:"+mappedStatement);
        return executor.update(configuration,mappedStatement,params);
    }

    /**
     * 使用jdk动态代理，生成代理对象，并返回
     * @param cls
     * @param <T>
     * @return
     */
    public <T> T getMapper(Class<?> cls){
        //使用jdk动态代理
       Object proxyInstance =  Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{cls}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //调用上面的selectList/selectOne方法
                /**
                 * 问题：如何获取参数statementid
                 *      在此方法中，如何直接获取statementid(原来的直接指定，存在硬编码)
                 * 解决：反射
                 *      修改statementid和接口的方法一致
                 */
                String methodName = method.getName();
                String className = method.getDeclaringClass().getName();
                String statementid = className + "." + methodName;

                //判断方法的返回类型
                Type genericReturnType = method.getGenericReturnType();

                System.out.println("判断返回类型："+genericReturnType.getTypeName());
                System.out.println("方法名称："+methodName);
                if(methodName.contains("select")||methodName.contains("query")){
                    if(genericReturnType instanceof ParameterizedType){
                        return selectList(statementid,args);
                    }
                    return selectOne(statementid,args);
                }else{
                    return update(statementid,args);
                }
            }
        });
       return (T) proxyInstance;
    }

}
