package com.myapi.common.utility;

import java.util.UUID;

public class StringUtil {
	
	/**
     * 產生UUID(大寫)
     * @return
     */
    public static String uuid(){
    	return UUID.randomUUID().toString().toUpperCase();
    }
	
	/**
     * 判斷是否為NULL
     * @param value
     * @return 若null或字串的null,則回傳空字串
     */
	public static String notNull(Object value){
        if(value == null || "null".equals(value)){
            return "";
        }
        return value.toString().trim();
    }
	
	/**
     * 非 Null 字串與空字串
     * @param value
     * @return
     */
    public static boolean isNotNullEmpty(String value){
    	if(value ==null || "".equals(value)  || "null".equals(value) ){
    		return false;
    	}else{
    		return true;
    	}
    }
}
