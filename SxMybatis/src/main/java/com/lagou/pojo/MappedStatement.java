package com.lagou.pojo;

/**
 * 封装配置文件XXXMapped.xml数据
 */
public class MappedStatement {
    private String id;//id标识

    private String resultType;//返回值类型

    private String parameterType;//参数值类型

    private String sql;//sql语句

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
