package com.example.ChefServer.database;

import org.springframework.boot.jdbc.*;
import org.springframework.context.annotation.*;
import org.springframework.beans.factory.annotation.Value;


import javax.sql.*;

@Configuration
public class H2DataSource {

    @Value("${spring.datasource.url}")
    private String database_url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();

        dataSourceBuilder.url(database_url);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);
        
        return dataSourceBuilder.build();
    }
}
