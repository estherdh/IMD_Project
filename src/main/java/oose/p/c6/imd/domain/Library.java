package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.IReplicaDao;

import javax.inject.Inject;
import java.util.List;

public class Library
{
    @Inject
    private IReplicaDao replicaDao;

    public int tryPlaceReplica(User user, int replicaId, int positionId) {
        Replica replica = getReplica(replicaId);

        if(replica != null) {
            if(user.userHasReplica(replica)) {
                if(isPositionFree(user, replica, positionId)) {
                    placeReplica(user, replica, positionId);
                    // success
                    return 0;
                }
                // position is not free
                return 3;
            }
            // user does not own replica
            return 2;
        }
        // replica does not exist
        return 1;
    }

    private Replica getReplica(int replicaId) {
        return replicaDao.find(replicaId);
    }

    private boolean isPositionFree(User user, Replica replica, int positionId) {
        List<Integer> replicaList = replicaDao.getFreePositions(user, replica.getType());
        for (int i: replicaList) {
            if(i == positionId) {
                return true;
            }
        }
        return false;
    }

    private void placeReplica(User user, Replica replica, int positionId) {
        replicaDao.updateReplicaPosition(user, replica, positionId);
    }
}
