package com.lagou.sqlSession;


public interface SqlSessionFactory {

    //获取会话对象
    public SqlSession openSession();
}
