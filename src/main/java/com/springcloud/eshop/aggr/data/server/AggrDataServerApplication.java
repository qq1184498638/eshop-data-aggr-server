package com.springcloud.eshop.aggr.data.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AggrDataServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AggrDataServerApplication.class, args);
	}

}

