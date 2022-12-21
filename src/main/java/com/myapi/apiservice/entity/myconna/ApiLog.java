package com.myapi.apiservice.entity.myconna;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="API_LOG")
public class ApiLog implements Serializable {
	private static final long serialVersionUID = 3398163746918827499L;
	
	@Id
	@Column(name="API_LOG_UUID")
	private String apiLogUuid;

	@Column(name="API_NAME")
	private String apiName;
	
	@Column(name="SERVER_IP")
	private String serverIp;
	
	@Column(name="INP_DATA")
	private String inpData;
	
	@Column(name="OUP_DATA")
	private String oupData;
	
	@Column(name="LOG_STATUS")
	private String logStatus;
	
	@Column(name="LOG_RESULT")
	private String logResult;
	
	@Column(name="LOG_EXCEPTION")
	private String logException;
	
	@Column(name="CREATE_DATE")
	private String createDate;

	public ApiLog(String apiLogUuid, String apiName, String serverIp, String inpData, String createDate) {
		this.apiLogUuid = apiLogUuid;
		this.apiName = apiName;
		this.serverIp = serverIp;
		this.inpData = inpData;
		this.createDate = createDate;
	}
}
