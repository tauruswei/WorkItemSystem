package com.firefly.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


import org.springframework.stereotype.Service;
import org.stellar.sdk.KeyPair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.firefly.pojo.Workitem;
import com.firefly.service.WorkItemService;

@Service
public class WorkItemUtil {

	private WorkItemService workItemService;

	public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public List<Workitem> closeWorkItem(List<Workitem> workItemList1) {
		List<Workitem> workItemList2 = new ArrayList();
		for (int i = 0; i < workItemList1.size(); i++) {
			if (getDiffDate(workItemList1.get(i).getUpdatedtime(), Calendar.DAY_OF_MONTH) < 1) {
				workItemList2.add(workItemList1.get(i));
			} else {
				Workitem workItem = workItemList1.get(i);
				workItem.setStatus("closed");
				workItem.setPerformer("SystemAuto");
				String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				workItem.setUpdatedtime(time);
				workItemService.updateWorkItem(workItem);
			}
		}
		return workItemList2;
	}

	/**
	 * 时间相减
	 * 
	 * @param strDateBegin
	 * @param strDateEnd
	 * @param iType
	 * @return
	 */
	public static int getDiffDate(String strDateBegin, int iType) {
		Calendar calBegin = Calendar.getInstance();
		calBegin.setTime(parseDate(strDateBegin, DATETIME_FORMAT));
		long lBegin = calBegin.getTimeInMillis();
		long lEnd = System.currentTimeMillis();
		if (iType == Calendar.SECOND)
			return (int) ((lEnd - lBegin) / 1000L);
		if (iType == Calendar.MINUTE)
			return (int) ((lEnd - lBegin) / 60000L);
		if (iType == Calendar.HOUR)
			return (int) ((lEnd - lBegin) / 3600000L);
		if (iType == Calendar.DAY_OF_MONTH) {
			return (int) ((lEnd - lBegin) / 86400000L);
		}
		return -1;
	}

	// -----------------格式化字符串为日期--------------------------------------
	/**
	 * 格式化字符串为日期
	 *
	 * @param date
	 * @param format
	 * @return
	 */
	public static Date parseDate(String date, String format) {
		try {
			return new SimpleDateFormat(format).parse(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 返回一个文件的全名，包括后缀，例如：a.txt
	public static String getFileFullName(String path, String fileName) {

		File file = new File(path);
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			String name = files[i].getName();
			if (name.startsWith(fileName)) {
				return name;
			}
		}
		return fileName;

	}

	// byte[] to hex string
	public static String toHexString(byte[] byteArray) {
		final StringBuilder hexString = new StringBuilder("");
		if (byteArray == null || byteArray.length <= 0)
			return null;
		for (int i = 0; i < byteArray.length; i++) {
			int v = byteArray[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				hexString.append(0);
			}
			hexString.append(hv);
		}
		return hexString.toString();
	}

	// 将16进制字符串转换为byte[]
	public static byte[] hexStringToByte(String hex) {
		   int len = (hex.length() / 2);
		   byte[] result = new byte[len];
		   char[] achar = hex.toCharArray();
		   for (int i = 0; i < len; i++) {
		    int pos = i * 2;
		    result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		   }
		   return result;
	}
	 private static int toByte(char c) {
		    byte b = (byte) "0123456789ABCDEF".indexOf(c);
		    return b;
	}

	public static Boolean verify(String userName, Long time, HttpServletRequest request) {
		// 获取KeyPair
		KeyPair keyPair = KeyPair.fromAccountId(userName);

		// 用来签名的数据
		JSONObject map = new JSONObject();
		map.put("userName", userName);
		map.put("time", time);
		String content = JSON.toJSONString(map);
		
		byte[] contentByte = content.getBytes();

		// json中的签名字符串
		String signature = ((HttpServletRequest) request).getHeader("x-auth");
		
//		验证签名decoder.decode
		Base64.Decoder decoder = Base64.getDecoder();
		boolean result = keyPair.verify(contentByte, decoder.decode(signature));
	
		long lEnd = System.currentTimeMillis();
		long time1 = lEnd - time;
		
		if (result&& (time1/60000L) < 5) {
//		if (result) {
			return true;
		} else {
			return false;
		}
//		return true;
	}
	public static String time(){
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);//date 换成已经已知的Date对象
		cal.add(Calendar.HOUR_OF_DAY, +8);// before 8 hour
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(cal.getTime());
	}
}
