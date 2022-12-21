package com.myapi.apiservice.api.base;

import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.myapi.common.enums.RespResultCode;
import com.myapi.common.utility.DateUtil;
import com.myapi.common.utility.StringUtil;

import net.sf.json.JSONObject;

@Component
public class BaseApiUtil {
	
	public String checkReqCommonField(JSONObject json) {
		StringBuilder validEventList = new StringBuilder();
		// 防呆 get null
		String compId = StringUtil.notNull(json.get("companyID"));
		String createDateTime = StringUtil.notNull(json.get("createDateTime"));
		String requestData = StringUtil.notNull(json.get("reqData"));

		checkQueryField(compId, "companyID", true, 10, true, null, validEventList);
		checkQueryField(createDateTime, "createDateTime", true, 14, true, null, validEventList);
		checkQueryField(requestData, "reqData", true, 256, false, null, validEventList);

		// error output
		if (validEventList.length() > 0) {
			return validEventList.toString();
		}

		Pattern p = Pattern.compile("^((19|2[0-9])[0-9]{2})(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])(0[0-9]|1[0-9]|2[0-3])[0-5][0-9][0-5][0-9]$");
		if (!p.matcher(createDateTime).find()) {
			return "[createDateTime]格式需為yyyyMMddHHmmss";
		}
		
		String limitMinuteFrom = DateUtil.addMinuteForSystemDate(-10, "yyyyMMddHHmmss");
		String limitMinuteTo = DateUtil.addMinuteForSystemDate(+10, "yyyyMMddHHmmss");
		if (Double.parseDouble(limitMinuteFrom) > Double.parseDouble(createDateTime)
				|| Double.parseDouble(createDateTime) > Double.parseDouble(limitMinuteTo)) {
			return "Request 時間已逾時，無法處理";
		}
		return null;
	}
	
	/**
	 * 檢查String欄位
	 * 1. 必填欄位檢查 (isRequird true)
	 * 2. 長度檢查 (isLength true)
	 * 3. 樣式檢查 (pattern not null)
	 * @param value 	欄位值
	 * @param invName 	欄位名稱
	 * @param isRequired 是否必填
	 * @param length	文字長度
	 * @param isLength	檢查長度是否超過設定
	 * @param pattern	檢查是否符合樣式
	 * @param validEventList 回傳錯誤訊息
	 */
	public void checkQueryField(String value, String columnName, Boolean isRequired, int length, Boolean isLength, String pattern, StringBuilder validEventList) {
		
		// 檢查必填欄位 (isRequird=true才會檢查)
		if (isRequired && StringUtils.isBlank(value)) {
			validEventList.append("[" + columnName + "]必填欄位錯誤;");
		}
		
		// 檢查最大長度,資料長度
		if (isLength && null != value && value.length() > length) {
			validEventList.append("[" + columnName + "]資料長度錯誤;");
		}
		
		// 樣式比對(pattern)
		if (null != value && !"".equals(value) && null != pattern && !Pattern.matches(pattern, value)) {
			validEventList.append("[" + columnName + "]資料格式錯誤;");
		}
	}
	
	public JSONObject defaultJsonObject() {
		JSONObject responseJson = new JSONObject();
		responseJson.put("statusCode", RespResultCode.SYSTEM_ERROR.getCode());
		responseJson.put("statusDesc", RespResultCode.SYSTEM_ERROR.getDesc());
		responseJson.put("respData", "");
		return responseJson;
	}
}
