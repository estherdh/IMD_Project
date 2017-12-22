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

        if(replica == null) {
            // replica does not exist
            return 1;
        }
        if(!user.userHasReplica(replica)) {
            // user does not own replica
            return 2;
        }
        if(!isPositionForType(positionId, replica.getType())) {
            // position is not available for this type
            return 4;
        }
        if(!isPositionFree(user, replica, positionId)) {
            // position is not free
            return 3;
        }
        placeReplica(user, replica, positionId);
        // success
        return 0;
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

    private boolean isPositionForType(int positionId, int replicaTypeId) {
        List<Integer> positions = replicaDao.getPositionsForReplicaType(replicaTypeId);
        for (Integer i:positions) {
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
