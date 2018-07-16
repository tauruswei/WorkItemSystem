package com.firefly.utils;

import com.firefly.pojo.Result;

public class ResultUtil {
	 
    //当正确时返回的值
    public static Result success(String msg,Object data){
        Result result = new Result();
        result.setCode("success");
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
 
//    public static Result success(){
//        return success(null);
//    }
 
    //当错误时返回的值
    public static Result error(String msg){
        Result result = new Result();
        result.setCode("fail");
        result.setMsg(msg);
        return result;
    }
}