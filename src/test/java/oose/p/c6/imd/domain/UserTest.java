package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.QuestJDBCDao;
import oose.p.c6.imd.persistent.dao.UserJDBCDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.json.Json;
import javax.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserTest {
	@Mock
	private QuestJDBCDao questDao;
	@Mock
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
	public void addNotificationTestSuccess() {
		//init
		Map expectedResult = new HashMap<String, String>();
		//test
		user.addNotification(1, expectedResult);
		//check
		verify(userDao, times(1)).addNotification(1, expectedResult, user);
	}
}