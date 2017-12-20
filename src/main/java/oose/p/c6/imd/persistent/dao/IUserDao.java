package oose.p.c6.imd.persistent.dao;

import oose.p.c6.imd.domain.User;

public interface IUserDao extends IDao<User> {
    public void add(User entity);
    public User findUserByemail(String email);
}
