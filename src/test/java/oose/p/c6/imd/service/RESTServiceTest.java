package oose.p.c6.imd.service;

import oose.p.c6.imd.domain.Librarian;
import oose.p.c6.imd.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RESTServiceTest {
	@Mock
	private Librarian librarian;
	@InjectMocks
	private RESTService service;

	@Before
	public void setUp() {
		//Nothing as of yet
	}

	@Test
	public void login() throws Exception {
	}

	@Test
	public void scanQrCode() throws Exception {
		//init
		TokenManager manager = TokenManager.getInstance();
		User user = new User(1, "test@test", "test", "Tester test", 0, 1);
		String token = manager.createTokenForUser(user).getTokenString();
		JsonObject jo = Json.createObjectBuilder()
				.add("qrCode", "aldfjalskdasdfasdf")
				.build();
		//test
		service.scanQrCode(token, jo);
		//check
		verify(librarian, times(1)).scanQrCode(user, "aldfjalskdasdfasdf");
	}

	@Test
	public void removeQuestFromQuestLog() {
		//test
		TokenManager manager = mock(TokenManager.class);
		User mockUser = mock(User.class);
		TokenManager.setInstance(manager);
		when(manager.getUserFromToken("userToken")).thenReturn(mockUser);
		service.removeQuestFromQuestLog(1, "userToken");
		//check
		verify(librarian, times(1)).removeQuestFromQuestLog(1, mockUser);
		TokenManager.setInstance(null);
	}

	@Test
	public void getExhibitDetails(){
		//init
		TokenManager manager = TokenManager.getInstance();
		User user = new User(1, "test@test", "test", "Tester test", 0, 1);
		String token = manager.createTokenForUser(user).getTokenString();
		//test
		service.getExhibitDetails(1, token);
		//check
		verify(librarian, times(1)).getExhibitDetails(user, 1);
	}

}