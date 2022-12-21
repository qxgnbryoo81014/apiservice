package com.myapi.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * API回傳訊息
 */
public enum RespResultCode {
	
	SUCCESS("0", "成功"),
	AUTH_CHECK_ERROR("1", "授權檢核錯誤"),
	DATA_CHECK_ERROR("2", "資料檢核錯誤"),
	SYSTEM_ERROR("3", "系統執行錯誤");
	
	private String code;
	private String desc;
	
	private RespResultCode(String code, String desc){
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	/**
	 * 產生對應Map
	 * @return
	 */
	public static Map<String, String> initiMapping() {
		Map<String, String> map = new HashMap<String, String>();
        for (RespResultCode rc : RespResultCode.values()) {
        	map.put(rc.code, rc.desc);
        }
        
        return map;
    }
}
