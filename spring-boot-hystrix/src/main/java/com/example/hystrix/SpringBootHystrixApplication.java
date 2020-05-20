package com.example.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@EnableHystrix
@EnableHystrixDashboard
@SpringBootApplication
public class SpringBootHystrixApplication {

    /**
     * hystrix的地址 : http://localhost:8080/hystrix
     * HystrixDashboard的地址：http://localhost:8080/actuator/hystrix.stream
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringBootHystrixApplication.class, args);
    }

}
