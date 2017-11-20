package oose.p.c6.imd.service;

import oose.p.c6.imd.domain.Librarian;
import oose.p.c6.imd.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
		User user = new User(1, "test@test", "test", "Tester test", 0);
		String token = manager.createTokenForUser(user).getTokenString();
		JsonObject jo = Json.createObjectBuilder()
				.add("qrCode", "aldfjalskdasdfasdf")
				.add("token", token)
				.build();
		//test
		service.scanQrCode(jo);
		//check
		verify(librarian, times(1)).scanQrCode(user, "aldfjalskdasdfasdf");
	}

}