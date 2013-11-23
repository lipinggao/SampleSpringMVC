package com.test.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.test.dao.UserDao;
import com.test.model.User;

@Repository("userDao")
public class UserDaoHibernate extends GenericDaoHibernate<User, Integer> implements
		UserDao {

	UserDaoHibernate() {
        super(User.class);
    }


	@SuppressWarnings("unchecked")
	@Override
	public User getUserByName(String userId) {
		List<User> userList = (List<User>) getHibernateTemplate().find(
                "from User u where u.name = ?", userId);
        return userList.isEmpty() ? null : userList.get(0);
	}


	@Override
	public User addUser(User user) {
		// TODO Auto-generated method stub
		return this.save(user);
	}

}
