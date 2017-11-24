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
        Mockito.when(users.findUserByemail("test@test.com")).thenReturn(new User(1, "test@test.com", "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08", "Test", 0));
    }

    @Test
    public void correctPasswordAndEmail()
    {
        boolean isCorrect = librarian.verifyLogin("test@test.com", "test");
        assertEquals(true, isCorrect);
    }

    @Test
    public void wrongPasswordAndEmail()
    {
        boolean isCorrect = librarian.verifyLogin("test@test.com", "fout");
        assertEquals(false, isCorrect);
    }
}
