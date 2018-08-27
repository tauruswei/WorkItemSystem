package com.firefly.controller;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/redis")
public class RedisController {
	@Autowired
	private RedisTemplate<String,String> strRedis;
	
	@RequestMapping(value="/test")
	public String test(){
//		strRedis.opsForValue().set("imooc-cache", "hello  world");
		strRedis.opsForValue().set("imooc-cache", "hello  stellar",5, TimeUnit.SECONDS);
		return strRedis.opsForValue().get("imooc-cache");
	}
}
