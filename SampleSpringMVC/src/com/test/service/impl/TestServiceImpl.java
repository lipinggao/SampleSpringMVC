package com.test.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.dao.UserDao;
import com.test.model.User;
import com.test.service.TestService;
import com.test.util.MD5Helper;

@Service("TestService")
public class TestServiceImpl implements TestService {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	UserDao userDao;

	@Override
	public String getWelcomeMsg(String userId) {
		String welcomeMsg = null;
		if(userId == null){
			logger.error("User id empty");
			welcomeMsg = "Empty name";
		}else{
			User user = userDao.getUserByName(userId);
			if (user == null) {
				welcomeMsg = "Unknown user";
				user = new User();
				user.setName(userId);
				user.setPassword(MD5Helper.MD5Encode32(userId));
				user.setEmail(String.format("%s@163.com", userId));
				User savedUser = userDao.save(user);
				if(savedUser != null){
					logger.info("Created user " + userId);
				}
			} else {
				logger.info("User exist");
				welcomeMsg = String.format("I am %s, my mail is %s", userId, user.getEmail());
			}
		}
		return welcomeMsg;
	}

}
