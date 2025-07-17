package com.andjelko.moneytransferservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class MoneyTransferServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoneyTransferServiceApplication.class, args);
    }

}
