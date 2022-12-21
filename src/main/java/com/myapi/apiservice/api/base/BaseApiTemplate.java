package com.myapi.apiservice.api.base;

import net.sf.json.JSONObject;

public abstract class BaseApiTemplate extends BaseApiUtil {

	public final Boolean M = true;	// 必填欄位(不可空白)
	public final Boolean O = false;	// 非必填欄位
	
	//====================
	//	Variable
	//====================
	protected String compId = null;
	protected JSONObject requestJson = null;
	protected JSONObject resultLists = null;
	protected StringBuilder validEventList = null;
	
	//====================
	//	get & set
	//====================
	public void setCompId(String compId) {
		this.compId = compId;
	}
	
	public JSONObject getRequestJson() {
		return requestJson;
	}

	public void setRequestJson(JSONObject requestJson) {
		this.requestJson = requestJson;
	}

	public JSONObject getResultLists() {
		return resultLists;
	}

	public void setResultLists(JSONObject resultLists) {
		this.resultLists = resultLists;
	}

	public StringBuilder getValidEventList() {
		return validEventList;
	}
	
	public abstract void doProcess() throws Exception;

	public abstract void checkData() throws Exception;
	
}
