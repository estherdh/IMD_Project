package oose.p.c6.imd.persistent.dao;

import oose.p.c6.imd.domain.Notification;
import oose.p.c6.imd.domain.User;

import java.util.List;

import java.util.Map;

public interface IUserDao extends IDao<User> {
    public List<Notification> listNotification(User u);
	void addNotification(int typeId, Map<String, String> properties, User user);
	User getUserByQuestId(int id);
    public void add(User entity);
    public User findUserByEmail(String email);
}
