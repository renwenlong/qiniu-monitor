package com.test.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by renwenlong on 16/6/17.
 */
@SpringBootApplication
@EnableScheduling
@ComponentScan
public class Main {

    public static void main(String[] args) throws Exception {

        SpringApplication.run(Main.class, args);


    }
}
