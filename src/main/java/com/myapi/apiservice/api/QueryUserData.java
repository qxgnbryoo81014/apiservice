package com.myapi.apiservice.api;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.myapi.common.utility.StringUtil;
import com.myapi.apiservice.api.base.BaseApiHandler;
import com.myapi.apiservice.api.bean.QueryUserDataBean;
import com.myapi.apiservice.entity.myconnb.MembUser;
import com.myapi.apiservice.service.ApiLogService;
import com.myapi.apiservice.service.MembUserService;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Slf4j
@Component
public class QueryUserData extends BaseApiHandler{
	
	@Resource
	MembUserService membUserService;
	
	@Resource
	ApiLogService apiLogService;
	
	private ArrayList<QueryUserDataBean> tempListForQuery;
	private ArrayList<JSONObject> finalResponseList;
	
	public ArrayList<QueryUserDataBean> getJsonObjectList() {
		return tempListForQuery;
	}

	public void setJsonObjectList(ArrayList<QueryUserDataBean> jsonObjectList) {
		this.tempListForQuery = jsonObjectList;
	}

	public ArrayList<JSONObject> getJsonRespObjectList() {
		return finalResponseList;
	}

	public void setJsonRespObjectList(ArrayList<JSONObject> jsonRespObjectList) {
		this.finalResponseList = jsonRespObjectList;
	}
	
	@Override
	public void checkData() throws Exception {
		tempListForQuery = new ArrayList<QueryUserDataBean>();
		finalResponseList = new ArrayList<JSONObject>();
		
		// get reqData from json object
		JSONObject reqData = (JSONObject) requestJson.get("reqData");
		if(null == reqData || reqData.size() == 0){
			throw new Exception("reqData未輸入任何資料");
		}
		
		JSONArray jsonArr = reqData.getJSONArray("queryLists");
		if (jsonArr.size() == 0) {
			throw new Exception("[queryLists]資料檢核錯誤;");
		}
		
		for (Object obj : jsonArr) {
			JSONObject dtlJson = (JSONObject) obj;
			validEventList = new StringBuilder();
			checkQueryField(StringUtil.notNull(dtlJson.get("userId")), "userId", M, 50, true, null, validEventList);

			String result = null;
			String statusDesc = null;

			// 以上檢核有錯誤，說明錯誤原因 &跳過該筆查詢
			if (validEventList.length() > 0) {
				result = validEventList.toString();
				statusDesc = "9";

				JSONObject invObj = new JSONObject();
				invObj.put("statusDesc", statusDesc);
				invObj.put("result", result);
				finalResponseList.add(invObj);
				continue;
			}

			QueryUserDataBean jsonObject = new QueryUserDataBean();
			jsonObject.setUserId(StringUtil.notNull(dtlJson.get("userId")));
			jsonObject.setStatus(StringUtil.notNull(dtlJson.get("status")));
			tempListForQuery.add(jsonObject);
		}
	}
	
	@Override
	public void doProcess() throws Exception {
		for (QueryUserDataBean jsonObject : tempListForQuery) {
			String statusDesc = "";
			String result = "成功";
			MembUser user = new MembUser();
			try {
				user = membUserService.getMembUser(jsonObject.getUserId());
				if (null == user) {
					throw new Exception("找不到該使用者");
				}

				statusDesc = getUserStatusDesc(user);

			} catch (Exception e) {
				log.error("doProcess error:", e);
				result = e.getMessage();
			} finally {
				// set response Data json, respData
				JSONObject invObj = new JSONObject();
				invObj.put("userId", user.getUserId());
				invObj.put("status", user.getStatus());
				invObj.put("userName", user.getUserName());
				invObj.put("address", user.getAddress());
				invObj.put("email", user.getEmail());
				invObj.put("tel", user.getTel());
				invObj.put("mobile", user.getMobile());
				invObj.put("statusDesc", statusDesc);
				invObj.put("resultDesc", StringUtil.notNull(result));
				finalResponseList.add(invObj);
			}
		}
		
		if (finalResponseList.size() > 0) {
			JSONObject respObj = new JSONObject();
			respObj.put("resultLists", finalResponseList);
			resultLists = respObj;
		}
	}
	
	private String getUserStatusDesc(MembUser user) {
		String status = user.getStatus();
		String desc = "";
		if("1".equals(status)) {
			desc = "一般";
		} else if("9".equals(status)) {
			desc = "封鎖";
		}
		return desc;
	}
}