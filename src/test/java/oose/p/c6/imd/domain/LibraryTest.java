package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.IReplicaDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@RunWith(MockitoJUnitRunner.class)
public class LibraryTest
{
    @Mock
    private IReplicaDao replicaDao;

    @InjectMocks
    private User user;

    @InjectMocks
    private Library library;

    @Before
    public void setUp() {
        user = new User(1, "test@tset", "test", "test", 12, 1);

        List<Replica> replicasFromUser = new ArrayList<Replica>() {{
            add(new Replica(1, 1, 12, "test", 1));
            add(new Replica(2, 1, 18, "test", 1));
            add(new Replica(3, 1, 15, "test", 2));
        }};

        List<Integer> freePositions = new ArrayList<Integer>() {{
           add(1);
           add(2);
        }};

        Mockito.when(replicaDao.getReplicasFromUser(any(User.class))).thenReturn(replicasFromUser);
        Mockito.when(replicaDao.getFreePositions(any(User.class), anyInt())).thenReturn(freePositions);
        Mockito.when(replicaDao.find(1)).thenReturn(new Replica(1, 1, 12, "test", 1));
    }

    @Test
    public void placeReplicaTestReplicaDoesNotExist() {
        // test
        int result = library.tryPlaceReplica(user, 39, 1);
        // check result
        assertThat(result, is(1));
    }

    @Test
    public void placeReplicaTestUserDoesNotOwnReplica() {
        Mockito.when(replicaDao.find(5)).thenReturn(new Replica(1, 1, 12, "test", 1));
        // test
        int result = library.tryPlaceReplica(user, 5, 1);
        // check result
        assertThat(result, is(2));
    }

    @Test
    public void placeReplicaTestPositionIsNotFree() {
        // test
        int result = library.tryPlaceReplica(user, 1, 3);
        // check result
        assertThat(result, is(3));
    }
}
