package oose.p.c6.imd.service;

import oose.p.c6.imd.domain.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sun.invoke.empty.Empty;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
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
    public void helloTest() throws Exception {
        //init
        String expectedResult = "hello world";
        when(token.getTokenString()).thenReturn("token");
        when(librarian.getUserByEmail(anyString())).thenReturn(mock(User.class));
        when(librarian.verifyLogin(anyString(), anyString())).thenReturn(0);
        when(tokenManager.getTokenFromTokenString(anyString())).thenReturn(token);
        //test
        String actualResult = service.hello();
        //check
        assertThat(actualResult, is(equalTo(expectedResult)));
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
        List<Replica> expectedResult = new ArrayList<>();
        expectedResult.add(mock(Replica.class));
        expectedResult.add(mock(Replica.class));
        User mockUser = mock(User.class);
        when(tokenManager.getUserFromToken("token")).thenReturn(mockUser);
        when(librarian.getAvailableReplicas(mockUser)).thenReturn(expectedResult);
        //test
        Response actualResponse = service.getReplicas("token");
        //check
        List<Replica> actualResult = (List<Replica>) actualResponse.getEntity();
        assertThat(actualResult, is(expectedResult));
        assertThat(actualResponse.getStatus(), is(200));
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
    public void getExhibitDetailsTestSuccess() {
        //init
        Exhibit exhibit = new Exhibit(1, "topstuk", "a topstuk", null, null, 2020, 2, 1);
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
        assertThat(json.getString("Image"), is(equalTo("undefined")));
        assertThat(json.getInt("Year"), is(2020));
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
        Exhibit exhibit1 = new Exhibit(1, "topstuk", "a topstuk", null, null, 2020, 2, 1);
        Exhibit exhibit2 = new Exhibit(2, "topstuk", "a topstuk", null, null, 2020, 2, 1);
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
        Exhibit exhibit1 = new Exhibit(1, "topstuk", "a topstuk", null, null, 2020, 2, 1);
        Exhibit exhibit2 = new Exhibit(2, "topstuk", "a topstuk", null, null, 2020, 2, 1);
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
        Exhibit exhibit1 = new Exhibit(1, "topstuk", "a topstuk", null, null, 2020, 2, 1);
        Exhibit exhibit2 = new Exhibit(2, "topstuk", "a topstuk", null, null, 2020, 2, 1);
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
}