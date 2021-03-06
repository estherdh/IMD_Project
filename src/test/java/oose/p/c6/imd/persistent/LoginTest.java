package oose.p.c6.imd.persistent;

import oose.p.c6.imd.domain.Librarian;
import oose.p.c6.imd.domain.User;
import oose.p.c6.imd.persistent.dao.IUserDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class LoginTest
{
    @Mock
    private IUserDao users;

    @InjectMocks
    private Librarian librarian;

    @Before
    public void setUp()
    {
        Mockito.when(users.findUserByEmail("test@test.com")).thenReturn(new User(1, "test@test.com", "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08", "Test", 0, 1));
    }

    @Test
    public void correctPasswordAndEmail()
    {
        int loginState = librarian.verifyLogin("test@test.com", "test");
        assertEquals(0, loginState);
    }

    @Test
    public void wrongEmail()
    {
        int loginState = librarian.verifyLogin("fout@fout.com", "test");
        assertEquals(1, loginState);
    }

    @Test
    public void wrongPassword()
    {
        int loginState = librarian.verifyLogin("test@test.com", "fout");
        assertEquals(2, loginState);
    }
}
