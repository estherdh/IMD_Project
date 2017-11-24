package oose.p.c6.imd.persistent.dao;

public class DAOFactory {
    public static IReplicaDao getReplicaDao(){
        return new ReplicaJDBCDao();
    }

    public static IUserDao getUserDao(){
        return new UserJDBCDao();
    }
}