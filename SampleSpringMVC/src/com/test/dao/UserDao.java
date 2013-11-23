package com.test.dao;

import com.test.model.User;

public interface UserDao extends GenericDao<User, Integer> {
	
	User getUserByName(String userId);
	
	User addUser(User user);

}
