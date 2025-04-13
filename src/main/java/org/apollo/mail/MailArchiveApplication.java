package org.apollo.mail;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableRetry
@EnableAsync
@EnableScheduling
@SpringBootApplication
@MapperScan("org.apollo.mail.mapper")
public class MailArchiveApplication {
    public static void main(String[] args) {
        SpringApplication.run(MailArchiveApplication.class, args);
    }
}