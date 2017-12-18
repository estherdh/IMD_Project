package oose.p.c6.imd.persistent.dao;

import oose.p.c6.imd.domain.Notification;
import oose.p.c6.imd.domain.User;

import java.util.List;

public interface IUserDao extends IDao<User> {
    public User findUserByEmail(String email);
    public List<Notification> listNotification(User u);
}
