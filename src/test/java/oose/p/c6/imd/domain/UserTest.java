package oose.p.c6.imd.domain;

import oose.p.c6.imd.domain.User;
import oose.p.c6.imd.persistent.dao.QuestDao;
import oose.p.c6.imd.persistent.dao.UserJDBCDao;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class UserTest {
	@Mock
	QuestDao questDao;
	@InjectMocks
	User user;

	//TODO fix deze functie, werkt op het moment niet omdmat de rest nog niet is geïmplementeerd.
	@Test
	public void removeQuestTestSuccess() {
		//init
//		User user = new User();
//		user.setId(1);
//		when(questDao.removeQuest(1, 1)).thenReturn(true);
		//test
//		Response actualResult = user.removeQuest(1);
		//check
//		assertThat(actualResult.getStatus(), is(200));

		//user = new User(5, "hoi@hoi", "test15", "jan", 12, 1);
		//new UserJDBCDao().add(user);
		//System.out.println(new UserJDBCDao().list().size());
		//System.out.println(new UserJDBCDao().find(1).getEmail());
		//new UserJDBCDao().update(user);
		//new UserJDBCDao().remove(user);
		//System.out.println(new UserJDBCDao().findUserByemail("mail").getEmail());
	}


}