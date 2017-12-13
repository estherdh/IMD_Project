package oose.p.c6.imd.persistent.dao;

public class DAOFactory {
    private DAOFactory(){
        throw new IllegalStateException("DAOFactory class");
    }
    public static IReplicaDao getReplicaDao(){
        return new ReplicaJDBCDao();
    }

    public static IUserDao getUserDao(){
        return new UserJDBCDao();
    }

    public static IQuestDAO getQuestDao(){
        return new QuestJDBCDao();
    }

    public static IExhibitDao getExhibitDao() { return new ExhibitJDBCDao(); }
}
