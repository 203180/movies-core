package com.example.moviescore.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean(name = "datasource1")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource1(){
        return DataSourceBuilder.create().build();
    }
    @Bean(name = "datasource2")
    @ConfigurationProperties(prefix = "mssql.datasource")
    public DataSource dataSource2(){
        return DataSourceBuilder.create().build();
    }

}
