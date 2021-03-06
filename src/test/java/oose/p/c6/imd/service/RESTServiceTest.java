package oose.p.c6.imd.service;

import oose.p.c6.imd.domain.*;
import oose.p.c6.imd.persistent.dao.DAOFactory;
import oose.p.c6.imd.persistent.dao.IUserDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.json.*;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RESTServiceTest {
    @Mock
    Token token;
    @Mock
    TokenManager tokenManager;
    @Mock
    private Librarian librarian;
    @InjectMocks
    private RESTService service;

    @Before
    public void setUp() {
        TokenManager.setInstance(tokenManager);
        when(tokenManager.createTokenForUser(any(User.class))).thenReturn(token);
    }

    @After
    public void after() {
        TokenManager.setInstance(null);
    }

    @Test
    public void loginTestSuccess() throws Exception {
        //init
        JsonObject jo = Json.createObjectBuilder()
                .add("email", "mail address")
                .add("password", "a bad password")
                .build();

        when(librarian.verifyLogin("mail address", "a bad password")).thenReturn(0);
        when(librarian.getUserByEmail(anyString())).thenReturn(mock(User.class));
        when(token.getTokenString()).thenReturn("token");
        //test
        Response actualResponse = service.login(jo);
        //check
        verify(librarian, times(1)).getUserByEmail("mail address");
        assertThat(actualResponse.getStatus(), is(201));
        JsonObject jsonResponse = (JsonObject) actualResponse.getEntity();
        assertTrue(jsonResponse.containsKey("token"));
        assertFalse(jsonResponse.containsKey("reason"));
    }

    @Test
    public void loginTestInvalidEmail() throws Exception {
        //init
        JsonObject jo = Json.createObjectBuilder()
                .add("email", "mail address")
                .add("password", "a bad password")
                .build();

        when(librarian.verifyLogin("mail address", "a bad password")).thenReturn(1);
        //test
        Response actualResponse = service.login(jo);
        //check
        assertThat(actualResponse.getStatus(), is(401));
        JsonObject jsonResponse = (JsonObject) actualResponse.getEntity();
        assertFalse(jsonResponse.containsKey("token"));
        assertThat(jsonResponse.getInt("reason"), is(1));
    }

    @Test
    public void loginTestInvalidPassword() throws Exception {
        //init
        JsonObject jo = Json.createObjectBuilder()
                .add("email", "mail address")
                .add("password", "a bad password")
                .build();

        when(librarian.verifyLogin("mail address", "a bad password")).thenReturn(2);
        //test
        Response actualResponse = service.login(jo);
        //check
        assertThat(actualResponse.getStatus(), is(401));
        JsonObject jsonResponse = (JsonObject) actualResponse.getEntity();
        assertFalse(jsonResponse.containsKey("token"));
        assertThat(jsonResponse.getInt("reason"), is(2));
    }

    @Test
    public void buyReplicaTestSuccess() throws Exception {
        //init
        User mockUser = mock(User.class);
        when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
        when(librarian.buyReplica(mockUser, 1)).thenReturn(true);
        //test
        Response actualResponse = service.buyReplica(1, "token");
        //check
        verify(librarian, times(1)).buyReplica(mockUser, 1);
        assertThat(actualResponse.getStatus(), is(201));
        JsonObject jsonResponse = (JsonObject) actualResponse.getEntity();
        assertTrue(jsonResponse.getBoolean("isPurchased"));
    }

    @Test
    public void buyReplicaCantBuy() throws Exception {
        //init
        User mockUser = mock(User.class);
        when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
        when(librarian.buyReplica(mockUser, 1)).thenReturn(false);
        //test
        Response actualResponse = service.buyReplica(1, "token");
        //check
        verify(librarian, times(1)).buyReplica(mockUser, 1);
        assertThat(actualResponse.getStatus(), is(201));
        JsonObject jsonResponse = (JsonObject) actualResponse.getEntity();
        assertFalse(jsonResponse.getBoolean("isPurchased"));
    }

    @Test
    public void buyReplicaTestInvalidUser() throws Exception {
        //init
        when(tokenManager.getUserFromToken("token")).thenReturn(null);
        //test
        Response actualResponse = service.buyReplica(1, "token");
        //check
        assertThat(actualResponse.getStatus(), is(401));
        JsonObject jsonResponse = (JsonObject) actualResponse.getEntity();
    }

    @Test
    public void getReplicasTestSuccess() throws Exception {
        //init
        Era era = new Era(1, "tijdperk test");
        Exhibit exhibit = new Exhibit(1, "Het test object",
                "Dit object wordt altijd al gebruikt om te testen", null, new ArrayList<String>(){{
            add("imagetest1");
            add("imagetest2");
        }},
                "300-800 v.C.", 1, 1);
        exhibit.setEra(era);
        Replica replica = new Replica(3, 1, 15, "test2", 2, 0, exhibit);
        List<Replica> expected = new ArrayList<>();
        expected.add(replica);
        User mockUser = mock(User.class);

        when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
        when(librarian.getAvailableReplicas(mockUser)).thenReturn(expected);
        //test
        Response actual = service.getReplicas("token");
        JsonArray jsonArray = (JsonArray) actual.getEntity();
        JsonObject object1 = (JsonObject) jsonArray.get(0);
        //check replica
        assertThat(object1.getInt("ReplicaId"), is(expected.get(0).getId()));
        assertThat(object1.getInt("PlacementCategoryId"), is(expected.get(0).getType()));
        assertThat(object1.getString("Image"), is(expected.get(0).getSprite()));
        assertThat(object1.getInt("Price"), is(expected.get(0).getPrice()));
        //check exhibit
        assertThat(object1.getJsonObject("Exhibit").getInt("ExhibitId"),  is(expected.get(0).getExhibit().getId()));
        assertThat(object1.getJsonObject("Exhibit").getString("Name"),  is(expected.get(0).getExhibit().getName()));
        assertThat(object1.getJsonObject("Exhibit").getString("Description"),  is(expected.get(0).getExhibit().getDescription()));
        assertThat(object1.getJsonObject("Exhibit").getString("Video"),  is("undefined"));
        assertThat(object1.getJsonObject("Exhibit").getString("Year"),  is(expected.get(0).getExhibit().getYear()));
        assertThat(object1.getJsonObject("Exhibit").getInt("MuseumId"),  is(expected.get(0).getExhibit().getMuseumId()));
        //check era
        assertThat(object1.getJsonObject("Exhibit").getJsonObject("Era").getInt("EraId"),  is(expected.get(0).getExhibit().getEra().getId()));
        assertThat(object1.getJsonObject("Exhibit").getJsonObject("Era").getString("Name"),  is(expected.get(0).getExhibit().getEra().getName()));

        assertThat(actual.getStatus(), is(200));
    }

	@Test
	public void getReplicasTestIncorrectUser() throws Exception {
		//init
		when(tokenManager.getUserFromToken("token")).thenReturn(null);
		//test
		Response actualResponse = service.getReplicas("token");
		//check
		assertThat(actualResponse.getStatus(), is(401));
	}

	@Test
	public void getReplicasFromUserTestSuccess() throws Exception {
		//init
		Era era = new Era(1, "tijdperk test");
		Exhibit exhibit = new Exhibit(1, "Het test object",
				"Dit object wordt altijd al gebruikt om te testen", null, new ArrayList<String>(){{
            add("imagetest1");
            add("imagetest2");
        }},
                "300-800 v.C.", 1, 1);
		exhibit.setEra(era);
		Replica replica = new Replica(2, 1, 15, "test1", 2, 1, exhibit);
		List<Replica> expected = new ArrayList<>();
		expected.add(replica);
		User mockUser = mock(User.class);

		when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
		when(librarian.getReplicasFromUser(mockUser)).thenReturn(expected);
		//test
		Response actual = service.getReplicasFromUser("token");
		JsonArray jsonArray = (JsonArray) actual.getEntity();
		JsonObject object1 = (JsonObject) jsonArray.get(0);
		//check replica
		assertThat(object1.getInt("ReplicaId"), is(expected.get(0).getId()));
		assertThat(object1.getInt("PlacementCategoryId"), is(expected.get(0).getType()));
		assertThat(object1.getString("Image"), is(expected.get(0).getSprite()));
		assertThat(object1.getInt("Price"), is(expected.get(0).getPrice()));
		assertThat(object1.getInt("Position"), is(expected.get(0).getPosition()));
		//check exhibit
		assertThat(object1.getJsonObject("Exhibit").getInt("ExhibitId"),  is(expected.get(0).getExhibit().getId()));
		assertThat(object1.getJsonObject("Exhibit").getString("Name"),  is(expected.get(0).getExhibit().getName()));
		assertThat(object1.getJsonObject("Exhibit").getString("Description"),  is(expected.get(0).getExhibit().getDescription()));
		assertThat(object1.getJsonObject("Exhibit").getString("Video"),  is("undefined"));
		assertThat(object1.getJsonObject("Exhibit").getString("Year"),  is(expected.get(0).getExhibit().getYear()));
		assertThat(object1.getJsonObject("Exhibit").getInt("MuseumId"),  is(expected.get(0).getExhibit().getMuseumId()));
		//check era
		assertThat(object1.getJsonObject("Exhibit").getJsonObject("Era").getInt("EraId"),  is(expected.get(0).getExhibit().getEra().getId()));
		assertThat(object1.getJsonObject("Exhibit").getJsonObject("Era").getString("Name"),  is(expected.get(0).getExhibit().getEra().getName()));

		assertThat(actual.getStatus(), is(200));
	}

	@Test
	public void getReplicasFromUserTestIncorrectUser() throws Exception {
		//init
		when(tokenManager.getUserFromToken("token")).thenReturn(null);
		//test
		Response actualResponse = service.getReplicasFromUser("token");
		//check
		assertThat(actualResponse.getStatus(), is(401));
	}

	@Test
	public void scanQrCodeTestSuccess() throws Exception {
		//init
		User mockUser = mock(User.class);
		when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
		JsonObject jo = Json.createObjectBuilder()
				.add("qrCode", "qrCode")
				.build();
		//test
		service.scanQrCode("token", jo);
		//check
		verify(librarian, times(1)).scanQrCode(mockUser, "qrCode");
	}

	@Test
	public void scanQrCodeTestInvalidUser() throws Exception {
		//init
		User mockUser = mock(User.class);
		JsonObject jo = Json.createObjectBuilder()
				.add("qrCode", "qrCode")
				.build();
		//test
		service.scanQrCode("token", jo);
		//check
		verify(librarian, times(0)).scanQrCode(mockUser, "qrCode");
	}

	@Test
    public void getMuseumByQrTestSuccess() {
        //init
        String qrCode = "AAA";
        User mockUser = mock(User.class);
        when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
        //test
        service.getMuseumByQr("token", qrCode);
        //check
        verify(librarian, times(1)).scanQrCode(mockUser, qrCode);
        verify(librarian, times(1)).getMuseumByQr(qrCode);
    }

    @Test
    public void getMuseumByQrTestInvalidUser() {
        //init
        String qrCode = "AAA";
        User mockUser = mock(User.class);
        //test
        service.getMuseumByQr("token", qrCode);
        //check
        verify(librarian, times(0)).scanQrCode(mockUser, qrCode);
        verify(librarian, times(0)).getMuseumByQr(qrCode);
    }

	@Test
	public void removeQuestFromQuestLogTestSuccess() {
		//init
		User mockUser = mock(User.class);
		when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
		//test
		Response actualResponse = service.removeQuestFromQuestLog(1, "token");
		//check
		verify(librarian, times(1)).removeQuestFromQuestLog(1, mockUser);
		assertThat(actualResponse.getStatus(), is(200));
	}

	@Test
	public void removeQuestFromQuestLogTestInvalidUser() {
		//init
		User mockUser = mock(User.class);
		//test
		Response actualResponse = service.removeQuestFromQuestLog(1, "token");
		//check
		verify(librarian, times(0)).removeQuestFromQuestLog(1, mockUser);
		assertThat(actualResponse.getStatus(), is(401));
	}

	@Test
	public void getQuestLogTestSuccess() throws Exception {
		//init
		List<Quest> questList = new ArrayList<>();
		questList.add(mock(Quest.class));
		questList.add(mock(Quest.class));

		User mockUser = mock(User.class);
		when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
		when(librarian.getQuestLog(mockUser)).thenReturn(questList);

		//test
		Response actualResponse = service.getQuestLog("token");
		//check
		List<Quest> actualList = (List<Quest>) actualResponse.getEntity();
		assertThat(actualList, is(questList));
		assertThat(actualResponse.getStatus(), is(200));
	}

	@Test
	public void getQuestLogTestEmptyQuestlog() throws Exception {
		//init
		List<Quest> questList = new ArrayList<>();

		User mockUser = mock(User.class);
		when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
		when(librarian.getQuestLog(mockUser)).thenReturn(questList);

		//test
		Response actualResponse = service.getQuestLog("token");
		//check
		assertThat(actualResponse.getEntity(), is(nullValue()));
		assertThat(actualResponse.getStatus(), is(200));
	}

	@Test
	public void getQuestLogTestInvalidUser() throws Exception {
		//init
		when(tokenManager.getUserFromToken("token")).thenReturn(null);
		//test
		Response actualResponse = service.getQuestLog("token");
		//check
		assertThat(actualResponse.getEntity(), is(nullValue()));
		assertThat(actualResponse.getStatus(), is(401));
	}

	@Test
	public void getExhibitDetailsTestSuccess(){
		//init
		Exhibit exhibit = new Exhibit(1, "topstuk", "a topstuk", null, new ArrayList<String>() {{
		    add("test");
		}}, "300-800 v.C.", 2, 1);
		User mockUser = mock(User.class);
		when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
		when(librarian.getExhibitDetails(mockUser, 1)).thenReturn(exhibit);
		//test
		Response actualResponse = service.getExhibitDetails(1, "token");
		//check
		JsonObject json = (JsonObject) actualResponse.getEntity();
		assertThat(actualResponse.getStatus(), is(200));
		assertThat(json.getInt("ExhibitId"), is(1));
		assertThat(json.getString("Name"), is(equalTo("topstuk")));
		assertThat(json.getString("Description"), is(equalTo("a topstuk")));
		assertThat(json.getString("Video"), is(equalTo("undefined")));
		assertThat(json.getJsonArray("Images").get(0).toString(), is(equalTo("\"test\"")));
		assertThat(json.getString("Year"), is("300-800 v.C."));
		assertThat(json.getInt("EraId"), is(2));
		assertThat(json.getInt("MuseumId"), is(1));
	}

	@Test
	public void getExhibitDetailsTestNoExhibit() {
		//init
		User mockUser = mock(User.class);
		when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
		when(librarian.getExhibitDetails(mockUser, 1)).thenReturn(null);
		//test
		Response actualResponse = service.getExhibitDetails(1, "token");
		//check
		assertThat(actualResponse.getStatus(), is(200));
	}

	@Test
	public void getExhibitDetailsTestInvalidUser() {
		//init
		when(tokenManager.getUserFromToken("token")).thenReturn(null);
		//test
		Response actualResponse = service.getExhibitDetails(1, "token");
		//check
		assertThat(actualResponse.getStatus(), is(401));
	}

	@Test
	public void getExhibitsTestSuccess() {
		//init
		Exhibit exhibit1 = new Exhibit(1, "topstuk", "a topstuk", null, new ArrayList<>(), "300-800 v.C.", 2, 1);
		Exhibit exhibit2 = new Exhibit(2, "topstuk", "a topstuk", null, new ArrayList<>(), "300-800 v.C.", 2, 1);
		List<Exhibit> exhibitList = new ArrayList<>();
		exhibitList.add(exhibit1);
		exhibitList.add(exhibit2);

		User mockUser = mock(User.class);
		when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
		when(librarian.getAvailableExhibits(mockUser)).thenReturn(exhibitList);
		//test
		Response actualResponse = service.getExhibits("token");
		//check
		JsonArray jsonArray = (JsonArray) actualResponse.getEntity();
		JsonObject object1 = (JsonObject) jsonArray.get(0);
		JsonObject object2 = (JsonObject) jsonArray.get(1);
		assertThat(actualResponse.getStatus(), is(200));
		assertThat(object1.getInt("ExhibitId"), is(1));
		assertThat(object2.getInt("ExhibitId"), is(2));
	}

	@Test
	public void getExhibitsTestEmptyArray() {
		//init
		List<Exhibit> exhibitList = new ArrayList<>();

		User mockUser = mock(User.class);
		when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
		when(librarian.getAvailableExhibits(mockUser)).thenReturn(exhibitList);
		//test
		Response actualResponse = service.getExhibits("token");
		//check
		assertThat(actualResponse.getEntity(), is(nullValue()));
		assertThat(actualResponse.getStatus(), is(200));
	}

	@Test
	public void getExhibitsInvalidUser() {
		//init
		when(tokenManager.getUserFromToken("token")).thenReturn(null);
		//test
		Response actualResponse = service.getExhibits("token");
		//check
		assertThat(actualResponse.getStatus(), is(401));
	}

	@Test
	public void getExhibitsFromMuseumTestSuccess() {
		//init
		Exhibit exhibit1 = new Exhibit(1, "topstuk", "a topstuk", null, new ArrayList<>(), "300-800 v.C.", 2, 1);
		Exhibit exhibit2 = new Exhibit(2, "topstuk", "a topstuk", null, new ArrayList<>(), "300-800 v.C.", 2, 1);
		List<Exhibit> exhibitList = new ArrayList<>();
		exhibitList.add(exhibit1);
		exhibitList.add(exhibit2);

		User mockUser = mock(User.class);
		when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
		when(librarian.getAvailableExhibitsFromMuseum(mockUser, 1)).thenReturn(exhibitList);
		//test
		Response actualResponse = service.getExhibitsFromMuseum(1, "token");
		//check
		JsonArray jsonArray = (JsonArray) actualResponse.getEntity();
		JsonObject object1 = (JsonObject) jsonArray.get(0);
		JsonObject object2 = (JsonObject) jsonArray.get(1);
		assertThat(actualResponse.getStatus(), is(200));
		assertThat(object1.getInt("ExhibitId"), is(1));
		assertThat(object2.getInt("ExhibitId"), is(2));
	}

    @Test
    public void getExhibitsFromMuseumTestEmptyArray() {
        //init
        List<Exhibit> exhibitList = new ArrayList<>();

        User mockUser = mock(User.class);
        when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
        when(librarian.getAvailableExhibitsFromMuseum(mockUser, 1)).thenReturn(exhibitList);
        //test
        Response actualResponse = service.getExhibitsFromMuseum(1, "token");
        //check
        assertThat(actualResponse.getEntity(), is(nullValue()));
        assertThat(actualResponse.getStatus(), is(200));
    }

    @Test
    public void getExhibitsFromMuseumInvalidUser() {
        //init
        when(tokenManager.getUserFromToken("token")).thenReturn(null);
        //test
        Response actualResponse = service.getExhibitsFromMuseum(1, "token");
        //check
        assertThat(actualResponse.getStatus(), is(401));
    }

    @Test
    public void getExhibitsFromEraTestSuccess() {
        //init
        Exhibit exhibit1 = new Exhibit(1, "topstuk", "a topstuk", null, new ArrayList<>(), "300-800 v.C.", 2, 1);
        Exhibit exhibit2 = new Exhibit(2, "topstuk", "a topstuk", null, new ArrayList<>(), "300-800 v.C.", 2, 1);
        List<Exhibit> exhibitList = new ArrayList<>();
        exhibitList.add(exhibit1);
        exhibitList.add(exhibit2);

        User mockUser = mock(User.class);
        when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
        when(librarian.getAvailableExhibitsFromEra(mockUser, 1)).thenReturn(exhibitList);
        //test
        Response actualResponse = service.getExhibitsFromEra(1, "token");
        //check
        JsonArray jsonArray = (JsonArray) actualResponse.getEntity();
        JsonObject object1 = (JsonObject) jsonArray.get(0);
        JsonObject object2 = (JsonObject) jsonArray.get(1);
        assertThat(actualResponse.getStatus(), is(200));
        assertThat(object1.getInt("ExhibitId"), is(1));
        assertThat(object2.getInt("ExhibitId"), is(2));
    }

    @Test
    public void getExhibitsFromEraTestEmptyArray() {
        //init
        List<Exhibit> exhibitList = new ArrayList<>();

        User mockUser = mock(User.class);
        when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
        when(librarian.getAvailableExhibitsFromEra(mockUser, 1)).thenReturn(exhibitList);
        //test
        Response actualResponse = service.getExhibitsFromEra(1, "token");
        //check
        assertThat(actualResponse.getEntity(), is(nullValue()));
        assertThat(actualResponse.getStatus(), is(200));
    }

    @Test
    public void getExhibitsFromEraInvalidUser() {
        //init
        when(tokenManager.getUserFromToken("token")).thenReturn(null);
        //test
        Response actualResponse = service.getExhibitsFromEra(1, "token");
        //check
        assertThat(actualResponse.getStatus(), is(401));
    }

    @Test
    public void placeReplicaTestSuccess() {
        //init
        JsonObject jo = Json.createObjectBuilder()
                .add("replicaId", 1)
                .add("positionId", 2)
                .build();
        User mockUser = mock(User.class);
        when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
        when(librarian.placeReplica(1, 2, mockUser)).thenReturn(1);
        //test
        Response actualResponse = service.placeReplica("token", jo);
        //check
        JsonObject jsonObject = (JsonObject) actualResponse.getEntity();
        assertThat(actualResponse.getStatus(), is(201));
        assertThat(jsonObject.getInt("reason"), is(1));
    }

    @Test
    public void placeReplicaTestInvaliduser() {
        //init
        JsonObject jo = Json.createObjectBuilder()
                .add("replicaId", 1)
                .add("positionId", 2)
                .build();
        when(tokenManager.getUserFromToken("token")).thenReturn(null);
        //test
        Response actualResponse = service.placeReplica("token", jo);
        //check
        assertThat(actualResponse.getStatus(), is(401));
    }

    @Test
    public void findMuseumTestSuccess() {
        //init
        User mockUser = mock(User.class);
        when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
        Museum museum = new Museum(1, "museum", "muse.um", "Amsterdam");
        when(librarian.findMuseum(1)).thenReturn(museum);
        //test
        Response actualResponse = service.findMuseum(1, "token");
        //check
        JsonObject jsonObject = (JsonObject) actualResponse.getEntity();
        assertThat(actualResponse.getStatus(), is(200));
        assertThat(jsonObject.getInt("MuseumId"), is(1));
        assertThat(jsonObject.getString("Name"), is(equalTo("museum")));
        assertThat(jsonObject.getString("Site"), is(equalTo("muse.um")));
        assertThat(jsonObject.getString("Region"), is(equalTo("Amsterdam")));
    }

    @Test
    public void findMuseumTestNoMuseum() {
        //init
        User mockUser = mock(User.class);
        when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
        when(librarian.findMuseum(1)).thenReturn(null);
        //test
        Response actualResponse = service.findMuseum(1, "token");
        //check
        assertThat(actualResponse.getEntity(), is(nullValue()));
        assertThat(actualResponse.getStatus(), is(200));
    }

    @Test
    public void findMuseumTestInvalidUser() {
        //init
        when(tokenManager.getUserFromToken("token")).thenReturn(null);
        //test
        Response actualResponse = service.findMuseum(1, "token");
        //check
        assertThat(actualResponse.getEntity(), is(nullValue()));
        assertThat(actualResponse.getStatus(), is(401));
    }

    @Test
    public void findEraTestSuccess() {
        //init
        User mockUser = mock(User.class);
        when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
        Era era = new Era(1, "era");
        when(librarian.findEra(mockUser, 1)).thenReturn(era);
        //test
        Response actualResponse = service.findEra(1, "token");
        //check
        JsonObject jsonObject = (JsonObject) actualResponse.getEntity();
        assertThat(actualResponse.getStatus(), is(200));
        assertThat(jsonObject.getInt("EraId"), is(1));
        assertThat(jsonObject.getString("Name"), is(equalTo("era")));
    }

    @Test
    public void findEraTestNoEra() {
        //init
        User mockUser = mock(User.class);
        when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
        when(librarian.findEra(mockUser, 1)).thenReturn(null);
        //test
        Response actualResponse = service.findEra(1, "token");
        //check
        assertThat(actualResponse.getEntity(), is(nullValue()));
        assertThat(actualResponse.getStatus(), is(200));
    }

    @Test
    public void findEraTestInvalidUser() {
        //init
        when(tokenManager.getUserFromToken("token")).thenReturn(null);
        //test
        Response actualResponse = service.findEra(1, "token");
        //check
        assertThat(actualResponse.getEntity(), is(nullValue()));
        assertThat(actualResponse.getStatus(), is(401));
    }

    @Test
    public void listMuseumTestSuccess() {
        //init
        User mockUser = mock(User.class);
        when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
        List<Museum> museumList = new ArrayList<>();
        Museum museum1 = new Museum(1, "museum", "muse.um", "Amsterdam");
        Museum museum2 = new Museum(2, "museum", "muse.um", "Amsterdam");
        museumList.add(museum1);
        museumList.add(museum2);
        when(librarian.listMuseums()).thenReturn(museumList);
        //test
        Response actualResponse = service.listMuseums("token");
        //check
        JsonArray jsonArray = (JsonArray) actualResponse.getEntity();
        JsonObject object1 = (JsonObject) jsonArray.get(0);
        JsonObject object2 = (JsonObject) jsonArray.get(1);
        assertThat(actualResponse.getStatus(), is(200));
        assertThat(object1.getInt("MuseumId"), is(1));
        assertThat(object2.getInt("MuseumId"), is(2));
    }

    @Test
    public void ListMuseumTestNoMuseum() {
        //init
        User mockUser = mock(User.class);
        when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
        List<Museum> museumList = new ArrayList<>();
        when(librarian.listMuseums()).thenReturn(museumList);
        //test
        Response actualResponse = service.listMuseums("token");
        //check
        JsonArray jsonArray = (JsonArray) actualResponse.getEntity();
        assertThat(actualResponse.getEntity(), is(nullValue()));
        assertThat(actualResponse.getStatus(), is(200));

    }

    @Test
    public void listMuseumTestInvalidUser() {
        //init
        when(tokenManager.getUserFromToken("token")).thenReturn(null);
        //test
        Response actualResponse = service.listMuseums("token");
        //check
        assertThat(actualResponse.getEntity(), is(nullValue()));
        assertThat(actualResponse.getStatus(), is(401));
    }

    @Test
    public void listEraTestSuccess() {
        //init
        User mockUser = mock(User.class);
        when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
        List<Era> eraList = new ArrayList<>();
        Era era1 = new Era(1, "oud");
        Era era2 = new Era(2, "nieuw");
        eraList.add(era1);
        eraList.add(era2);
        when(librarian.listEra(any())).thenReturn(eraList);
        //test
        Response actualResponse = service.listEra("token");
        //check
        JsonArray jsonArray = (JsonArray) actualResponse.getEntity();
        JsonObject object1 = (JsonObject) jsonArray.get(0);
        JsonObject object2 = (JsonObject) jsonArray.get(1);
        assertThat(actualResponse.getStatus(), is(200));
        assertThat(object1.getInt("EraId"), is(1));
        assertThat(object1.getString("Name"), is("oud"));
        assertThat(object2.getInt("EraId"), is(2));
        assertThat(object2.getString("Name"), is("nieuw"));
    }

    @Test
    public void ListEraTestNoEra() {
        //init
        User mockUser = mock(User.class);
        when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
        List<Era> eraList = new ArrayList<>();
        when(librarian.listEra(mockUser)).thenReturn(eraList);
        //test
        Response actualResponse = service.listEra("token");
        //check
        JsonArray jsonArray = (JsonArray) actualResponse.getEntity();
        assertThat(actualResponse.getEntity(), is(nullValue()));
        assertThat(actualResponse.getStatus(), is(200));

    }

    @Test
    public void listEraTestInvalidUser() {
        //init
        when(tokenManager.getUserFromToken("token")).thenReturn(null);
        //test
        Response actualResponse = service.listEra("token");
        //check
        assertThat(actualResponse.getEntity(), is(nullValue()));
        assertThat(actualResponse.getStatus(), is(401));
    }

    @Test
    public void verifyInvalidUser() {
        //init
        when(tokenManager.getUserFromToken("token")).thenReturn(null);
        //test
        Response actualResponse = service.verifyUser("token");
        //check
        assertThat(actualResponse.getStatus(), is(401));
    }

    @Test
    public void verifyValidUser() {
        //init
        when(tokenManager.getUserFromToken("token")).thenReturn(new User(1, "", "", "", 1, 10));
        //test
        Response actualResponse = service.verifyUser("token");
        //check
        assertThat(actualResponse.getStatus(), is(200));
    }

    @Test
    public void getUserInfoInvalidUser() {
        //init
        when(tokenManager.getUserFromToken("token")).thenReturn(null);
        //test
        Response actualResponse = service.getUserInfo("token");
        //check
        assertThat(actualResponse.getEntity(), is(nullValue()));
        assertThat(actualResponse.getStatus(), is(401));
    }

    @Test
    public void getUserInfoValidUser() {
        //init
        User user = new User(1, "test@email", "HASHEDPASSWORD", "John Doe", 1337, 2);
        when(tokenManager.getUserFromToken("token")).thenReturn(user);
        //test
        Response actualResponse = service.getUserInfo("token");
        //check
        JsonObject joResponse = (JsonObject) actualResponse.getEntity();
        assertEquals(joResponse.getInt("UserId"), user.getId());
        assertEquals(joResponse.getString("Name"), user.getDisplayName());
        assertEquals(joResponse.getString("Email"), user.getEmail());
        assertEquals(joResponse.getInt("Coins"), user.getCoins());
        assertEquals(joResponse.getInt("Language"), user.getLanguageId());
        assertThat(actualResponse.getStatus(), is(200));
    }

    @Test
    public void updateUserInvalidUser() {
        //init
        when(tokenManager.getUserFromToken("token")).thenReturn(null);
        JsonObject jo = Json.createObjectBuilder()
                .add("email", "test@mail.com")
                .add("displayName", "testuser")
                .add("password", "testpassword")
                .add("languageId", 1)
                .build();
        //test
        Response actualResponse = service.updateUser("token", jo);
        //check
        assertThat(actualResponse.getEntity(), is(nullValue()));
        assertThat(actualResponse.getStatus(), is(401));
    }

    @Test
    public void getNotificationsInvalidUser(){
        Response actualResponse = service.getNotifications("token");
        //check
        assertThat(actualResponse.getEntity(), is(nullValue()));
        assertThat(actualResponse.getStatus(), is(401));
    }

    @Test
    public void getNotificationsValidUser(){
        //init
        IUserDao dao = mock(IUserDao.class);
        DAOFactory.setUserDao(dao);
        User user = new User(1, "test@email", "HASHEDPASSWORD", "John Doe", 1337, 2);
        when(tokenManager.getUserFromToken("token")).thenReturn(user);
        List<Notification> expected = new ArrayList<Notification>();
        Notification n = new Notification(1, "NOW", "This test totally works", false,5);
        expected.add(n);
        when(dao.listNotification(user)).thenReturn(expected);
        //test

        Response actualResponse = service.getNotifications("token");
        //check
        JsonArray joResponse = (JsonArray) actualResponse.getEntity();
        JsonObject jo = joResponse.getJsonObject(0);
        assertEquals("This test totally works", jo.getString("Text"));
    }

	@Test
	public void removeAccountTestInvalidUser() {
		//test
		Response actualResponse = service.removeAccount("token");
		//check
		verify(tokenManager, times(0)).removeUserByToken("token");
		verify(librarian, times(0)).removeUser(any());
		assertThat(actualResponse.getStatus(), is(401));
	}

	@Test
	public void removeAccountTestSuccess() {
		//test
		User mockUser = mock(User.class);
		when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
		Response actualResponse = service.removeAccount("token");
		//check
		verify(tokenManager, times(1)).removeUserByToken("token");
		verify(librarian, times(1)).removeUser(any());
		assertThat(actualResponse.getStatus(), is(200));
	}


    @Test
    public void newExhibitNotificationTestSuccess() {
        //init
        User mockUser = mock(User.class);
        when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder job = factory.createObjectBuilder();
        job.add("exhibitId", 12);
        JsonObject jo = job.build();
        //test
        Response actualResponse = service.newExhibitNotification("token", jo);
        //check
        assertThat(actualResponse.getStatus(), is(200));
        verify(librarian, times(1)).addNotificationToEveryUser(any(), anyInt());
    }

    @Test
    public void newExhibitNotificationTestInvalidUser() {
        //init
        when(tokenManager.getUserFromToken("token")).thenReturn(null);
        //test
        Response actualResponse = service.newExhibitNotification("token", mock(JsonObject.class));
        //check
        assertThat(actualResponse.getStatus(), is(401));
    }

    @Test
    public void newReplicaNotificationTestSuccess() {
        //init
        User mockUser = mock(User.class);
        when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder job = factory.createObjectBuilder();
        job.add("replicaId", 12);
        JsonObject jo = job.build();
        //test
        Response actualResponse = service.newReplicaNotification("token", jo);
        //check
        assertThat(actualResponse.getStatus(), is(200));
        verify(librarian, times(1)).addNotificationToEveryUser(any(), anyInt());
    }

    @Test
    public void newReplicaNotificationTestInvalidUser() {
        //init
        when(tokenManager.getUserFromToken("token")).thenReturn(null);
        //test
        Response actualResponse = service.newReplicaNotification("token", mock(JsonObject.class));
        //check
        assertThat(actualResponse.getStatus(), is(401));
    }

    @Test
    public void newVideoNotificationTestSuccess() {
        //init
        User mockUser = mock(User.class);
        when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder job = factory.createObjectBuilder();
        job.add("exhibitId", 12);
        JsonObject jo = job.build();
        //test
        Response actualResponse = service.newVideoNotification("token", jo);
        //check
        assertThat(actualResponse.getStatus(), is(200));
        verify(librarian, times(1)).addNotificationToEveryUser(any(), anyInt());
    }

    @Test
    public void newVideoNotificationTestInvalidUser() {
        //init
        when(tokenManager.getUserFromToken("token")).thenReturn(null);
        //test
        Response actualResponse = service.newVideoNotification("token" , mock(JsonObject.class));
        //check
        assertThat(actualResponse.getStatus(), is(401));
    }

    @Test
    public void markNotificationTest() {
        //init
        User mockUser = mock(User.class);
        when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder job = factory.createObjectBuilder();
        job.add("Read", true);
        job.add("NotificationId", 1);
        JsonObject jo = job.build();
        //test
        Response actualResponse = service.markNotification("token", jo);
        //check
        assertThat(actualResponse.getStatus(), is(200));
        verify(librarian, times(1)).markNotification(mockUser, 1, true);
    }

    @Test
    public void markNotificationInvalidUser() {
        //init
        when(tokenManager.getUserFromToken("token")).thenReturn(null);
        //test
        Response actualResponse = service.markNotification("token" , mock(JsonObject.class));
        //check
        assertThat(actualResponse.getStatus(), is(401));
    }
	//register user tests
    @Test
    public void registerUserSuccessTest() {
        //init
        JsonObject object = Json.createObjectBuilder()
                .add("email", "email@eee.com")
                .add("password", "testPassword")
                .add("displayName", "username")
                .add("languageId", 1)
                .build();

        when(librarian.registerUser(object.getString("email"), object.getString("password"), object.getString("displayName"), object.getInt("languageId"))).thenReturn(0);
        when(librarian.getUserByEmail(anyString())).thenReturn(mock(User.class));
        when(token.getTokenString()).thenReturn("token");

        //test
        Response actualResponse = service.registerUser(object);

        //check
        verify(librarian, times(1)).getUserByEmail(object.getString("email"));
        assertThat(actualResponse.getStatus(), is(201));
        JsonObject jsonResponse = (JsonObject) actualResponse.getEntity();
        assertTrue(jsonResponse.containsKey("token"));
        assertFalse(jsonResponse.containsKey("reason"));
    }

    @Test
    public void registerUserInvalidPasswordTest() {
        //init
        JsonObject object = Json.createObjectBuilder()
                .add("email", "email@e.com")
                .add("password", "testPassword")
                .add("displayName", "username")
                .add("languageId", 1)
                .build();

        when(librarian.registerUser(object.getString("email"), object.getString("password"), object.getString("displayName"), object.getInt("languageId"))).thenReturn(1);

        //test
        Response actualResponse = service.registerUser(object);

        //check
        assertThat(actualResponse.getStatus(), is(417));
        JsonObject jsonResponse = (JsonObject) actualResponse.getEntity();
        assertFalse(jsonResponse.containsKey("token"));
        assertThat(jsonResponse.getInt("reason"), is(1));
    }

    @Test
    public void registerUserInvalidDisplayNameTest() {
        //init
        JsonObject object = Json.createObjectBuilder()
                .add("email", "email@e.com")
                .add("password", "testPassword")
                .add("displayName", "username")
                .add("languageId", 1)
                .build();

        when(librarian.registerUser(object.getString("email"), object.getString("password"), object.getString("displayName"), object.getInt("languageId"))).thenReturn(2);

        //test
        Response actualResponse = service.registerUser(object);

        //check
        assertThat(actualResponse.getStatus(), is(417));
        JsonObject jsonResponse = (JsonObject) actualResponse.getEntity();
        assertFalse(jsonResponse.containsKey("token"));
        assertThat(jsonResponse.getInt("reason"), is(2));
    }

    @Test
    public void registerUserInvalidEmailTest() {
        //init
        JsonObject object = Json.createObjectBuilder()
                .add("email", "email@e.com")
                .add("password", "testPassword")
                .add("displayName", "username")
                .add("languageId", 1)
                .build();

        when(librarian.registerUser(object.getString("email"), object.getString("password"), object.getString("displayName"), object.getInt("languageId"))).thenReturn(3);

        //test
        Response actualResponse = service.registerUser(object);

        //check
        assertThat(actualResponse.getStatus(), is(417));
        JsonObject jsonResponse = (JsonObject) actualResponse.getEntity();
        assertFalse(jsonResponse.containsKey("token"));
        assertThat(jsonResponse.getInt("reason"), is(3));
    }

    @Test
    public void registerUserExistingEmailTest() {
        //init
        JsonObject object = Json.createObjectBuilder()
                .add("email", "email@e.com")
                .add("password", "testPassword")
                .add("displayName", "username")
                .add("languageId", 1)
                .build();

        when(librarian.registerUser(object.getString("email"), object.getString("password"), object.getString("displayName"), object.getInt("languageId"))).thenReturn(4);

        //test
        Response actualResponse = service.registerUser(object);

        //check
        assertThat(actualResponse.getStatus(), is(417));
        JsonObject jsonResponse = (JsonObject) actualResponse.getEntity();
        assertFalse(jsonResponse.containsKey("token"));
        assertThat(jsonResponse.getInt("reason"), is(4));
    }

    @Test
    public void checkFavoriteExhibits(){
        User mockUser = mock(User.class);
        when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
        List<Exhibit> list = new ArrayList<>();
        when(librarian.getAvailableExhibits(mockUser)).thenReturn(list);
        list.add(new Exhibit(1, "", "", "", new ArrayList<>(), "",2,3));
        list.add(new Exhibit(3, "", "", "", new ArrayList<>(), "",2,3));
        Response r = service.getFavoriteExhibits("token");
        assertEquals(true, ((JsonArray)r.getEntity()).getJsonObject(0).getBoolean("Favorite") );
        assertEquals(1, ((JsonArray)r.getEntity()).size());
    }
}