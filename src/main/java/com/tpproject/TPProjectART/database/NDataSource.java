package com.tpproject.TPProjectART.database;

import org.springframework.boot.jdbc.*;
import org.springframework.context.annotation.*;

import javax.sql.*;

@Configuration
public class NDataSource {
    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url("jdbc:h2:file:~/data/database");
        dataSourceBuilder.username("sa");
        dataSourceBuilder.password("");
        return dataSourceBuilder.build();
    }
}
