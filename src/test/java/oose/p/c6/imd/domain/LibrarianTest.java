package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.IUserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
		//test
		librarian.scanQrCode(expectedUser, expectedQrCode);
		//init
		verify(expectedUser, times(1)).checkQuestCompleted(any(Action.class));
	}

}