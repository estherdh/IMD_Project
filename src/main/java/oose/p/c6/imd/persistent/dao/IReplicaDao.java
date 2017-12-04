package oose.p.c6.imd.persistent.dao;

import oose.p.c6.imd.domain.Replica;
import oose.p.c6.imd.domain.User;

import java.util.List;

public interface IReplicaDao extends IDao<Replica> {
    List<Replica> findAvailableReplicas(User user);
    void giveReplicaToUser(User user, Replica replica);
    void updateReplicaPosition(User user, Replica replica, int position);
    List<Integer> getFreePositions(User user, int replicaType);
    List<Replica> getReplicasFromUser(User user);
}
