package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.IExhibitDao;
import oose.p.c6.imd.persistent.dao.IQuestDAO;
import oose.p.c6.imd.persistent.dao.IUserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LibrarianTest {
    @Mock
    IUserDao userDao;
    @Mock
    QuestGenerator questGenerator;
    @Mock
    Shop shop;
    @Mock
    IExhibitDao exhibit;
    @Mock
    Library library;
    @Mock
    IQuestDAO questDao;
    @InjectMocks
    Librarian librarian;


	@Test
	public void scanQrCodeTestSuccess() throws Exception {
		//init
		User expectedUser = mock(User.class);
		String expectedQrCode = "a qr code";
		when(expectedUser.checkQuestCompleted(any(Action.class))).thenReturn(true);
		//test
		librarian.scanQrCode(expectedUser, expectedQrCode);
		//init
		verify(expectedUser, times(1)).checkQuestCompleted(any(Action.class));
		verify(userDao, times(1)).update(expectedUser);
	}

	@Test
	public void removeQuestFromQuestLogTest() {
		//init
		User mockUser = mock(User.class);
		when(mockUser.removeQuestFromQuestLog(1)).thenReturn(true);
		//test
		boolean actualResult = librarian.removeQuestFromQuestLog(1, mockUser);
		//check
		assertTrue(actualResult);
	}

	@Test
	public void removeUserTestSuccess() {
		//test
		User mockUser = mock(User.class);
		//test
		librarian.removeUser(mockUser);
		//verify
		verify(userDao, times(1)).remove(mockUser);
	}

    @Test
    public void registerUserSuccesTest() {
        //init
        String email = "hoi@hooi.nl";
        String displayName = "hallo123";
        String password = "Hee456";
        int languageId = 1;
		User mockUser = mock(User.class);
		when(userDao.findUserByEmail("hoi@hooi.nl")).thenReturn(null).thenReturn(mockUser);
		when(mockUser.getId()).thenReturn(0);

		//test
		int actualResponse = librarian.registerUser(email, password, displayName, languageId);

        //check
		verify(userDao, times(1)).add(any(User.class));
		verify(questGenerator, times(3)).generateQuest(0);
        assertThat(actualResponse, is(0));
    }

    @Test
    public void registerUserAlreadyExistingEmailTest() {
        //init
        String email = "hoi@hoi.nl";
        String displayName = "hallo123";
        String password = "Hee456";
        int languageId = 1;
        User mockUser = mock(User.class);
        when(userDao.findUserByEmail("hoi@hoi.nl")).thenReturn(mockUser);

        //test
        int actualResponse = librarian.registerUser(email, password, displayName, languageId);

        //check
        assertThat(actualResponse, is(4));
    }

    @Test
    public void buyReplicaTest() {
        //init
        User mockUser = mock(User.class);
        int replicaId = 1;

        //test
        librarian.buyReplica(mockUser, replicaId);

        //check
        verify(shop, times(1)).buyReplica(mockUser, replicaId);
    }

    @Test
    public void getAvailableReplicasTest() {
        //init
        User mockUser = mock(User.class);

        //test
        librarian.getAvailableReplicas(mockUser);

        //check
        verify(shop, times(1)).getAvailableReplicas(mockUser);
    }

    @Test
    public void getExhibitDetailsTest() {
        //init
        User mockUser = mock(User.class);
        int exhibitId = 1;

        //test
        librarian.getExhibitDetails(mockUser, exhibitId);

        //check
        verify(exhibit, times(1)).find(mockUser, exhibitId);
    }

    @Test
    public void getAvailableExhibitsFromEraTest() {
        //init
        User mockUser = mock(User.class);
        int eraId = 1;

        //test
        librarian.getAvailableExhibitsFromEra(mockUser, eraId);

        //check
        verify(exhibit, times(1)).listByEra(mockUser, eraId);
    }

    @Test
    public void getAvailableExhibitsFromMuseumTest() {
        //init
        User mockUser = mock(User.class);
        int museumId = 1;

        //test
        librarian.getAvailableExhibitsFromMuseum(mockUser, museumId);

        //check
        verify(exhibit, times(1)).listByMuseum(mockUser, museumId);
    }

    @Test
    public void getAvailableExhibitsTest() {
        //init
        User mockUser = mock(User.class);

        //test
        librarian.getAvailableExhibits(mockUser);

        //check
        verify(exhibit, times(1)).list(mockUser);
    }

    @Test
    public void findEraTest() {
        //init
        User mockUser = mock(User.class);
        int eraId = 1;

        //test
        librarian.findEra(mockUser, eraId);

        //check
        verify(exhibit, times(1)).findEra(mockUser, eraId);
    }

    @Test
    public void listEraTest() {
        //init
        User mockUser = mock(User.class);

        //test
        librarian.listEra(mockUser);

        //check
        verify(exhibit, times(1)).listEra(mockUser);
    }

    @Test
    public void findMuseumTest() {
        //init
        int museumId = 1;

        //test
        librarian.findMuseum(museumId);

        //check
        verify(exhibit, times(1)).findMuseum(museumId);
    }

    @Test
    public void listMuseumsTest() {
        //test
        librarian.listMuseums();

        //check
        verify(exhibit, times(1)).listMuseums();
    }

    @Test
    public void updateUserTest() {
        //init
        String email = "test@test.test";
        String displayName = "tester1";
        String password = "Password1";
        int languageId = 1;
        User mockUser = mock(User.class);

        //test
        librarian.updateUser(email, displayName, password, languageId, mockUser);

        //check
        verify(mockUser, times(1)).updateUser(email, displayName, password, languageId, mockUser);
    }

    @Test
    public void placeReplicaTest() {
        //init
        int positionId = 1;
        int replicaId = 1;
        User mockUser = mock(User.class);

        //test
        librarian.placeReplica(replicaId, positionId, mockUser);

        //check
        verify(library, times(1)).tryPlaceReplica(mockUser, replicaId, positionId);
    }

    @Test
    public void getQuestLogTest() {
        //init
        User mockUser = mock(User.class);

        //test
        librarian.getQuestLog(mockUser);

        //check
        verify(questDao, times(1)).getQuestsForUser(anyInt(), anyInt());
    }

    @Test
    public void markNotificationTest() {
        //init
        int notificationId = 1;
        boolean read = true;
        User mockUser = mock(User.class);

        //test
        librarian.markNotification(mockUser, notificationId, read);

        //check
        verify(mockUser, times(1)).markNotification(notificationId, read);
    }

    @Test
    public void getReplicasFromUserTest() {
        //init
        User mockUser = mock(User.class);

        //test
        librarian.getReplicasFromUser(mockUser);

        //check
        verify(mockUser, times(1)).getReplicas();
    }

    @Test
    public void verifyLoginNonExistingUserTest() {
        //init
        String email = "test@test.test";
        String password = "Password1";
        when(userDao.findUserByEmail("test@test.test")).thenReturn(null);

        //test
        int verification = librarian.verifyLogin(email, password);

        //check
        assertThat(verification, is(1));
    }

    @Test
    public void verifyLoginInvalidCredentialsTest() {
        //init
        String email = "test@test.test";
        String password = "Password1";
        User mockUser = mock(User.class);
        when(userDao.findUserByEmail("test@test.test")).thenReturn(mockUser);
        when(mockUser.passwordCorrect("Password1")).thenReturn(false);

        //test
        int verification = librarian.verifyLogin(email, password);

        //check
        assertThat(verification, is(2));
    }

    @Test
    public void verifyLoginSuccesTest() {
        //init
        String email = "test@test.test";
        String password = "Password1";
        User mockUser = mock(User.class);
        when(userDao.findUserByEmail("test@test.test")).thenReturn(mockUser);
        when(mockUser.passwordCorrect("Password1")).thenReturn(true);

        //test
        int verification = librarian.verifyLogin(email, password);

        //check
        assertThat(verification, is(0));
    }

    @Test
    public void addNotificationToEveryUser() {
        //init
        Map<String, String> variables = new HashMap<>();
        int typeId = 1;
        User mockUser = mock(User.class);
        List<User> mockUsers = new ArrayList<>();
        mockUsers.add(mockUser);
        when(userDao.list()).thenReturn(mockUsers);

        //test
        librarian.addNotificationToEveryUser(variables, typeId);

        //check
        verify(mockUser, times(1)).addNotification(typeId, variables);
    }
}