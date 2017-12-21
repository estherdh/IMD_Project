package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.IUserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LibrarianTest {
	@Mock
	IUserDao userDao;
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
        String email = "hoi@hoi.nl";
        String displayName = "hallo123";
        String password = "Hee456";
        int languageId = 1;

        //test
        int actualResponse = librarian.registerUser(email, password, displayName, languageId);

        //check
        assertThat(actualResponse, is(0));
    }
}