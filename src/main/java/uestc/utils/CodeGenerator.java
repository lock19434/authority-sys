package uestc.utils;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.sql.Types;
import java.util.Collections;

public class CodeGenerator {

    private static final String url = "jdbc:mysql://127.0.0.1:3306/db_authority?serverTimezone=Asia/Shanghai";
    private static final String username = "root";
    private static final String password = "88888888";
    private static final String author = "liulong";
    private static final String projectPath = System.getProperty("user.dir"); //获取当前项目路径

    public static void main(String[] args) {
        FastAutoGenerator.create(url, username, password)
                // 全局配置
                .globalConfig(builder -> {builder
                        // .enableSwagger() // 是否启用swagger注解
                        .author(author) // 作者名称
                        .dateType(DateType.ONLY_DATE) // 时间策略
                        .commentDate("yyyy-MM-dd") // 注释日期
                        .outputDir(projectPath + "/src/main/java") // 输出目录
                        .disableOpenDir(); // 生成后禁止打开所生成的系统目录
                })
                // java和数据库字段的类型转换
                .dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                    int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                    if (typeCode == Types.SMALLINT || typeCode == Types.TINYINT) {
                        // 自定义类型转换
                        return DbColumnType.INTEGER;
                    }
                    return typeRegistry.getColumnType(metaInfo);

                }))

                // 包配置
                .packageConfig(builder -> {builder
                        .parent("uestc") // 父包名
                        .controller("controller")
                        .entity("entity") // 实体类包名
                        .service("service") // service包名
                        .serviceImpl("service.impl") // serviceImpl包名
                        .mapper("mapper") // mapper包名
                        .xml("mapper.xml")
                        .pathInfo(Collections.singletonMap(OutputFile.xml, projectPath + "/src/main/resources/uestc/mapper")).build();
                })

                // 策略配置
                .strategyConfig(builder -> { builder
                        .addInclude("sys_user","sys_role","sys_department","sys_permission")
                        .enableCapitalMode() //驼峰
                        .addTablePrefix("sys_") // 增加过滤表前缀
                        // Entity 策略配置
                        .entityBuilder()
                        .enableLombok() // 开启lombok
                        .enableRemoveIsPrefix() // 开启boolean类型字段移除is前缀
                        .enableTableFieldAnnotation() //开启生成实体时生成的字段注解
                        .naming(NamingStrategy.underline_to_camel) // 表名 下划线 -》 驼峰命名
                        .columnNaming(NamingStrategy.underline_to_camel) // 字段名 下划线 -》 驼峰命名
                        // .idType(IdType.ASSIGN_ID) // 主键生成策略 雪花算法生成id

                        // Controller 策略配置
                        .controllerBuilder()
                        .enableRestStyle() // 开启@RestController

                        // Service 策略配置
                        .serviceBuilder()

                        // Mapper 策略配置
                        .mapperBuilder()
                        .build();
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute(); // 执行
        System.out.println("generator success!");
    }
}
