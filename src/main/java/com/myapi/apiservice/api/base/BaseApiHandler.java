package com.myapi.apiservice.api.base;

import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import com.myapi.apiservice.entity.myconna.ApiLog;
import com.myapi.apiservice.service.ApiLogService;
import com.myapi.common.enums.RespResultCode;
import com.myapi.common.utility.StringUtil;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

@Slf4j
@Component
public abstract class BaseApiHandler extends BaseApiTemplate implements BaseApiInterface{
	
	@Resource
	ApiLogService apiLogService;
	
	@Override
	public String runProcess(String className, String reqJson) throws Exception {
		log.info("{} 開始執行", className);
		ApiLog apiLog = null;
		JSONObject responseJson = defaultJsonObject();
		try {
			// 1. 寫入 wsOplog 紀錄
			JSONObject reqJsonObj = JSONObject.fromObject(reqJson);
			apiLog = apiLogService.setReqlog(className, reqJson);
			apiLogService.insertLog(apiLog);
			
			// 檢核共同欄位
			String authErrMsg = checkReqCommonField(reqJsonObj);
			if(StringUtil.isNotNullEmpty(authErrMsg)){
				throw new Exception(authErrMsg);
			}
			
			// set properties
			setCompId(compId);
			setRequestJson(reqJsonObj);
			
			// process
			checkData();
			doProcess();
			
			// 發票操作成功時
			if(null != getResultLists()){
				responseJson.put("respData", getResultLists());
			}
			responseJson.put("statusCode", RespResultCode.SUCCESS.getCode());
			responseJson.put("statusDesc", RespResultCode.SUCCESS.getDesc());
		} catch (JSONException je) {
			responseJson.put("statusCode", RespResultCode.DATA_CHECK_ERROR.getCode());
			responseJson.put("statusDesc", je.getMessage());
		} catch (Exception e) {
			String errorMsg = ExceptionUtils.getStackTrace(e);
			responseJson.put("statusCode", RespResultCode.SYSTEM_ERROR.getCode());
			responseJson.put("statusDesc", errorMsg);
			
			// set WebServlet Operation log long exception
			String logException = errorMsg;
			if(logException.getBytes().length > 4000){
				logException = logException.substring(0, 1000);
			}
			
			apiLog.setLogException(logException);
			log.error("不預期的錯誤：", e);
		} finally {
			log.info("response json: {}", responseJson.toString());
			
			try {
				// 防呆
				String statusDesc = responseJson.getString("statusDesc");
				if (statusDesc.getBytes().length > 256) {
					statusDesc = statusDesc.substring(0, 70) + "...";
				}
				responseJson.put("statusDesc", statusDesc);

				// update api log
				apiLog.setOupData(responseJson.toString());
				apiLog.setLogStatus(responseJson.getString("statusCode"));
				apiLog.setLogResult(responseJson.getString("statusDesc"));
				apiLogService.saveOrUpdateWsOplog(apiLog);
			} catch (Exception e) {
				log.error("不預期的錯誤：", e);
			}
		}
		log.info("{} 結束執行", className);
		return responseJson.toString();
	}
}
