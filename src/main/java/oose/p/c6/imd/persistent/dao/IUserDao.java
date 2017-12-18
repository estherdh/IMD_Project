package oose.p.c6.imd.persistent.dao;

import oose.p.c6.imd.domain.User;

import java.util.Map;

public interface IUserDao extends IDao<User> {
    public User findUserByemail(String email);
	void addNotification(int typeId, Map<String, String> properties, User user);
}
