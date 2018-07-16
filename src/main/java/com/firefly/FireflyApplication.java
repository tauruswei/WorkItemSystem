package com.firefly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
// 扫描 mybatis mapper 包路径
@MapperScan(basePackages = "com.firefly.mapper")
// 扫描 所有需要的包, 包含一些自用的工具类包 所在的路径
@ComponentScan(basePackages = { "com.firefly", "org.n3r.idworker" })
//// 开启定时任务
//@EnableScheduling
//// 开启异步调用方法
//@EnableAsync
////扫描 包下面所有的servlet
//@ServletComponentScan(basePackages = "com.firefly.servlet")

// 开启CAS支持
// @EnableCasClient
public class FireflyApplication {

	public static void main(String[] args) {
		SpringApplication.run(FireflyApplication.class, args);
	}
	
}
