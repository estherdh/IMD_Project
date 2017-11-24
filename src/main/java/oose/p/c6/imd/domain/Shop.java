package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.IReplicaDao;

import javax.inject.Inject;
import java.util.List;

public class Shop {
    @Inject
    private IReplicaDao replicaDao;

    public void buyReplica(User user, int replicaId) {
        Replica replica = replicaDao.find(replicaId);
        replicaDao.giveReplicaToUser(user, replica);
        user.removeCoins(replica.getPrice());
    }

    public List<Replica> getAvailableReplicas(User user) {
        return replicaDao.findAvailableReplicas(user);
    }
}
