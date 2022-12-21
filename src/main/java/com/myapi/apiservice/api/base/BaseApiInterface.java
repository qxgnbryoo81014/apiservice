package com.myapi.apiservice.api.base;

public interface BaseApiInterface {

	// 處理API內容
	String runProcess(String className, String reqJson) throws Exception;

}
