package com.firefly.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class SASUtil {
	
	public static RestTemplate restTemepalte = new RestTemplate();
	/**
	 * SAS数据,用来读取文件
	 */
	public static String getSASforRead(String fileName){
		String[] fileNameArray = fileName.split(";");
		StringBuffer buffer = new StringBuffer();
		for(int i=0;i<fileNameArray.length;i++){
			
			String url = "http://139.219.8.252:33604/sas/blob/ticket/"+fileNameArray[i];
			
			HttpHeaders headers = new HttpHeaders();
			headers.add("x-fchain-sas", "ja3NmwQqVXLbbmAqt3yk4XzZ");
			
			HttpEntity<String> requestEntity = new HttpEntity<String>(null, headers);
			
			ResponseEntity<String> response = restTemepalte.exchange(url, HttpMethod.GET, requestEntity, String.class);
			
			String sttr ="https"+response.getBody().split("https")[1].split("\"}")[0];
			
//			System.out.println(sttr);
//			System.out.println(sttr.split("{\"store_url\": \"")[1].split("\"}")[0]);
//			System.out.println("https"+sttr.split("https")[1].split("\"}")[0]);
			
//			urlArray.add(sttr);	
			buffer.append(sttr);
			buffer.append(";");
		}
		return buffer.toString();
	}
}
