package com.lagou.pojo;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 封装配置文件sqlMapConfig.xml数据
 */
public class Configuration {
    private DataSource dataSource;

    Map<String,MappedStatement> mappedStatementMap = new HashMap<>();

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, MappedStatement> getMappedStatementMap() {
        return mappedStatementMap;
    }

    public void setMappedStatementMap(Map<String, MappedStatement> mappedStatementMap) {
        this.mappedStatementMap = mappedStatementMap;
    }
}
