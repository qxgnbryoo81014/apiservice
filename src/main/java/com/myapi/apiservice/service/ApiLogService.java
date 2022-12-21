package com.myapi.apiservice.service;

import java.net.InetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapi.apiservice.dao.impl.GenericServiceA;
import com.myapi.apiservice.entity.myconna.ApiLog;
import com.myapi.common.utility.DateUtil;
import com.myapi.common.utility.StringUtil;

@Service
public class ApiLogService {
	
	@Autowired
	private GenericServiceA<ApiLog, String> sevb;

	public ApiLog setReqlog(String apiName, String inpData) {
		ApiLog log = new ApiLog(StringUtil.uuid(), apiName, getIP(), inpData, DateUtil.getCurrentDateTime());
		return log;
	}
	
	public void insertLog(ApiLog log) throws Exception {
		sevb.save(log);
	}
	
	/**
	 * 更新WebServlet紀錄檔
	 * @param log
	 */
	public void saveOrUpdateWsOplog(ApiLog log){
		sevb.saveOrUpdate(log);
	}
	
	/**
	 * 取得IP資料
	 * @return
	 */
	public String getIP() {
		String ipStr = "N/A";
		try {
			InetAddress IP = InetAddress.getLocalHost();
			ipStr = IP.getHostAddress();
		}catch(Exception e){
			e.printStackTrace();
		}
		return ipStr;
	}
}
