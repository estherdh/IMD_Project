package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.IReplicaDao;

import javax.inject.Inject;
import java.util.List;

public class Shop {

    @Inject
    private IReplicaDao replicaDao;

    public boolean buyReplica(User user, int replicaId) {
        Replica replica = getReplica(replicaId);

        if(user.getCoins() >= replica.getPrice()) {
            if(isReplicaAvailable(user, replicaId)) {
                user.addReplicaToInventory(replica);
                user.removeCoins(replica.getPrice());
            }
            return true;
        }
        return false;
    }

    private Replica getReplica(int id) {
        return replicaDao.find(id);
    }

    public List<Replica> getAvailableReplicas(User user) {
        return replicaDao.findAvailableReplicas(user);
    }

    protected boolean isReplicaAvailable(User user, int replicaId) {
        for (Replica r : getAvailableReplicas(user)) {
            if(r.getId() == replicaId) {
               return true;
            }
        }
        return false;
    }
}
