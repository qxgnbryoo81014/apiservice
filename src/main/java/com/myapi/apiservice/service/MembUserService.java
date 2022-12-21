package com.myapi.apiservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapi.apiservice.dao.impl.GenericServiceB;
import com.myapi.apiservice.entity.myconnb.MembUser;


@Service
public class MembUserService {

	@Autowired
	private GenericServiceB<MembUser, String> musev;
	
	public MembUser getMembUser(String userId) {
		List<MembUser> list = musev.find(" from MembUser ", new Object[] {});
		musev.setEntityClass(MembUser.class);
		return musev.get(userId);
	}
}
