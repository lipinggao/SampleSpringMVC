package com.test.service;

import org.springframework.stereotype.Service;

@Service("TestService")
public class TestServiceImpl implements TestService {

	@Override
	public String getWelcomeMsg(String userId) {
		// TODO Auto-generated method stub
		if(userId == null){
			userId = "Unknown";
		}
		return String.format("I am %s", userId);
	}

}
