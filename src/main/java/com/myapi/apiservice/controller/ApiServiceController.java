package com.myapi.apiservice.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.myapi.apiservice.api.base.BaseApiInterface;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * @author YvesChiang
 *
 */
@Slf4j
@RestController
public class ApiServiceController implements ApplicationContextAware{
	
	private ApplicationContext applicationContext;
	
	private Map<String,BaseApiInterface> baseApiInterfaceMap;
	
	@Override
	public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	@PostConstruct
	private void init() {
		for(BaseApiInterface base : new ArrayList<>(applicationContext.getBeansOfType(BaseApiInterface.class).values())){
            log.info("載入 BaseApiInterface 實現類: {}", base);
            baseApiInterfaceMap = new HashMap<String, BaseApiInterface>(){{
            	String name = base.getClass().getSimpleName();
            	put(name.substring(0, 1).toLowerCase() + name.substring(1), base);
            }};
        }
	}
	
	@RequestMapping(path = "/queryUserData", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> queryUserData(@RequestBody String reqData, HttpServletRequest request) throws Exception {
		String servletPath = request.getServletPath().replaceAll("/", "");
		BaseApiInterface service = baseApiInterfaceMap.get(servletPath);
		if(service == null){
			log.error("{} 查無對應的 api service", servletPath);
			return new ResponseEntity<>("查無對應的 api service", HttpStatus.NOT_FOUND);
		}
		String responseJson = service.runProcess(servletPath, reqData);
		return new ResponseEntity<>(responseJson, HttpStatus.OK);
	}
}
