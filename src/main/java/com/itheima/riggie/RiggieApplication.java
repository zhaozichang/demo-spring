package com.itheima.riggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Slf4j
@SpringBootApplication
@EnableWebMvc
@ServletComponentScan
@EnableTransactionManagement
public class RiggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(RiggieApplication.class,args);
        log.info("项目启动成功.....");
    }
}
