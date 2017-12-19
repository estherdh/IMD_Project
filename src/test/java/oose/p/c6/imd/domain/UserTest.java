package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.DAOFactory;
import oose.p.c6.imd.persistent.dao.IUserDao;
import oose.p.c6.imd.persistent.dao.QuestJDBCDao;
import oose.p.c6.imd.persistent.dao.UserJDBCDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.json.Json;
import javax.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserTest {
	@Mock
	private QuestJDBCDao questDao;
	private UserJDBCDao userDao;
	@InjectMocks
	private User user = new User(0, "mail", "password", "fullname", 0, 1);

	@Test
	public void checkQuestCompletedAddCoins() throws Exception {
		//init
		Action inputAction = mock(Action.class);
		QuestLog questLog = mock(QuestLog.class);
		user.setQuestLog(questLog);
		when(questLog.checkQuestComplete(inputAction, 0, 1)).thenReturn(100);
		//test
		boolean actualResult = user.checkQuestCompleted(inputAction);
		//check
		assertTrue(actualResult);
		assertThat(user.getCoins(), is(100));
	}

	@Test
	public void checkQuestQuestFailedNoCoins() throws Exception {
		//init
		Action inputAction = mock(Action.class);
		QuestLog questLog = mock(QuestLog.class);
		user.setQuestLog(questLog);
		when(questLog.checkQuestComplete(inputAction, 0, 1)).thenReturn(0);
		//test
		Boolean actualResult = user.checkQuestCompleted(inputAction);
		//check
		assertThat(user.getCoins(), is(0));
		assertFalse(actualResult);
	}

	@Test
	public void removeQuestFromBacklogSuccess() {
		//init
		User user = new User(1, "email", "password", "fullName", 10, 1);
		QuestLog questLog = mock(QuestLog.class);
		user.setQuestLog(questLog);
		when(questLog.removeQuestFromQuestLog(1, 1)).thenReturn(true);
		//test
		boolean actualResult = user.removeQuestFromQuestLog(1);
		//check
		assertTrue(actualResult);
	}

	@Test
	public void updateUserSuccess() {
		//init
		JsonObject jo = Json.createObjectBuilder()
				.add("email", "test@mail.com")
				.add("displayName", "testUser")
				.add("password", "testPassword")
				.add("languageId", 2)
				.build();
		//test
		int actualResponse =   user.updateUser(jo.getString("email"), jo.getString("displayName"), jo.getString("password"), jo.getInt("languageId"), user);
		//check
		assertThat(actualResponse, is(0));
		assertEquals("test@mail.com", jo.getString("email"));
		assertEquals("testUser", jo.getString("displayName"));
		assertEquals("testPassword", jo.getString("password"));
		assertEquals(2,jo.getInt("languageId"));
	}

	@Test
	public void updateUserInvalidEmail() {
		JsonObject jo = Json.createObjectBuilder()
				.add("email", "------")
				.add("displayName", "testUser")
				.add("password", "testPassword")
				.add("languageId", 1)
				.build();
		//test
		int actualResponse =   user.updateUser(jo.getString("email"), jo.getString("displayName"), jo.getString("password"), jo.getInt("languageId"), user);
		//check
		assertThat(actualResponse, is(3));
	}

	@Test
	public void updateUserInvalidDisplayName() {
		JsonObject jo = Json.createObjectBuilder()
				.add("email", "test@mail.com")
				.add("displayName", "")
				.add("password", "testPassword")
				.add("languageId", 1)
				.build();
		//test
		int actualResponse =   user.updateUser(jo.getString("email"), jo.getString("displayName"), jo.getString("password"), jo.getInt("languageId"), user);
		//check
		assertThat(actualResponse, is(2));
	}

	@Test
	public void updateUserInvalidPassword() {
		JsonObject jo = Json.createObjectBuilder()
				.add("email", "test@mail.com")
				.add("displayName", "testUser")
				.add("password", "")
				.add("languageId", 1)
				.build();
		//test
		int actualResponse =   user.updateUser(jo.getString("email"), jo.getString("displayName"), jo.getString("password"), jo.getInt("languageId"), user);
		//check
		assertThat(actualResponse, is(1));
	}

	@Test
	public void getNotifications() {
		IUserDao dao = mock(IUserDao.class);
		DAOFactory.setUserDao(dao);
		User test2 = new User(2, "test@void", "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08", "muspi merol", 0, 2);
		List<Notification> expected = new ArrayList<Notification>();
		Notification n = new Notification(1, "NOW", "YEAH", false,5);
		expected.add(n);
		when(dao.listNotification(test2)).thenReturn(expected);
		List<Notification> list = test2.getNotifications();
		assertEquals(list.get(0).getId(), expected.get(0).getId());
		assertEquals(list.get(0).getText(), expected.get(0).getText());
		assertEquals(list.get(0).getRead(), expected.get(0).getRead());
		assertEquals(list.get(0).getTime(), expected.get(0).getTime());
	}




}