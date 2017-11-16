package oose.p.c6.imd.service;

import oose.p.c6.imd.domain.User;

import java.util.ArrayList;

public class TokenManager {
	public static ArrayList<User> users = new ArrayList<User>();

	public static void addUser(User user) {
		users.add(user);
	}

	public static User getUserByToken(String token) {
		for (User user : users) {
			if (user.getToken().equals(token)) {
				return user;
			}
		}
		return null;
	}
}
