package com.firefly.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "web")
public class WebConfig {

	private String httpPrefix;
	private String version;
	// 服务商登录错误次数锁定账号-0，不锁定
	private int plecount;
	// //后台登录超时时间（小时）
	private int sessionTimeOut;
	// //工单超时时间（小时）
	private int questionTimeOut;


	// 用户被投诉多少次，账户被锁定
	private int bcount;
	// 用户因被投诉锁定显示错误内容
	private String bmessage;

	public int getQuestionTimeOut() {
		return questionTimeOut;
	}
	
	public void setQuestionTimeOut(int questionTimeOut) {
		this.questionTimeOut = questionTimeOut;
	}
	public String getHttpPrefix() {
		return httpPrefix;
	}

	public void setHttpPrefix(String httpPrefix) {
		this.httpPrefix = httpPrefix;
	}

	

	public int getSessionTimeOut() {
		return sessionTimeOut;
	}

	public void setSessionTimeOut(int sessionTimeOut) {
		this.sessionTimeOut = sessionTimeOut;
	}

	// 订单自动超时时间
	private int transTimeout;

	public int getTransTimeout() {
		return transTimeout;
	}

	public void setTransTimeout(int transTimeout) {
		this.transTimeout = transTimeout;
	}

	public String getBmessage() {
		return bmessage;
	}

	public void setBmessage(String bmessage) {
		this.bmessage = bmessage;
	}

	public int getBcount() {
		return bcount;
	}

	public void setBcount(int bcount) {
		this.bcount = bcount;
	}

	public int getPlecount() {
		return plecount;
	}

	public void setPlecount(int plecount) {
		this.plecount = plecount;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
