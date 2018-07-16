package com.firefly.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Java原生的API可用于发送HTTP请求，即java.net.URL、java.net.URLConnection，这些API很好用、很常用，
 * 但不够简便；
 * 
 * 1.通过统一资源定位器（java.net.URL）获取连接器（java.net.URLConnection） 2.设置请求的参数 3.发送请求
 * 4.以输入流的形式获取返回内容 5.关闭输入流
 * 
 * @author H__D
 *
 */
public class HttpConnectionUtil {


    /**
     * 多文件上传的方法
     * 
     * @param actionUrl：上传的路径
     * @param uploadFilePaths：需要上传的文件路径，数组
     * @return
     */
    @SuppressWarnings("finally")
    public static String uploadFile(String actionUrl, String[] uploadFilePaths) {
    	
        DataOutputStream ds = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;

        try {
            // 统一资源
            URL url = new URL(actionUrl);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            // http的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;

            // 设置是否从httpUrlConnection读入，默认情况下是true;
            httpURLConnection.setDoInput(true);
            // 设置是否向httpUrlConnection输出
            httpURLConnection.setDoOutput(true);
            // Post 请求不能使用缓存
            httpURLConnection.setUseCaches(false);
            // 设定请求的方法，默认是GET
            httpURLConnection.setRequestMethod("PUT");
            // 设置字符编码连接参数
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            
            httpURLConnection.setRequestProperty("x-ms-blob-type", "BlockBlob");
            // 设置请求内容类型
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data");

            // 设置DataOutputStream
            ds = new DataOutputStream(httpURLConnection.getOutputStream());
            for (int i = 0; i < uploadFilePaths.length; i++) {
                String uploadFile = uploadFilePaths[i];
                String filename = uploadFile.substring(uploadFile.lastIndexOf("/") + 1);
                
                FileInputStream fStream = new FileInputStream(uploadFile);
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                int length = -1;
                while ((length = fStream.read(buffer)) != -1) {
                    ds.write(buffer, 0, length);
                }
                /* close streams */
                fStream.close();
            }
            ds.flush();
            ds.close();
//            int code = httpURLConnection.getResponseCode();
			String str = httpURLConnection.getResponseMessage();
			System.out.println(str);
			
            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception(
                        "HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }

//            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                inputStream = httpURLConnection.getInputStream();
//                inputStreamReader = new InputStreamReader(inputStream);
//                reader = new BufferedReader(inputStreamReader);
//                tempLine = null;
//                resultBuffer = new StringBuffer();
//                while ((tempLine = reader.readLine()) != null) {
//                    resultBuffer.append(tempLine);
//                    resultBuffer.append("\n");
//                }
//            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (ds != null) {
                try {
                    ds.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            return resultBuffer.toString();
        }
    }


    public static void main(String[] args) {

        // 上传文件测试
         String str = uploadFile("https://fireflystore.blob.core.chinacloudapi.cn/ticket-test/Img337302965.jpg?2018-07-04T09%3A38%3A51Z&sp=w&sv=2017-07-29&sr=c&sig=/9x47hiwe/CDCuOcMgTQmMiDGYDvZhm2eL6zkKpUGjw%3D",new String[] { "C:/Users/Administrator/Desktop/Img337302965.jpg"});
         System.out.println(str);

    }

}
