package com.magic.dao.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 *
 * @author Cheng Yufei
 * @create 2022-02-16 3:58 PM
 **/
@Configuration
@MapperScan(basePackages = {"com.magic.dao.mp"}, sqlSessionFactoryRef = "mybatisSqlSessionFactoryBean", sqlSessionTemplateRef = "mpSqlSessionTemplate")
public class MpConfig {


    /*@Bean(name = "mp")
    public DruidDataSource firstDtaSource(@Qualifier("first") DruidDataSource dataSource) {
        DruidDataSource mp = DruidDataSourceBuilder.create().build();
        BeanUtils.copyProperties(dataSource, mp);
        return mp;
    }*/

    @Bean(name = "mybatisSqlSessionFactoryBean")
    public SqlSessionFactory mybatisSqlSessionFactoryBean(@Qualifier("first") DruidDataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        factoryBean.setMapperLocations(resolver.getResources("classpath*:mp/*.xml"));
        factoryBean.setTypeAliasesPackage("com.magic.dao.model");
        return factoryBean.getObject();
    }

    //@Bean(name = "globalConfig")
    public GlobalConfig globalConfig(@Qualifier("mybatisSqlSessionFactoryBean") SqlSessionFactory sqlSessionFactory) {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setSqlSessionFactory(sqlSessionFactory);
        return globalConfig;
    }

    @Bean(name = "mpSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("mybatisSqlSessionFactoryBean") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
