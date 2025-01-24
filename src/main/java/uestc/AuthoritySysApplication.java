package uestc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("uestc.mapper")
@SpringBootApplication
public class AuthoritySysApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthoritySysApplication.class, args);
    }
}
