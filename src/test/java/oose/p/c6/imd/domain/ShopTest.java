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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

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
            add(new Replica(1, 1, 12, "test", 1));
            add(new Replica(3, 2, 20, "test", 1));
        }});

        Mockito.when(replicaDao.findAvailableReplicas(any(User.class))).thenReturn(result);
        Mockito.when(replicaDao.find(1)).thenReturn(new Replica(1, 1, 12, "test", 1));
    }

    @Test
    public void getReplicasTest() {
        User user = mock(User.class);
        List<Replica> replicaList = shop.getAvailableReplicas(user);

        assertEquals(result, replicaList);
    }

    @Test
    public void buyReplicaEnoughCoinsTest() {
        User user = mock(User.class);
        Mockito.when(user.getCoins()).thenReturn(200);
        boolean failed = shop.buyReplica(user, 1);

        assertEquals(true, failed);
        Mockito.verify(user, times(1)).addReplicaToInventory(any(Replica.class));
        Mockito.verify(user, times(1)).getCoins();
        Mockito.verify(user, times(1)).removeCoins(anyInt());
    }

    @Test
    public void buyReplicaNotEnoughCoinsTest() {
        User user = mock(User.class);
        Mockito.when(user.getCoins()).thenReturn(2);
        boolean failed = shop.buyReplica(user, 1);

        assertEquals(false, failed);
        Mockito.verify(user, times(0)).addReplicaToInventory(any(Replica.class));
        Mockito.verify(user, times(1)).getCoins();
        Mockito.verify(user, times(0)).removeCoins(anyInt());
    }
}
