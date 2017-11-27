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

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class ShopTest
{
    private List<Replica> result;

    @Mock
    private IReplicaDao replicaDao;

    @InjectMocks
    private Shop shop;

    @Before
    public void setUp() {
        result = (new ArrayList<Replica>(){{
            add(new Replica(1, 1, 12, "test", "boek", 1));
            add(new Replica(3, 2, 20, "test", "boek", 2));
        }});

        Mockito.when(replicaDao.findAvailableReplicas(any(User.class))).thenReturn(result);
        Mockito.when(replicaDao.find(1)).thenReturn(new Replica(1, 1, 12, "test", "boek", 1));
    }

    @Test
    public void getReplicasTest() {
        User user = new User(1, "test@test.com", "test", "peter", 200, 1);
        List<Replica> replicaList = shop.getAvailableReplicas(user);
        assertEquals(result, replicaList);
    }

    @Test
    public void buyReplicaEnoughCoinsTest() {
        User user = new User(1, "test@test.com", "test", "peter", 200, 1);
        boolean failed = shop.buyReplica(user, 1);
        assertEquals(true, failed);
    }

    @Test
    public void buyReplicaNotEnoughCoinsTest() {
        User user = new User(1, "test@test.com", "test", "peter", 2, 1);
        boolean failed = shop.buyReplica(user, 1);
        assertEquals(false, failed);
    }
}
