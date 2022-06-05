package com.wu.douban.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.wu.douban.mapper")
public class ComConfig {

/**

 * SQL 执行性能分析插件

 * 开发环境使用，线上不推荐。 maxTime 指的是 sql 最大执行时长

 */
//
//    @Bean
//
//
//    public PerformanceInterceptor performanceInterceptor() {
//
//        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
//
//       // performanceInterceptor.setMaxTime(5000);//ms，超过此处设置的ms则sql不执行
//
//        performanceInterceptor.setFormat(true);
//
//        return performanceInterceptor;
//
//    }

    /**
     2
     * 逻辑删除插件
     3
     */

//    @Bean
//
//    public ISqlInjector sqlInjector() {
//
//        return new LogicSqlInjector();
//
//    }


/**

 * 分页插件

 */

    @Bean

    public PaginationInterceptor paginationInterceptor() {

        return new PaginationInterceptor();

    }

}
