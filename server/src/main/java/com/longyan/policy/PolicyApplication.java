package com.longyan.policy;

import com.longyan.policy.util.BatchUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.swing.*;

@EnableScheduling
@EnableAsync
@SpringBootApplication
public class PolicyApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(PolicyApplication.class);
        BatchUtil.configurableApplicationContext = springApplication.run(args);
    }

}
